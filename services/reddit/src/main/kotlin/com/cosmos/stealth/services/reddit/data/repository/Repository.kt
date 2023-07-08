package com.cosmos.stealth.services.reddit.data.repository

import com.cosmos.stealth.core.common.util.extension.interlace
import com.cosmos.stealth.core.model.api.After
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.CommunityResults
import com.cosmos.stealth.core.model.api.Feed
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.FeedableResults
import com.cosmos.stealth.core.model.api.Post
import com.cosmos.stealth.core.model.api.PostFeedable
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.model.api.UserInfo
import com.cosmos.stealth.core.model.api.UserResults
import com.cosmos.stealth.core.model.api.string
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.network.data.repository.NetworkRepository
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.base.util.extension.isSuccess
import com.cosmos.stealth.services.base.util.extension.map
import com.cosmos.stealth.services.base.util.extension.orInternalError
import com.cosmos.stealth.services.base.util.extension.toAfter
import com.cosmos.stealth.services.base.util.extension.toStatus
import com.cosmos.stealth.services.reddit.data.mapper.CommentMapper
import com.cosmos.stealth.services.reddit.data.mapper.CommunityMapper
import com.cosmos.stealth.services.reddit.data.mapper.PostMapper
import com.cosmos.stealth.services.reddit.data.mapper.UserMapper
import com.cosmos.stealth.services.reddit.data.model.AboutChild
import com.cosmos.stealth.services.reddit.data.model.AboutUserChild
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.CommentChild
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.cosmos.stealth.services.reddit.data.model.MoreChildren
import com.cosmos.stealth.services.reddit.data.model.PostChild
import com.cosmos.stealth.services.reddit.data.model.Sort
import com.cosmos.stealth.services.reddit.data.model.Sorting
import com.cosmos.stealth.services.reddit.util.joinSubredditList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import kotlin.math.ceil

@Suppress("TooManyFunctions")
abstract class Repository(
    private val postMapper: PostMapper,
    private val communityMapper: CommunityMapper,
    private val userMapper: UserMapper,
    private val commentMapper: CommentMapper,
    private val defaultDispatcher: CoroutineDispatcher
) : NetworkRepository() {

    suspend fun getSubreddit(
        request: Request,
        subreddit: List<String>,
        sorting: Sorting,
        after: String?
    ): Feed {
        return if (subreddit.size > REDDIT_SUBREDDIT_LIMIT) {
            getMultiSubreddit(request, subreddit, sorting, after)
        } else {
            getSubreddit(request, joinSubredditList(subreddit), sorting, after)
        }
    }

    private suspend fun getMultiSubreddit(
        request: Request,
        subreddit: List<String>,
        sorting: Sorting,
        after: String?
    ): Feed = supervisorScope {
        // Find the optimal chunk size to have lists of similar sizes
        val chunkSize = ceil(subreddit.size / ceil(subreddit.size / REDDIT_SUBREDDIT_LIMIT.toDouble())).toInt()

        val keys = after?.split(KEY_DELIMITER)

        val queries = withContext(defaultDispatcher) {
            subreddit
                // Step 1: Split the subreddit list into chunks
                .chunked(chunkSize)
                // Step 2: Create the query string for each chunk
                .map { joinSubredditList(it) }
                // Step 3: Map each chunk with its `after` key (if available)
                .mapIndexed { index, chunkedList -> chunkedList to keys?.getOrNull(index) }
        }

        // Step 4: Request the posts for each chunk in parallel
        val responses = queries
            .map { async { getSubreddit(request, it.first, sorting, it.second) } }
            .awaitAll()

        val feedables = mutableListOf<List<Feedable>>()
        val afters = mutableListOf<After?>()

        responses.forEach { response ->
            val status = response.status.firstOrNull().orInternalError(request.service)

            when {
                status.isSuccess -> {
                    feedables.add(response.items)
                    afters.add(response.after?.firstOrNull())
                }
                else -> return@supervisorScope Feed(listOf(), null, listOf(status))
            }
        }

        // Step 5: Flatten (and sort) the responses in order to have a single list of posts
        val data = withContext(defaultDispatcher) { feedables.sort(sorting) }

        // Step 6: Retrieve the `after` key for each response and join them to a single string
        val afterKeys = afters
            .joinToString(separator = KEY_DELIMITER) { it?.key?.string ?: "" }
            .toAfter(request.service)

        val status = Status(request.service, HttpURLConnection.HTTP_OK)

        Feed(data, listOf(afterKeys), listOf(status))
    }

    abstract suspend fun getSubreddit(request: Request, subreddit: String, sorting: Sorting, after: String?): Feed

    @Suppress("UNCHECKED_CAST")
    protected suspend fun getSubreddit(response: Resource<Listing>, request: Request): Feed {
        val status = response.toStatus(request.service)
            .run { listOf(this) }

        return when (response) {
            is Resource.Success -> {
                val items = postMapper.dataToEntities(response.data.data.children as List<PostChild>, request.service)
                val afterData = response.data.data.after
                    ?.toAfter(request.service)
                    ?.run { listOf(this) }

                Feed(items, afterData, status)
            }

            else -> Feed(listOf(), null, status)
        }
    }

    abstract suspend fun getSubredditInfo(request: Request, subreddit: String): Resource<CommunityInfo>

    protected suspend fun getSubredditInfo(request: Request, apiCall: suspend () -> Child): Resource<CommunityInfo> {
        return safeApiCall(apiCall) { communityMapper.dataToEntity(it as AboutChild, request.service) }
    }

    abstract suspend fun searchInSubreddit(
        request: Request,
        subreddit: String,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults>

    @Suppress("UNCHECKED_CAST")
    protected suspend fun searchInSubreddit(request: Request, apiCall: suspend () -> Listing): Resource<SearchResults> {
        return safeApiCall(apiCall) {
            val results = postMapper.dataToEntities(it.data.children as List<PostChild>, request.service)
            FeedableResults(results)
        }
    }

    abstract suspend fun getPost(request: Request, permalink: String, limit: Int?, sort: Sort): Resource<Post>

    protected suspend fun getPost(
        response: Resource<List<Listing>>,
        request: Request
    ): Resource<Post> = supervisorScope {
        response.map {
            val postChild = it[0].data.children[0] as PostChild

            val post = async { postMapper.dataToEntity(postChild, request.service) }
            val replies = async {
                commentMapper.dataToEntities(it[1].data.children, request.service, postChild.data)
            }

            Post(post.await(), replies.await())
        }
    }

    abstract suspend fun getMoreChildren(
        request: Request,
        children: List<String>,
        linkId: String
    ): Resource<List<Feedable>>

    protected suspend fun getMoreChildren(
        request: Request,
        apiCall: suspend () -> MoreChildren
    ): Resource<List<Feedable>> {
        return safeApiCall(apiCall) { commentMapper.dataToEntities(it.json.data.things, request.service, null) }
    }

    abstract suspend fun getUserInfo(request: Request, user: String): Resource<UserInfo>

    protected suspend fun getUserInfo(request: Request, apiCall: suspend () -> Child): Resource<UserInfo> {
        return safeApiCall(apiCall) { userMapper.dataToEntity(it as AboutUserChild, request.service) }
    }

    abstract suspend fun getUserPosts(request: Request, user: String, sorting: Sorting, after: String?): Feed

    protected suspend fun getUserPosts(response: Resource<Listing>, request: Request): Feed {
        val status = response.toStatus(request.service)
            .run { listOf(this) }

        @Suppress("UNCHECKED_CAST")
        return when (response) {
            is Resource.Success -> {
                val items = postMapper.dataToEntities(response.data.data.children as List<PostChild>, request.service)
                val afterData = response.data.data.after
                    ?.toAfter(request.service)
                    ?.run { listOf(this) }

                Feed(items, afterData, status)
            }

            else -> Feed(listOf(), null, status)
        }
    }

    abstract suspend fun getUserComments(request: Request, user: String, sorting: Sorting, after: String?): Feed

    @Suppress("UNCHECKED_CAST")
    protected suspend fun getUserComments(response: Resource<Listing>, request: Request): Feed {
        val status = response.toStatus(request.service)
            .run { listOf(this) }

        return when (response) {
            is Resource.Success -> {
                val items = commentMapper.dataToEntities(
                    response.data.data.children as List<CommentChild>,
                    request.service,
                    null
                )
                val afterData = response.data.data.after
                    ?.toAfter(request.service)
                    ?.run { listOf(this) }

                Feed(items, afterData, status)
            }

            else -> Feed(listOf(), null, status)
        }
    }

    abstract suspend fun searchPost(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults>

    @Suppress("UNCHECKED_CAST")
    protected suspend fun searchPost(request: Request, apiCall: suspend () -> Listing): Resource<SearchResults> {
        return safeApiCall(apiCall) {
            val results = postMapper.dataToEntities(it.data.children as List<PostChild>, request.service)
            FeedableResults(results)
        }
    }

    abstract suspend fun searchUser(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults>

    @Suppress("UNCHECKED_CAST")
    protected suspend fun searchUser(request: Request, apiCall: suspend () -> Listing): Resource<SearchResults> {
        return safeApiCall(apiCall) {
            val results = userMapper.dataToEntities(it.data.children as List<AboutUserChild>, request.service)
            UserResults(results)
        }
    }

    abstract suspend fun searchSubreddit(
        request: Request,
        query: String,
        sorting: Sorting,
        after: String?
    ): Resource<SearchResults>

    @Suppress("UNCHECKED_CAST")
    protected suspend fun searchSubreddit(request: Request, apiCall: suspend () -> Listing): Resource<SearchResults> {
        return safeApiCall(apiCall) {
            val results = communityMapper.dataToEntities(it.data.children as List<AboutChild>, request.service)
            CommunityResults(results)
        }
    }

    private fun List<List<Feedable>>.sort(sorting: Sorting): List<Feedable> {
        return when (sorting.generalSorting) {
            // If sorting is set to NEW, simply flatten the lists and sort the posts by date
            Sort.NEW -> this.flatten().sortedByDescending { (it as PostFeedable).created }
            // If sorting is set to TOP, simply flatten the lists and sort the posts by score
            Sort.TOP -> this.flatten().sortedByDescending { (it as PostFeedable).upvotes }
            // For all the other sorting methods, interlace the lists to have a consistent result
            // [['a', 'b', 'c'], ['e', 'f', 'g'], ['h', 'i']] ==> ['a', 'e', 'h', 'b', 'f', 'i', 'c', 'g']
            else -> this.interlace()
        }
    }

    companion object {
        private const val REDDIT_SUBREDDIT_LIMIT = 100
        private const val KEY_DELIMITER = ","
    }
}

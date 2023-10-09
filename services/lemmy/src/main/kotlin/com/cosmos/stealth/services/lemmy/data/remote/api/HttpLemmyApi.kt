package com.cosmos.stealth.services.lemmy.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.core.network.util.extension.forward
import com.cosmos.stealth.services.lemmy.data.model.CommentSortType
import com.cosmos.stealth.services.lemmy.data.model.GetCommentsResponse
import com.cosmos.stealth.services.lemmy.data.model.GetCommunityResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPersonDetailsResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPostResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPostsResponse
import com.cosmos.stealth.services.lemmy.data.model.SearchResponse
import com.cosmos.stealth.services.lemmy.data.model.SearchType
import com.cosmos.stealth.services.lemmy.data.model.SortType
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_COMMENT_LIST_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_COMMUNITY_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_POST_LIST_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_POST_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_SEARCH_V3
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi.Endpoint.GET_USER_V3
import com.cosmos.stealth.services.lemmy.di.LemmyModule.Qualifier.LEMMY_QUALIFIER
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
@Named(LEMMY_QUALIFIER)
class HttpLemmyApi(
    @Named(LEMMY_QUALIFIER) private val client: HttpClient,
    private val urlSubstitutor: UrlSubstitutor
) : LemmyApi {

    override suspend fun getPosts(
        instance: String,
        communityName: String?,
        sort: SortType?,
        page: Int?,
        limit: Int?,
        host: String?
    ): GetPostsResponse {
        val url = urlSubstitutor.buildUrl(instance, GET_POST_LIST_V3)

        return client.get(url) {
            forward(host, host == null)

            parameter("community_name", communityName)
            parameter("sort", sort?.value)
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }

    override suspend fun getCommunity(instance: String, name: String, host: String?): GetCommunityResponse {
        val url = urlSubstitutor.buildUrl(instance, GET_COMMUNITY_V3)

        return client.get(url) {
            forward(host, host == null)

            parameter("name", name)
        }.body()
    }

    override suspend fun getPost(instance: String, id: Int, host: String?): GetPostResponse {
        val url = urlSubstitutor.buildUrl(instance, GET_POST_V3)

        return client.get(url) {
            forward(host, host == null)

            parameter("id", id)
        }.body()
    }

    override suspend fun getUser(
        instance: String,
        username: String,
        sort: SortType?,
        page: Int?,
        limit: Int?,
        host: String?
    ): GetPersonDetailsResponse {
        val url = urlSubstitutor.buildUrl(instance, GET_USER_V3)

        return client.get(url) {
            forward(host, host == null)

            parameter("username", username)
            parameter("sort", sort?.value)
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }

    override suspend fun getComments(
        instance: String,
        postId: Int?,
        parentId: Int?,
        sort: CommentSortType?,
        page: Int?,
        limit: Int?,
        host: String?
    ): GetCommentsResponse {
        val url = urlSubstitutor.buildUrl(instance, GET_COMMENT_LIST_V3)

        return client.get(url) {
            forward(host, host == null)

            parameter("post_id", postId)
            parameter("parent_id", parentId)
            parameter("sort", sort?.value)
            parameter("page", page)
            parameter("limit", limit)

            // TODO: Remove hardcoded parameters
            parameter("max_depth", LemmyApi.MAX_DEPTH)
        }.body()
    }

    override suspend fun search(
        instance: String,
        query: String,
        type: SearchType,
        communityName: String?,
        sort: SortType?,
        page: Int?,
        limit: Int?,
        host: String?
    ): SearchResponse {
        val url = urlSubstitutor.buildUrl(instance, GET_SEARCH_V3)

        return client.get(url) {
            forward(host, host == null)

            parameter("q", query)
            parameter("type_", type.value)
            parameter("community_name", communityName)
            parameter("sort", sort?.value)
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }
}

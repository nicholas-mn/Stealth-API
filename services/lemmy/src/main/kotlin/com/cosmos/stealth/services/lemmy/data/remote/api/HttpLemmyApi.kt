package com.cosmos.stealth.services.lemmy.data.remote.api

import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.core.network.util.extension.forward
import com.cosmos.stealth.core.network.util.getEndpoint
import com.cosmos.stealth.core.network.util.getQueryParameter
import com.cosmos.stealth.services.lemmy.data.model.GetCommentsResponse
import com.cosmos.stealth.services.lemmy.data.model.GetCommunityResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPersonDetailsResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPostResponse
import com.cosmos.stealth.services.lemmy.data.model.GetPostsResponse
import com.cosmos.stealth.services.lemmy.data.model.SearchResponse
import com.cosmos.stealth.services.lemmy.data.model.SearchType
import com.cosmos.stealth.services.lemmy.data.model.SortType
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

    @Suppress("MagicNumber")
    override suspend fun getPosts(
        instance: String,
        communityName: String,
        sort: SortType?,
        page: Int?,
        limit: Int?,
        host: String?
    ): GetPostsResponse {
        val endpoint = LemmyApi::getPosts.getEndpoint()

        val url = urlSubstitutor.buildUrl(instance, endpoint)

        return client.get(url) {
            forward(host, host == null)

            parameter(LemmyApi::getPosts.getQueryParameter(0), communityName)
            parameter(LemmyApi::getPosts.getQueryParameter(1), sort?.value)
            parameter(LemmyApi::getPosts.getQueryParameter(2), page)
            parameter(LemmyApi::getPosts.getQueryParameter(3), limit)
        }.body()
    }

    override suspend fun getCommunity(instance: String, name: String, host: String?): GetCommunityResponse {
        val endpoint = LemmyApi::getCommunity.getEndpoint()

        val url = urlSubstitutor.buildUrl(instance, endpoint)

        return client.get(url) {
            forward(host, host == null)

            parameter(LemmyApi::getCommunity.getQueryParameter(0), name)
        }.body()
    }

    override suspend fun getPost(instance: String, id: Int, host: String?): GetPostResponse {
        val endpoint = LemmyApi::getPost.getEndpoint()

        val url = urlSubstitutor.buildUrl(instance, endpoint)

        return client.get(url) {
            forward(host, host == null)

            parameter(LemmyApi::getPost.getQueryParameter(0), id)
        }.body()
    }

    @Suppress("MagicNumber")
    override suspend fun getUser(
        instance: String,
        username: String,
        sort: SortType?,
        page: Int?,
        limit: Int?,
        host: String?
    ): GetPersonDetailsResponse {
        val endpoint = LemmyApi::getUser.getEndpoint()

        val url = urlSubstitutor.buildUrl(instance, endpoint)

        return client.get(url) {
            forward(host, host == null)

            parameter(LemmyApi::getUser.getQueryParameter(0), username)
            parameter(LemmyApi::getUser.getQueryParameter(1), sort?.value)
            parameter(LemmyApi::getUser.getQueryParameter(2), page)
            parameter(LemmyApi::getUser.getQueryParameter(3), limit)
        }.body()
    }

    @Suppress("MagicNumber")
    override suspend fun getComments(
        instance: String,
        postId: Int?,
        parentId: Int?,
        sort: SortType?,
        page: Int?,
        limit: Int?,
        host: String?
    ): GetCommentsResponse {
        val endpoint = LemmyApi::getComments.getEndpoint()

        val url = urlSubstitutor.buildUrl(instance, endpoint)

        return client.get(url) {
            forward(host, host == null)

            parameter(LemmyApi::getComments.getQueryParameter(0), postId)
            parameter(LemmyApi::getComments.getQueryParameter(1), parentId)
            parameter(LemmyApi::getComments.getQueryParameter(2), sort?.value)
            parameter(LemmyApi::getComments.getQueryParameter(3), page)
            parameter(LemmyApi::getComments.getQueryParameter(4), limit)

            // TODO: Remove hardcoded parameters
            parameter("max_depth", LemmyApi.MAX_DEPTH)
            parameter("limit", LemmyApi.LIMIT)
        }.body()
    }

    @Suppress("MagicNumber")
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
        val endpoint = LemmyApi::search.getEndpoint()

        val url = urlSubstitutor.buildUrl(instance, endpoint)

        return client.get(url) {
            forward(host, host == null)

            parameter(LemmyApi::search.getQueryParameter(0), query)
            parameter(LemmyApi::search.getQueryParameter(1), type.value)
            parameter(LemmyApi::search.getQueryParameter(2), communityName)
            parameter(LemmyApi::search.getQueryParameter(3), sort?.value)
            parameter(LemmyApi::search.getQueryParameter(4), page)
            parameter(LemmyApi::search.getQueryParameter(5), limit)
        }.body()
    }
}

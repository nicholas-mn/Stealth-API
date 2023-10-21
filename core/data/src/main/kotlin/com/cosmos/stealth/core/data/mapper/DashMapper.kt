package com.cosmos.stealth.core.data.mapper

import com.cosmos.stealth.core.common.data.mapper.Mapper
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.extension.nullIfEmpty
import com.cosmos.stealth.core.data.model.dash.AdaptationSet
import com.cosmos.stealth.core.data.model.dash.MPD
import com.cosmos.stealth.core.data.model.dash.Representation
import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class DashMapper(
    @Named(DEFAULT_DISPATCHER_QUALIFIER) defaultDispatcher: CoroutineDispatcher
) : Mapper<MPD, String, Media?>(defaultDispatcher) {

    override suspend fun toEntity(from: MPD, context: String?): Media? {

        val source = from.period.sets.firstOrNull()?.getMainRepresentation() ?: return null
        val mediaSource = source.toMediaSource(context)

        val resolutions = mutableListOf<MediaSource>()
        val alternatives = mutableListOf<Media>()

        from.period.sets.mapIndexed { index, adaptationSet ->
            when (index) {
                0 -> {
                    adaptationSet.representations
                        .map { it.toMediaSource(context) }
                        .let { resolutions.addAll(it) }
                }
                else -> adaptationSet.toMedia(context)?.let { alternatives.add(it) }
            }
        }

        return Media(source.mimeType, mediaSource, null, resolutions.nullIfEmpty(), alternatives.nullIfEmpty())
    }

    private fun AdaptationSet.toMedia(context: String?): Media? {
        val source = getMainRepresentation() ?: return null
        val resolutions = representations.map { it.toMediaSource(context) }

        return Media(source.mimeType, source.toMediaSource(context), null, resolutions.nullIfEmpty())
    }

    private fun AdaptationSet.getMainRepresentation(): Representation? {
        return representations.lastOrNull()
    }

    private fun Representation.toMediaSource(context: String?): MediaSource {
        return MediaSource(baseUrl.toUrl(context), width?.toIntOrNull(), height?.toIntOrNull())
    }

    private fun String.toUrl(context: String?): String {
        return "$context/$this"
    }
}

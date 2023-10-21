package com.cosmos.stealth.services.reddit.util


private const val REDDIT_SOUNDTRACK_NAME: String = "DASH_audio"
private val REDDIT_VIDEO_REGEX = Regex("DASH_(\\d+)")

fun joinSubredditList(subreddits: List<String>): String {
    return subreddits.joinToString("+")
}

fun getRedditSoundTrack(link: String): String {
    return link.replace(REDDIT_VIDEO_REGEX, REDDIT_SOUNDTRACK_NAME)
}

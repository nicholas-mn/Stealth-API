package com.cosmos.stealth.core.common.data

data class Config(val reddit: Reddit) {
    data class Reddit(val useOauth: Boolean)
}

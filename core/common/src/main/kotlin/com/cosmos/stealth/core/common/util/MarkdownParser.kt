package com.cosmos.stealth.core.common.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

class MarkdownParser(
    private val flavor: MarkdownFlavourDescriptor = CommonMarkFlavourDescriptor(),
    private val parser: MarkdownParser = MarkdownParser(flavor),
    private val defaultDispatcher: CoroutineDispatcher
) {

    suspend fun parse(text: String): String = withContext(defaultDispatcher) {
        val tree = parser.buildMarkdownTreeFromString(text)
        HtmlGenerator(text, tree, flavor).generateHtml()
    }
}

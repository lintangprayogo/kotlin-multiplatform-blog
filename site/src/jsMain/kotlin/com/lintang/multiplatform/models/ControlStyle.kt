package com.lintang.multiplatform.models

sealed class ControlStyle(val style: String) {
    data class Bold(val selectedText: String?) :
        ControlStyle(style = "<strong>$selectedText</strong>")

    data class Italic(val selectedText: String?) : ControlStyle(style = "<em>$selectedText</em>")
    data class Link(
        val selectedText: String?=null,
        val website: String,
        val title: String
    ) :
        ControlStyle(style = "<a href=\'$website\' title=\'$title\'>$selectedText</a>")

    data class Title(val selectedText: String?) :
        ControlStyle(style = "<h1><strong>$selectedText</strong></h1>")

    data class SubTitle(val selectedText: String?) :
        ControlStyle(style = "<h3>$selectedText</h3>")

    data class Quote(val selectedText: String?) : ControlStyle(
        style = "<div style=\"background-color:#FAFAFA;padding:12px;border-radius:6px;\"><em>❞ $selectedText</em></div>"
    )

    data class Code(val selectedText: String?) : ControlStyle(
        style = "<div style=\"background-color:#0d1117;padding:12px;border-radius:6px;\"><pre><code class=\"language-kotlin\"> $selectedText </code></pre></div>"
    )

    data class Image(
        val imageUrl: String,
        val selectedText: String?,
        val description: String
    ) : ControlStyle(
        style = "<img src=\"$imageUrl\" alt=\"$description\" style=\"max-width: 100%\">$selectedText</img>"
    )
    data class Break(val selectedText: String?) : ControlStyle(
        style = "$selectedText<br>"
    )
}
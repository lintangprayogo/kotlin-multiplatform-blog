package com.lintang.multiplatform.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.Screen
import com.lintang.multiplatform.models.ControlStyle
import com.lintang.multiplatform.models.EditorControl
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

@Composable
fun isUserLoggedIn(content: @Composable () -> Unit) {
    val context = rememberPageContext()
    val remembered = remember { localStorage["remember"].toBoolean() }
    val userId = remember { localStorage["userId"] }
    var userIdExists by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        userIdExists = if (!userId.isNullOrEmpty()) checkUserId(id = userId) else false
        if (!remembered || !userIdExists) {
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    }
    println("$userIdExists $remembered")
    if (remembered && userIdExists) {
        content()
    } else {
        println("Loading....")
    }
}

fun Modifier.noBorder(): Modifier {
    return this.border(0.px, style = LineStyle.None, color = Colors.Transparent)
        .outline(0.px, style = LineStyle.None, color = Colors.Transparent)
}

fun getEditor() = document.getElementById(Id.editor) as HTMLTextAreaElement

private fun getSelectedIntRange(): IntRange? {
    val editor = getEditor()

    val start = editor.selectionStart
    val end = editor.selectionEnd
    return if (start != null && end != null) {
        IntRange(start, end - 1)
    } else {
        null
    }
}

fun getSelectedText(): String? {
    val range = getSelectedIntRange()
    return if (range != null) {
        getEditor().value.substring(range.first, range.last)
    } else {
        null
    }
}

fun applyStyle(controlStyle: ControlStyle) {
    val selectedText = getSelectedText()
    val intRange = getSelectedIntRange()
    if (intRange != null && selectedText != null) {
        getEditor().value =
            getEditor().value.replaceRange(range = intRange, replacement = controlStyle.style)
        document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
    }
}

fun applyControlStyle(control: EditorControl, onLinkClick: () -> Unit, onImageClick: () -> Unit) {
    when (control) {

        EditorControl.Bold -> {
            applyStyle(
                ControlStyle.Bold(
                    selectedText = getSelectedText()
                )
            )
        }

        EditorControl.Italic -> {
            applyStyle(ControlStyle.Italic(selectedText = getSelectedText()))
        }

        EditorControl.Link -> {
            onLinkClick()
        }

        EditorControl.SubTitle -> {
            applyStyle(ControlStyle.SubTitle(selectedText = getSelectedText()))
        }

        EditorControl.Title -> {
            applyStyle(ControlStyle.Title(selectedText = getSelectedText()))
        }

        EditorControl.Quote -> {
            applyStyle(ControlStyle.Quote(selectedText = getSelectedText()))
        }

        EditorControl.Code -> {
            applyStyle(ControlStyle.Code(selectedText = getSelectedText()))
        }
        EditorControl.Image -> {
            onImageClick()
        }
    }
}

fun Long.parseDateString() = Date(this).toLocaleDateString()

fun logout() {
    localStorage["remember"] = false.toString()
    localStorage["userId"] = ""
    localStorage["username"] = ""
}
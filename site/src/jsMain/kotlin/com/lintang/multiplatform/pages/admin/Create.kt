package com.lintang.multiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.multiplatform.Screen
import com.lintang.multiplatform.components.AdminPageLayout
import com.lintang.multiplatform.components.LinkPopup
import com.lintang.multiplatform.components.MessagePopup
import com.lintang.multiplatform.models.Category
import com.lintang.multiplatform.models.ControlStyle
import com.lintang.multiplatform.models.EditorControl
import com.lintang.multiplatform.models.Post
import com.lintang.multiplatform.models.Theme
import com.lintang.multiplatform.pages.admin.create_components.CategoryDropdown
import com.lintang.multiplatform.pages.admin.create_components.CreateButton
import com.lintang.multiplatform.pages.admin.create_components.EditorControls
import com.lintang.multiplatform.pages.admin.create_components.Editors
import com.lintang.multiplatform.pages.admin.create_components.TagPost
import com.lintang.multiplatform.pages.admin.create_components.ThumbnailUploader
import com.lintang.multiplatform.util.Constants.FONT_FAMILY
import com.lintang.multiplatform.util.Constants.SIDE_PANEL_WIDTH
import com.lintang.multiplatform.util.Id.editor
import com.lintang.multiplatform.util.Id.subtitleInput
import com.lintang.multiplatform.util.Id.thumbnailInput
import com.lintang.multiplatform.util.Id.titleInput
import com.lintang.multiplatform.util.addPost
import com.lintang.multiplatform.util.applyStyle
import com.lintang.multiplatform.util.getSelectedText
import com.lintang.multiplatform.util.isUserLoggedIn
import com.lintang.multiplatform.util.noBorder
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import kotlin.js.Date

data class CreateUiState(
    var id: String = "",
    var title: String = "",
    var subTitle: String = "",
    var thumbnail: String = "",
    var isEditorVisible: Boolean = true,
    var copyPastedSwitch: Boolean = true,
    var content: String = "",
    var category: Category = Category.Technology,
    var isPopular: Boolean = false,
    var isMain: Boolean = false,
    var isSponsored: Boolean = false,
    val isShowMessagePopup: Boolean = false,
    val isShowLinkPopup: Boolean = false,
    val isShowImagePopup: Boolean = false,
)

@Page
@Composable
fun CreatePage() {
    isUserLoggedIn {
        CreateScreen()
    }
}

@Composable
private fun CreateScreen() {
    val breakpoint = rememberBreakpoint()
    var uiState by remember { mutableStateOf(CreateUiState()) }
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()

    AdminPageLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .margin(topBottom = 50.px)
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .maxWidth(700.px),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleGrid(numColumns = numColumns(base = 1, sm = 3)) {
                    TagPost(modifier = Modifier
                        .margin(
                            right = 24.px,
                            bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                        ),
                        title = "Popular",
                        checked = uiState.isPopular,
                        onCheck = { uiState = uiState.copy(isPopular = it) })
                    TagPost(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        title = "Main",
                        checked = uiState.isMain,
                        onCheck = { uiState = uiState.copy(isMain = it) })
                    TagPost(
                        title = "Sponsor",
                        checked = uiState.isSponsored,
                        onCheck = { uiState = uiState.copy(isSponsored = it) })
                }

                Input(
                    attrs = Modifier.fillMaxWidth()
                        .id(titleInput)
                        .height(54.px)
                        .margin(topBottom = 12.px)
                        .padding(leftRight = 24.px)
                        .background(Theme.LightGray.rgb)
                        .borderRadius(4.px)
                        .noBorder()
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .toAttrs {
                            attr("placeholder", "Title")
                        },
                    type = InputType.Text,
                )
                Input(
                    attrs = Modifier.fillMaxWidth()
                        .id(subtitleInput)
                        .height(54.px)
                        .margin(bottom = 12.px)
                        .padding(leftRight = 24.px)
                        .background(Theme.LightGray.rgb)
                        .borderRadius(4.px)
                        .noBorder()
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .toAttrs {
                            attr("placeholder", "Subtitle")
                        },
                    type = InputType.Text,
                )

                CategoryDropdown(
                    selectedCategory = uiState.category,
                    onCategorySelect = {
                        uiState = uiState.copy(category = it)
                    })

                Row(
                    modifier = Modifier.margin(topBottom = 12.px).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        modifier = Modifier.margin(right = 8.px),
                        checked = uiState.copyPastedSwitch,
                        onCheckedChange = {
                            uiState = uiState.copy(copyPastedSwitch = it, thumbnail = "")
                        },
                        size = SwitchSize.MD
                    )
                    SpanText(
                        modifier = Modifier
                            .fontFamily(FONT_FAMILY)
                            .fontSize(14.px)
                            .color(Theme.HalfBlack.rgb),
                        text = "Paste an Image URL Instead",
                    )
                }

                ThumbnailUploader(
                    thumbnail = uiState.thumbnail,
                    thumbnailInputDisabled = !uiState.copyPastedSwitch,
                    onThumbnailSelect = { thumbnail, file ->
                        (document.getElementById(thumbnailInput) as HTMLInputElement).value =
                            thumbnail
                        uiState = uiState.copy(thumbnail = file)
                    })

                EditorControls(
                    breakpoint = breakpoint,
                    onEditorVisibilityChange = {
                        uiState = uiState.copy(isEditorVisible = it)
                    },
                    editorsVisibile = uiState.isEditorVisible,
                    onLinkClick = {
                        uiState = uiState.copy(isShowLinkPopup = true)
                    },
                    onImageClick = {
                        uiState = uiState.copy(isShowImagePopup = true)
                    }
                )

                Editors(uiState.isEditorVisible)

                CreateButton(
                    onClick = {
                        uiState = uiState.copy(
                            title = (document.getElementById(titleInput) as HTMLInputElement).value,
                            subTitle = (document.getElementById(subtitleInput) as HTMLInputElement).value,
                            content = (document.getElementById(editor) as HTMLTextAreaElement).value,
                        )

                        if (uiState.copyPastedSwitch) {
                            uiState =
                                uiState.copy(thumbnail = (document.getElementById(thumbnailInput) as HTMLInputElement).value)
                        }

                        if (uiState.title.isNotEmpty()
                            && uiState.subTitle.isNotEmpty()
                            && uiState.thumbnail.isNotEmpty()
                            && uiState.content.isNotEmpty()
                        ) {
                            scope.launch {
                                val result = addPost(
                                    Post(
                                        author = localStorage["username"] ?: "",
                                        thumbnail = uiState.thumbnail,
                                        subtitle = uiState.subTitle,
                                        title = uiState.title,
                                        content = uiState.content,
                                        category = uiState.category,
                                        isSponsored = uiState.isSponsored,
                                        isMain = uiState.isMain,
                                        isPopular = uiState.isPopular,
                                        date = Date.now().toLong(),
                                    )
                                )
                                if (result) {
                                    context.router.navigateTo(Screen.Success.route)
                                } else {
                                    println("failed")
                                }
                            }

                        } else {
                            scope.launch {
                                uiState = uiState.copy(isShowMessagePopup = true)
                                delay(5000)
                                uiState = uiState.copy(isShowMessagePopup = false)
                            }


                        }
                    })
            }
        }
    }
    if (uiState.isShowMessagePopup) {
        MessagePopup(
            message = "Please fill out all field",
            onMessageDismiss = {
                uiState = uiState.copy(isShowMessagePopup = false)
            })
    }

    if (uiState.isShowLinkPopup) {
        LinkPopup(onAddClick = { title, website ->
            applyStyle(
                ControlStyle.Link(
                    selectedText = getSelectedText(),
                    title = title,
                    website = website
                )
            )

            uiState = uiState.copy(isShowLinkPopup = false)
        }, onLinkDismiss = {
            uiState = uiState.copy(isShowLinkPopup = false)
        }, editorControl = EditorControl.Link
        )
    }

    if (uiState.isShowImagePopup) {
        LinkPopup(
            onAddClick = { des, imageUrl ->
                applyStyle(
                    ControlStyle.Image(
                        selectedText = getSelectedText(),
                        description = des,
                        imageUrl = imageUrl
                    )
                )
                uiState = uiState.copy(isShowImagePopup = false)
            }, onLinkDismiss = {
                uiState = uiState.copy(isShowImagePopup = false)
            }, editorControl = EditorControl.Image
        )
    }


}



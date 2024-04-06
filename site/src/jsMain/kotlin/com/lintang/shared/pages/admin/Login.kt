package com.lintang.shared.pages.admin


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.lintang.shared.Screen
import com.lintang.shared.style.loginInputStyle
import com.lintang.shared.models.Theme
import com.lintang.shared.models.User
import com.lintang.shared.models.UserWithoutPassword
import com.lintang.shared.util.Constants
import com.lintang.shared.util.Id
import com.lintang.shared.util.Res
import com.lintang.shared.util.checkIfUserExist
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.set

@Page
@Composable
fun LoginScreen() {
    var errorText by remember { mutableStateOf(" ") }
    val scope = rememberCoroutineScope()
    val page = rememberPageContext()

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(leftRight = 50.px, top = 80.px, bottom = 24.px)
                .background(Theme.LightGray.rgb),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .margin(bottom = 50.px).width(100.px),
                src = Res.Image.logo,
                description = "Logo Image"
            )

            Input(
                type = InputType.Text,
                attrs = loginInputStyle.toModifier().margin(bottom = 12.px)
                    .id(Id.usernameInput)
                    .width(350.px)
                    .height(54.px)
                    .padding(leftRight = 20.px)
                    .background(Colors.White)
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(14.px)
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(14.px)
                    .outline(
                        width = 0.px,
                        style = LineStyle.None,
                        color = Colors.Transparent
                    )
                    .toAttrs {
                        attr("placeholder", "Username")
                    }
            )
            Input(
                type = InputType.Password,
                attrs = loginInputStyle.toModifier().margin(bottom = 12.px)
                    .id(Id.passwordInput)
                    .width(350.px)
                    .height(54.px)
                    .padding(leftRight = 20.px)
                    .background(Colors.White)
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(14.px)
                    .outline(
                        width = 0.px,
                        style = LineStyle.None,
                        color = Colors.Transparent
                    )
                    .toAttrs {
                        attr("placeholder", "Password")
                    }
            )

            Button(
                attrs = Modifier
                    .margin(bottom = 24.px)
                    .width(350.px)
                    .height(54.px)
                    .background(Theme.Primary.rgb)
                    .color(Colors.White)
                    .borderRadius(r = 4.px)
                    .fontWeight(FontWeight.Medium)
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(14.px)
                    .border(
                        width = 0.px,
                        style = LineStyle.None,
                        color = Colors.Transparent
                    )
                    .outline(
                        width = 0.px,
                        style = LineStyle.None,
                        color = Colors.Transparent
                    ).cursor(Cursor.Pointer)
                    .onClick {
                        scope.launch {
                            val userName =
                                (document.getElementById(Id.usernameInput) as HTMLInputElement).value
                            val password =
                                (document.getElementById(Id.passwordInput) as HTMLInputElement).value
                            if (userName.isNotEmpty() && password.isNotEmpty()) {
                                val user =
                                    checkIfUserExist(User(username = userName, password = password))
                                if (user != null) {
                                    rememberLoggedIn(remember = true, userWithoutPassword = user)
                                    page.router.navigateTo(Screen.AdminHome.route)
                                } else {
                                    errorText = "Users Not Found !!!"
                                    delay(3000)
                                    errorText = ""
                                }
                            } else {
                                errorText = "Input Fields are Empty"
                                delay(3000)
                                errorText = ""
                            }
                        }

                    }
                    .toAttrs()
            ) { SpanText("Sign In") }
            SpanText(
                modifier = Modifier.width(350.px).color(Colors.Red)
                    .height(54.px).textAlign(textAlign = TextAlign.Center),
                text = errorText
            )

        }
    }
}

private fun rememberLoggedIn(remember: Boolean, userWithoutPassword: UserWithoutPassword? = null) {
    localStorage["remember"] = remember.toString()
    if (userWithoutPassword != null) {
        localStorage["userId"] = userWithoutPassword._id
        localStorage["username"] = userWithoutPassword.username

    }
}
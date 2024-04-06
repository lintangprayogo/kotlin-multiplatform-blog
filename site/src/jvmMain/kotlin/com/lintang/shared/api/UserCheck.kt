package com.lintang.shared.api

import com.lintang.shared.data.MongoDB
import com.lintang.shared.models.User
import com.lintang.shared.models.UserWithoutPassword
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

@Api(routeOverride = "usercheck")
suspend fun userCheck(context: ApiContext) {
    try {
        val userRequest =
            context.req.body?.decodeToString()?.let {
                Json.decodeFromString<User>(it) }
        val user = userRequest?.let {
            context.data.getValue<MongoDB>()
                .checkIfUserExist(User(username = it.username, password = hashPassword(it.password)))
        }
        if (user != null) {
            context.res.setBodyText(
                Json.encodeToString(
                    UserWithoutPassword(
                        _id = user._id,
                        username = user.username
                    )
                )
            )
        } else {
            context.res.setBodyText(Json.encodeToString(Exception("User Not Found !!!")))
        }
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}


private fun hashPassword(password:String):String{
    val messageDigest  = MessageDigest.getInstance("SHA-256")
    val hashBytes  = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
    val hexString = StringBuffer()

    for (byte in hashBytes){
        hexString.append(String.format("%02x",byte))
    }

    return hexString.toString()
}
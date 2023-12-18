package com.saddict.testkost.products.model.remote

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonProperty

@Keep
data class User(
    val password: String,
    val username: String
)

@Keep
data class UserResponse(
    @JsonProperty("user")
    val user: String,
    @JsonProperty("token")
    val token: String,
    @JsonProperty("created")
    val created: Boolean
)

@Keep
data class RegisterUser(
    val username: String,
    val email: String,
    val password: String
)

@Keep
data class RegisterUserResponse(
    @JsonProperty("user")
    val user: UserRegisterResponse,
    @JsonProperty("token")
    val token: String,
)

@Keep
data class UserRegisterResponse(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("username")
    val username: String
)

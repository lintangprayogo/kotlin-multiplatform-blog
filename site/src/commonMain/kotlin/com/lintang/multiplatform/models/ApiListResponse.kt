package com.lintang.multiplatform.models

expect sealed class ApiListResponse {
    object Idle : ApiListResponse
    class Error : ApiListResponse
    class Success
}


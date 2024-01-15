package com.lintang.multiplatform.models

expect sealed class ApiListResponse{
    object Idle
    class Error
    class Success
}
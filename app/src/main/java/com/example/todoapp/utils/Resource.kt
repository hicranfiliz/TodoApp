package com.example.todoapp.utils

sealed class Resource<T>(val status: Status, val date: T? = null, val message: String? = null) {

    class Success<T>(date: T?): Resource<T>(Status.SUCCESS, date)
    class Error<T>(message: String?, date: T? = null): Resource<T>(Status.ERROR, date, message)
    class Loading<T> : Resource<T>(Status.LOADING)
}

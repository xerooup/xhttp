package io.github.xerooup.xhttp

import io.github.xerooup.xhttp.request.Method
import io.github.xerooup.xhttp.request.Request

// entry point
object XHttp {
    fun get(url: String): Request = Request(Method.GET, url)
    fun post(url: String): Request = Request(Method.POST, url)
    fun put(url: String): Request = Request(Method.PUT, url)
    fun delete(url: String): Request = Request(Method.DELETE, url)
}
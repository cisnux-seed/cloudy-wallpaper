package com.cisnux.wallpaper_app

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source

open class BaseTest {
    protected val mockWebServer = MockWebServer()

    fun mockResponse(fileName: String) {
        val inputStream = javaClass.classLoader?.getResourceAsStream(fileName)
        val buffer = inputStream?.source()?.buffer()

        buffer?.let {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(it.readString(Charsets.UTF_8))
            )
        }
    }
}
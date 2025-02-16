package com.soa.ebay.client

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler

class RestTemplateErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse) = response.statusCode.is4xxClientError

    override fun handleError(response: ClientHttpResponse) {
        throw WebException(response.body.readAllBytes().toString(), response.statusCode.value())
    }

    class WebException(
        override val message: String,
        val status: Int,
    ) : RuntimeException(message)
}
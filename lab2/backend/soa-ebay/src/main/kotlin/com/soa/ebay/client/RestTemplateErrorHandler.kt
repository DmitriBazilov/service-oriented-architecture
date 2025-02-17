package com.soa.ebay.client

import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import java.nio.charset.StandardCharsets


class RestTemplateErrorHandler : ResponseErrorHandler {
    override fun hasError(response: ClientHttpResponse) = response.statusCode.is4xxClientError || response.statusCode.is5xxServerError

    override fun handleError(response: ClientHttpResponse) {
        throw WebException(String(response.body.readAllBytes(), StandardCharsets.UTF_8), response.statusCode.value())
    }

    class WebException(
        override val message: String,
        val status: Int,
    ) : RuntimeException(message)
}
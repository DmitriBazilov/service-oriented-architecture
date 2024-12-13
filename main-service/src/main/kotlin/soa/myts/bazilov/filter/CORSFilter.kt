package com.dmitri.backend.filter

import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider
import java.io.IOException

@Provider
class CORSFilter : ContainerResponseFilter {
    @Throws(IOException::class)
    override fun filter(
        requestContext: ContainerRequestContext,
        responseContext: ContainerResponseContext
    ) {
        responseContext.headers.add(
            "Access-Control-Allow-Origin", "*"
        )
        responseContext.headers.add(
            "Access-Control-Allow-Credentials", "true"
        )
        responseContext.headers.add(
            "Access-Control-Allow-Headers",
            "origin, content-type, accept, authorization"
        )
        responseContext.headers.add(
            "Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH"
        )
    }
}
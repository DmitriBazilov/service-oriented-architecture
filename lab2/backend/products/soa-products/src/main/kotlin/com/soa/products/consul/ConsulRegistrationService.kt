package com.soa.products.consul

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory

@Service
class ConsulRegistrationService(
    private val restTemplate: RestTemplate,
    @Value("\${service.id}")
    private val serviceId: String,
) {

    private val consulUrl = "http://consul-server:8500"

    @PostConstruct
    fun registerService() {
        val serviceName = "products"
        val servicePort = 1616

        val registrationJson = """
            {
                "ID": "$serviceId",
                "Name": "$serviceName",
                "Address": "$serviceId",
                "Port": $servicePort,
                "Check": {
                    "HTTP": "http://$serviceId:$servicePort/actuator/health",
                    "Interval": "1s"
                }
            }
        """.trimIndent()

        val uriBuilder = DefaultUriBuilderFactory(consulUrl)
        val uri = uriBuilder.expand("/v1/agent/service/register?replace-existing-checks").toString()

        try {
            restTemplate.put(uri, registrationJson)
            println("Service registered successfully.")
        } catch (ex: HttpClientErrorException) {
            println("Failed to register service: ${ex.message}")
        } catch (ex: HttpServerErrorException) {
            println("Server error: ${ex.message}")
        } catch (ex: Exception) {
            println("Exception while registering service: ${ex.message}")
        }
    }
}

package com.soa.ebay.configuration

import com.orbitz.consul.AgentClient
import com.orbitz.consul.Consul
import com.orbitz.consul.model.health.Service
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class ConsulRetriever {
    val crudAddresses: List<String>
        get() {
            val client: Consul = Consul.builder().withUrl("http://consul:8500").build()
            val agentClient: AgentClient = client.agentClient()
            val services: Map<String, Service> = agentClient.services
            return services.values.stream().filter { x: Service -> x.tags.contains("products") }
                .map { service: Service -> service.address + ":" + service.port }.collect(
                    Collectors.toList()
                )
        }
}
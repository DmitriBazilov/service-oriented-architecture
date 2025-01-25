package com.soa.ebay

import com.soa.ebay.configuration.RibbonClientConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.ribbon.RibbonClients

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClients(defaultConfiguration = [RibbonClientConfig::class])
class SoaEbayApplication

fun main(args: Array<String>) {
	runApplication<SoaEbayApplication>(*args)
}

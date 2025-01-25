package com.soa.ebay.configuration

import com.netflix.client.config.CommonClientConfigKey
import com.netflix.client.config.DefaultClientConfigImpl.DEFAULT_CONNECT_TIMEOUT
import com.netflix.client.config.DefaultClientConfigImpl.DEFAULT_READ_TIMEOUT
import com.netflix.client.config.IClientConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.ribbon.RibbonClientName
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import javax.net.ssl.SSLContext


@Configuration
class RibbonConfiguration {
    @RibbonClientName
    private val name = "grammy-service"

    @Bean
    @ConditionalOnMissingBean
    fun ribbonClientConfig(): IClientConfig {
        val config = RibbonClientConfig()
        config.loadProperties(this.name)
        config.set<Int>(CommonClientConfigKey.ConnectTimeout, DEFAULT_CONNECT_TIMEOUT)
        config.set<Int>(CommonClientConfigKey.ReadTimeout, DEFAULT_READ_TIMEOUT)
        config.set(CommonClientConfigKey.GZipPayload, false)
        config.set(CommonClientConfigKey.ServerListRefreshInterval, 5000)
        config.set(CommonClientConfigKey.NIWSServerListClassName, ConsulServerList::class.java.name)
        return config
    }

//    @LoadBalanced
//    @Bean
//    fun restTemplate(): RestTemplate {
//        try {
//            val sslContext: SSLContext = SSLContextBuilder()
//                .loadTrustMaterial { chain, authType -> true }.build()
//            val socketFactory: SSLConnectionSocketFactory =
//                SSLConnectionSocketFactory(sslContext)
//            val httpClient: HttpClient = HttpClients.custom()
//                .setSSLSocketFactory(socketFactory).build()
//            val factory =
//                HttpComponentsClientHttpRequestFactory(httpClient)
//            factory.setConnectTimeout(6000)
//            factory.setConnectionRequestTimeout(6000)
//            return RestTemplate(factory)
//        } catch (e: Exception) {
//            throw RuntimeException()
//        }
//    }
}
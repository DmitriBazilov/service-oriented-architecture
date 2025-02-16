package com.soa.ebay.configuration

import com.soa.ebay.client.RestTemplateErrorHandler
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.io.HttpClientConnectionManager
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.ssl.SSLContextBuilder
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestTemplate
import javax.net.ssl.SSLContext

@Configuration
class ApplicationConfiguration {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
            cors { disable() }
            csrf { disable() }
        }

        return http.build()
    }

    @Bean
    fun errorHandler(): RestTemplateErrorHandler = RestTemplateErrorHandler()

    @LoadBalanced
    @Bean
    fun restTemplate(
        @Value("\${trust.store}") trustStore: Resource,
        @Value("\${trust.store.password}") trustStorePassword: String,
        errorHandler: RestTemplateErrorHandler
    ): RestTemplate? {
        val sslContext: SSLContext = SSLContextBuilder()
            .loadTrustMaterial { chain, authType -> true }.build()

        val sslConFactory = SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE)

        val cm: HttpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(sslConFactory)
            .build()

        val httpClient: CloseableHttpClient = HttpClients.custom().setConnectionManager(cm).build()
        val requestFactory: ClientHttpRequestFactory =
            HttpComponentsClientHttpRequestFactory(httpClient)

        return RestTemplate(requestFactory).apply {
            this.errorHandler = errorHandler
        }
    }

    @Bean
    fun disableSniHostCheck(): WebServerFactoryCustomizer<JettyServletWebServerFactory>? {
        return WebServerFactoryCustomizer<JettyServletWebServerFactory> { factory: JettyServletWebServerFactory ->
            factory.addServerCustomizers(
                JettyServerCustomizer { server: Server ->
                    for (connector in server.connectors) {
                        if (connector is ServerConnector) {
                            val connectionFactory: HttpConnectionFactory = connector
                                .getConnectionFactory(HttpConnectionFactory::class.java)
                            val secureRequestCustomizer: SecureRequestCustomizer =
                                connectionFactory.httpConfiguration
                                    .getCustomizer(SecureRequestCustomizer::class.java)
                            secureRequestCustomizer.isSniHostCheck = false
                        }
                    }
                })
        }
    }
}

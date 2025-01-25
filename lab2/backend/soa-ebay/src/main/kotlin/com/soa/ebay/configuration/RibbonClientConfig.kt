package com.soa.ebay.configuration

import com.netflix.client.config.DefaultClientConfigImpl

class RibbonClientConfig : DefaultClientConfigImpl() {
    override fun getDefaultSeverListClass(): String = ConsulServerList::class.java.name
}
package com.soa.ebay.configuration

import com.netflix.loadbalancer.Server
import com.netflix.loadbalancer.ServerList
import java.util.stream.Collectors


class ConsulServerList : ServerList<Server?> {
    private val consulRetriever = ConsulRetriever()

    fun prepareListOfServers(): List<Server> {
        return consulRetriever.crudAddresses.map { Server(it) }.toList()
    }

    override fun getInitialListOfServers(): List<Server> = prepareListOfServers()
    override fun getUpdatedListOfServers(): List<Server> = prepareListOfServers()
}
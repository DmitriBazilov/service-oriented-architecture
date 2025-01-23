package com.soa.products.ejb.service

import com.soa.products.ejb.service.command.CreatePersonCommand
import jakarta.ejb.Remote

@Remote
interface PersonService {
    fun create(createPersonCommand: CreatePersonCommand): String
}

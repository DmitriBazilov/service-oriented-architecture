package com.soa.products.controller

import com.soa.products.domain.BestGroupDto
import com.soa.products.domain.toDomain
import com.soa.products.domain.toDto
import com.soa.products.ejb.service.BestGroupService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["/best-group"],
    produces = ["application/xml"],
    consumes = ["application/xml"]
)
class BestGroupController(
    private val bestGroupService: BestGroupService,
) {

    @PostMapping
    fun saveBestGroup(@RequestBody bestGroup: BestGroupDto): ResponseEntity<*> {
        return bestGroupService.saveBestGroup(bestGroup.toDomain()).let {
            ResponseEntity.ok(it.toDto())
        }
    }
}
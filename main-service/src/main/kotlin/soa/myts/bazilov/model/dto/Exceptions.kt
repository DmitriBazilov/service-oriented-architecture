package soa.myts.bazilov.model.dto

import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status
import soa.myts.bazilov.model.dto.Response as ResponseDto

class WebException(response: ResponseDto, status : Status) : WebApplicationException(
    Response.status(status)
        .entity(response).type(MediaType.APPLICATION_XML).build()
)
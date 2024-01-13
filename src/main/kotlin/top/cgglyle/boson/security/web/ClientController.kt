package top.cgglyle.boson.security.web

import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import top.cgglyle.boson.security.domain.command.CreateClientCommand
import top.cgglyle.boson.security.service.ClientService

@RestController
@RequestMapping("client")
class ClientController(
    val clientService: ClientService
) {

    @Transactional
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createClient(@RequestBody command: CreateClientCommand) {
       clientService.save(command)
    }
}
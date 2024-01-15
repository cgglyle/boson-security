package top.cgglyle.boson.security.web

import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
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
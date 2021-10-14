package br.com.zup.apivacina.controller

import br.com.zup.apivacina.controller.dto.UsuarioRequest
import br.com.zup.apivacina.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api/usuario")
class CadastroUsuarioController(
        private val repository: UsuarioRepository,
        private val transaction: TransactionTemplate
) {

    @PostMapping
    fun cadastrar(@RequestBody @Valid request: UsuarioRequest): ResponseEntity<Any> {
        return ResponseEntity.created(
            request.toUsuario().run {
                transaction.execute {
                    repository.save(this)
                }!!.let {
                    return@run UriComponentsBuilder
                            .fromPath("api/usuario/{id}")
                            .buildAndExpand(it.id)
                            .toUri()
                }
            }
        ).build()
    }
}
package br.com.zup.apivacina.controller

import br.com.zup.apivacina.controller.dto.AplicacaoVacinaRequest
import br.com.zup.apivacina.repository.AplicacaoVacinaRepository
import br.com.zup.apivacina.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/api/vacina")
class CadastroVacinaController(
        private val usuarioRepository: UsuarioRepository,
        private val aplicacaoVacinaRepository: AplicacaoVacinaRepository,
        private val transaction: TransactionTemplate
) {

    @PostMapping
    fun cadastrar(@RequestBody @Valid request: AplicacaoVacinaRequest): ResponseEntity<Any> {
        return ResponseEntity.created(
                request.toAplicacaoVacina(usuarioRepository).run {
                    transaction.execute {
                        aplicacaoVacinaRepository.save(this)
                    }!!.let {
                        return@run UriComponentsBuilder
                                .fromPath("api/vacina/{id}")
                                .buildAndExpand(it.id)
                                .toUri()
                    }
                }
        ).build()
    }
}


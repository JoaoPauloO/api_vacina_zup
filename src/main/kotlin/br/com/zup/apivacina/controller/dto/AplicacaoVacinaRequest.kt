package br.com.zup.apivacina.controller.dto

import br.com.zup.apivacina.model.AplicacaoVacina
import br.com.zup.apivacina.model.Usuario
import br.com.zup.apivacina.repository.UsuarioRepository
import br.com.zup.apivacina.shared.util.NomeVacina
import br.com.zup.apivacina.shared.validator.ExistValue
import br.com.zup.apivacina.shared.validator.ValidVacina
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class AplicacaoVacinaRequest(
        @field:NotBlank
        @field:ValidVacina
        val nomeVacina: String,

        @field:Email
        @field:NotBlank
        @field:ExistValue(className = Usuario::class, field = "email")
        val emailUsuario: String
) {

    fun toAplicacaoVacina(repository: UsuarioRepository): AplicacaoVacina {
        return repository.findByEmail(emailUsuario).get().let { usuario ->
            return@let AplicacaoVacina(
                    nomeVacina = NomeVacina.valueOf(nomeVacina.uppercase()),
                    usuario = usuario
            )
        }

    }
}


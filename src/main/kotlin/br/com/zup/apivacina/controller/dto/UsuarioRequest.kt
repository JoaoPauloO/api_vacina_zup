package br.com.zup.apivacina.controller.dto

import br.com.zup.apivacina.model.Usuario
import br.com.zup.apivacina.shared.validator.UniqueValue
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class UsuarioRequest(
        @field:NotBlank
        val nome: String,

        @field:Email
        @field:NotBlank
        @field:UniqueValue(className = Usuario::class, field = "email")
        val email: String,

        @field:CPF
        @field:UniqueValue(className = Usuario::class, field = "cpf")
        val cpf: String,

        @field:NotNull
        val dataNascimento: LocalDate
) {

    fun toUsuario(): Usuario {
        return Usuario(
                nome = nome,
                email = email,
                cpf = cpf,
                dataNascimento = dataNascimento
        )
    }
}

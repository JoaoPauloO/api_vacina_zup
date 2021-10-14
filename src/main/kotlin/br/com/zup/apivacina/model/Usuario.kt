package br.com.zup.apivacina.model

import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.GenerationType.SEQUENCE
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
class Usuario(
        @field:NotBlank
        @Column(nullable = false)
        val nome: String,

        @field:NotBlank
        @field:Email
        @Column(nullable = false, unique = true)
        val email: String,

        @field:CPF
        @Column(nullable = false, unique = true)
        private val cpf: String,

        @Column(nullable = false)
        val dataNascimento: LocalDate
) {

    @Id @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null
}

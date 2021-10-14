package br.com.zup.apivacina.model

import br.com.zup.apivacina.shared.util.NomeVacina
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.EnumType.STRING
import javax.persistence.GenerationType.IDENTITY
import javax.validation.constraints.NotNull

@Entity
class AplicacaoVacina(
        @field:NotNull
        @field:Enumerated(STRING)
        @Column(nullable = false)
        val nomeVacina: NomeVacina,

        @field:NotNull
        @field:ManyToOne
        val usuario: Usuario
) {

    @Id @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    val dataAplicacaoVacina: LocalDateTime = LocalDateTime.now()
}

package br.com.zup.apivacina.repository

import br.com.zup.apivacina.model.AplicacaoVacina
import org.springframework.data.jpa.repository.JpaRepository

interface AplicacaoVacinaRepository : JpaRepository<AplicacaoVacina, Long> {
}
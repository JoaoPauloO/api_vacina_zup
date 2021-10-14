package br.com.zup.apivacina

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(enableDefaultTransactions = false)
class ApiVacinaApplication

fun main(args: Array<String>) {
	runApplication<ApiVacinaApplication>(*args)
}

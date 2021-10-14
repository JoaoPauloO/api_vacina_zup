package br.com.zup.apivacina.controller

import br.com.zup.apivacina.model.Usuario
import br.com.zup.apivacina.repository.UsuarioRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDate


@SpringBootTest
@AutoConfigureMockMvc
internal class CadastroUsuarioControllerTest(
        @Autowired private val mockMvc: MockMvc,
        @Autowired private val repository: UsuarioRepository,
        @Autowired private val transaction: TransactionTemplate
) {

    private val usuario = Usuario(
            nome = "Giovana",
            email = "gio@gio.com",
            cpf = "825.991.120-52",
            dataNascimento = LocalDate.of(2001, 8, 13)
    )

    private val request = object {
        var nome = "Joao"
        var email = "mail@mail.com"
        var cpf = "688.659.160-60"
        var dataNascimento = "1996-11-07"
    }

    @BeforeEach
    fun setup() {
        transaction.execute {
            repository.deleteAll()
        }
    }

    @Test
    fun `deve cadastrar um usuario`() {
        mockMvc.perform(
                post("/api/usuario")
                        .contentType(APPLICATION_JSON)
                        .content(
                                ObjectMapper().writeValueAsString(request)
                        )
        )
                .andExpect(status().isCreated)
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("api/usuario/{\\d}"))
    }

    @Test
    fun `nao deve cadastrar um usuario com campos ilegais`() {
        request.nome = ""
        request.email = "mailmail.com"
        request.cpf = "633.159.190-77"
        request.dataNascimento = "1996-11-07"

        mockMvc.perform(
                post("/api/usuario")
                        .contentType(APPLICATION_JSON)
                        .content(
                                ObjectMapper().writeValueAsString(request)
                        )
        )
                .andExpect(status().isBadRequest)
                .andDo(print())
    }

    @Test
    fun `nao deve cadastar um usuario ja existente com o mesmo email`() {
        transaction.execute {
            repository.save(usuario)
        }

        val requestGi = object {
            var nome = "Giovana"
            var email = "gio@gio.com"
            var cpf = "688.659.160-60"
            var dataNascimento = "2001-8-13"
        }

        mockMvc.perform(
                post("/api/usuario")
                        .contentType(APPLICATION_JSON)
                        .content(
                                ObjectMapper().writeValueAsString(requestGi)
                        )
        )
                .andExpect(status().isBadRequest)
                .andDo(print())
    }

    @Test
    fun `nao deve cadastar um usuario ja existente com o mesmo cpf`() {
        transaction.execute {
            repository.save(usuario)
        }

        val requestGi = object {
            var nome = "Giovana"
            var email = "gio@gio.com"
            var cpf = "825.991.120-52"
            var dataNascimento = "2001-8-13"
        }

        mockMvc.perform(
                post("/api/usuario")
                        .contentType(APPLICATION_JSON)
                        .content(
                                ObjectMapper().writeValueAsString(requestGi)
                        )
        )
                .andExpect(status().isBadRequest)
                .andDo(print())
    }
}
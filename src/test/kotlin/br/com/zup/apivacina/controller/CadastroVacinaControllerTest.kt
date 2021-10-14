package br.com.zup.apivacina.controller

import br.com.zup.apivacina.model.Usuario
import br.com.zup.apivacina.repository.AplicacaoVacinaRepository
import br.com.zup.apivacina.repository.UsuarioRepository
import br.com.zup.apivacina.shared.util.NomeVacina
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.support.TransactionTemplate
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
internal class CadastroVacinaControllerTest(
        @Autowired private val mockMvc: MockMvc,
        @Autowired private val aplicacaoVacinaRepository: AplicacaoVacinaRepository,
        @Autowired private val usuarioRepository: UsuarioRepository,
        @Autowired private val transaction: TransactionTemplate
) {

    private val request = object {
        var nomeVacina = "CORONAVAC";
        var emailUsuario = "gio@gio.com";
    }

    private val usuario = Usuario(
            nome = "Giovana",
            email = "gio@gio.com",
            cpf = "825.991.120-52",
            dataNascimento = LocalDate.of(2001, 8, 13)
    )

    @BeforeEach
    fun setup() {
        transaction.execute {
            aplicacaoVacinaRepository.deleteAll()
        }
    }

    @Test
    fun `deve cadastrar uma aplicacao de vacina`() {
        transaction.execute {
            usuarioRepository.save(usuario)
        }

        mockMvc.perform(
                post("/api/vacina")
                        .contentType(APPLICATION_JSON)
                        .content(
                                ObjectMapper().writeValueAsString(request)
                        )
        )
                .andExpect(status().isCreated)
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("api/vacina/{\\d}"))
    }

    @Test
    fun `nao deve cadastrar uma aplicacao de vacina com email de usuario inexistente`() {
        transaction.execute {
            usuarioRepository.deleteAll()
        }

        mockMvc.perform(
                post("/api/vacina")
                        .contentType(APPLICATION_JSON)
                        .content(
                                ObjectMapper().writeValueAsString(request)
                        )
        )
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `nao deve cadastrar uma aplicacao de vacina com vacina inexistente`() {
        request.nomeVacina = "CORONAVAQUINHA"

        mockMvc.perform(
                post("/api/vacina")
                        .contentType(APPLICATION_JSON)
                        .content(
                                ObjectMapper().writeValueAsString(request)
                        )
        )
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `deve retornar true caso a enum exista`() {
        NomeVacina.valueOf("CORONAVAC").validar().run {
            assertTrue(this)
        }

        NomeVacina.valueOf("PFIZER").validar().run {
            assertTrue(this)
        }

        NomeVacina.valueOf("JANSSEN").validar().run {
            assertTrue(this)
        }

        NomeVacina.valueOf("ASTRAZENECA").validar().run {
            assertTrue(this)
        }
    }

    @Test
    fun `deve retornar falso caso a enum no exista`() {
        assertThrows(IllegalArgumentException::class.java, Executable {
            NomeVacina.valueOf("").validar()
        })
    }
}
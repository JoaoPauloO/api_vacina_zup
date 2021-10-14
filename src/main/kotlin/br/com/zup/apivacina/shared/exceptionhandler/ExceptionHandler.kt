package br.com.zup.apivacina.shared.exceptionhandler

import br.com.zup.apivacina.shared.exceptionhandler.dto.ErrorResponse
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler(
        private val messageSource: MessageSource
) {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(exception: MethodArgumentNotValidException): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body(
            mutableListOf<ErrorResponse>().apply {
                exception.bindingResult.fieldErrors.let{ erro ->
                    erro.forEach{
                        add(ErrorResponse(it.field, messageSource.getMessage(it, LocaleContextHolder.getLocale())))
                    }
                }
            }
        )
    }
}
package br.com.zup.apivacina.shared.validator

import br.com.zup.apivacina.shared.util.NomeVacina
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [ValidVacinaValidator::class])
@MustBeDocumented
annotation class ValidVacina(
        val message: String = "Essa vacina não existe. Tente CORONAVAC, PFIZER, JANSSEN, ASTRAZENECA.",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class ValidVacinaValidator() : ConstraintValidator<ValidVacina, String> {

    override fun isValid(value: String, constraint: ConstraintValidatorContext?): Boolean {
        return try {
            NomeVacina.valueOf(value.uppercase()).validar()
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
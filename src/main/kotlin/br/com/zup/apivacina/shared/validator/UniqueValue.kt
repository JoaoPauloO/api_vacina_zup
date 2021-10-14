package br.com.zup.apivacina.shared.validator

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = [UniqueValueValidator::class])
@MustBeDocumented
annotation class UniqueValue(
        val className: KClass<*>,
        val field: String,
        val message: String = "Esse campo já existe. Tente um diferente.",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class UniqueValueValidator() : ConstraintValidator<UniqueValue, Any> {

    private lateinit var field: String
    private lateinit var className: KClass<*>
    private lateinit var message: String
    @PersistenceContext private lateinit var manager: EntityManager

    override fun initialize(constraintAnnotation: UniqueValue) {
        field = constraintAnnotation.field
        className = constraintAnnotation.className
    }

    override fun isValid(value: Any?, consttraint: ConstraintValidatorContext): Boolean {
        message = "Este $field ja existe. Tente um diferente"
        consttraint.disableDefaultConstraintViolation()
        consttraint.buildConstraintViolationWithTemplate(message).addConstraintViolation()

        return manager.createQuery("SELECT 1 FROM ${className.qualifiedName} WHERE $field = :value").let { query ->
            query.setParameter("value", value)
            return@let query.resultList.isEmpty()
        }
    }
}

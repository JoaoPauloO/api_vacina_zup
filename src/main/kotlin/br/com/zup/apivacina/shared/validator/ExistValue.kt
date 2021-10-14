package br.com.zup.apivacina.shared.validator

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [ExistValueValidator::class])
@MustBeDocumented
annotation class ExistValue(
        val className: KClass<*>,
        val field: String,
        val message: String = "Esse campo não existe.",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)

class ExistValueValidator : ConstraintValidator<ExistValue, Any> {

    private lateinit var field: String
    private lateinit var className: KClass<*>
    private lateinit var message: String
    @PersistenceContext private lateinit var manager: EntityManager

    override fun initialize(constraintAnnotation: ExistValue) {
        field = constraintAnnotation.field
        className = constraintAnnotation.className
    }

    override fun isValid(value: Any?, consttraint: ConstraintValidatorContext): Boolean {
        message = "Este $field não existe."
        consttraint.disableDefaultConstraintViolation()
        consttraint.buildConstraintViolationWithTemplate(message).addConstraintViolation()

        return manager.createQuery("SELECT 1 FROM ${className.qualifiedName} WHERE $field = :value").let { query ->
            query.setParameter("value", value)
            return@let query.resultList.isNotEmpty()
        }
    }

}



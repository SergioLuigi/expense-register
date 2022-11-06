package br.com.sergioluigi.expenseregister.modules.exception

import br.com.sergioluigi.expenseregister.model.dto.CustomExceptionResponseDTO
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class CustomExceptionHandler: ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {

        val cause = ex.cause
        val messages = HashMap<String, String?>()

        when(cause) {
            is JsonParseException -> {
                messages["error"] = cause.message
            }
            is MismatchedInputException -> {
                if (cause.path != null && cause.path.size > 0) {
                    cause.path.forEach {
                        messages[it.fieldName] = cause.message
                    }
                }
            }
            is JsonMappingException -> {
                if (cause.path != null && cause.path.size > 0) {
                    cause.path.forEach {
                        messages[it.fieldName] = it.description
                    }
                }
            }
        }

        val responseBody = CustomExceptionResponseDTO(
            status = status.value(),
            error = HttpStatus.valueOf(status.value()).name,
            messages = listOf(messages),
            path = (request as ServletWebRequest).request.requestURI
        )

        return ResponseEntity(responseBody, headers, status)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {

        val responseBody = CustomExceptionResponseDTO(
            status = status.value(),
            error = HttpStatus.BAD_REQUEST.name,
            messages = ex.bindingResult.allErrors.map {
                if(it is FieldError){
                    mapOf(it.field to it.defaultMessage)
                }else{
                    mapOf(it.objectName to it.defaultMessage)
                }
            },
            path = (request as ServletWebRequest).request.requestURI
        )

        return ResponseEntity(responseBody, headers, status)
    }

}
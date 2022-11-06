package br.com.sergioluigi.expenseregister.model.dto

import java.time.LocalDateTime

class CustomExceptionResponseDTO(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val messages: Collection<Map<String,String?>>,
    val path: String,
)
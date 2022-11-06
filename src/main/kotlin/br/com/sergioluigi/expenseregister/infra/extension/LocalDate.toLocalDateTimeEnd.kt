package br.com.sergioluigi.expenseregister.infra.extension

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun LocalDate.toLocalDateTimeEnd() = LocalDateTime.of(this, LocalTime.MIDNIGHT.minusSeconds(1))
package br.com.sergioluigi.expenseregister

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration
class DesafioExactaApplication

fun main(args: Array<String>) {
	runApplication<DesafioExactaApplication>(*args)
}

package br.com.sergioluigi.expenseregister.infra.extension


import mu.KLogger
import reactor.core.publisher.Mono

fun <T: Any, K: KLogger> Mono<T>.logCreation(k: K): Mono<T> =
    doOnNext { k.info("DATA CREATED = $it") }
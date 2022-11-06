package br.com.sergioluigi.expenseregister.infra.extension

import reactor.core.publisher.Mono

fun <T: Any, R: Any> Mono<T>.zipWhenToMono(func:(T) -> R) =
    zipWhen{
        Mono.just(func(it))
    }
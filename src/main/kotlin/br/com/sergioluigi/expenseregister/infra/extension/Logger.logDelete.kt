package br.com.sergioluigi.expenseregister.infra.extension

import mu.KLogger
import reactor.core.publisher.Mono
import javax.persistence.Table
import kotlin.reflect.KClass

fun <T: Any, K: KLogger,ID: Any, R: KClass<*>> Mono<T>.logDelete(k: K, id: ID, r: R): Mono<T> =
    doOnNext { k.info("DATA ID DELETED = $id, TABLE = ${(r.annotations.find { it is Table } as? Table)?.name ?: r.simpleName?.plus(" (Class name)")}") }
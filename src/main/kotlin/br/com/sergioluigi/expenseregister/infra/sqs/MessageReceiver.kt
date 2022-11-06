package br.com.sergioluigi.expenseregister.infra.sqs


abstract class MessageReceiver<T> {

    abstract fun execute(obj: T)
    
}
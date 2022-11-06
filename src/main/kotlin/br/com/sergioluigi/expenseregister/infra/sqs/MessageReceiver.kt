package br.com.sergioluigi.expenseregister.infra.sqs

import io.awspring.cloud.messaging.listener.Acknowledgment


abstract class MessageReceiver<T> {

    abstract fun execute(obj: T)
    
}
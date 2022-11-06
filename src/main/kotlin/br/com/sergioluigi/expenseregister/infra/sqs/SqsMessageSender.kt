package br.com.sergioluigi.expenseregister.infra.sqs

import io.awspring.cloud.messaging.core.QueueMessagingTemplate
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder

open class SqsMessageSender<T: Any>(
    private val messagingTemplate: QueueMessagingTemplate,
){
    fun send(queueUrl: String, obj: T){
        messagingTemplate.convertAndSend(queueUrl, obj)
    }

    fun send(queueUrl: String, message: Message<T>){
        messagingTemplate.convertAndSend(queueUrl, message)
    }

}
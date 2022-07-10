package com.winor30.ktor.plugins.cloud.pubsub

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.Base64

/**
 * PubSubMessage is class of Cloud Pub/Sub's message for push subscription
 * @param message The message data.
 * @param T Type Arguments for Decode Destination
 */
data class PubsubMessage<T>(val message: Message) {

  companion object {
    val mapper = jacksonObjectMapper()
  }

  /**
   * Message is field of Cloud Pub/Sub's message for push subscription
   * @param messageId message id
   * @param publishTime publish time
   * @param data the main data published by Publisher. It is also in Base64-encoded format.
   */
  data class Message(val messageId: String, val publishTime: String, val data: String)

  /**
   * Decode and get messages published by the publisher
   * @return the decoded message
   */
  inline fun <reified R : T> toDecodedData(): R {
    val decoded = String(Base64.getDecoder().decode(this.message.data))
    return mapper.readValue(decoded, R::class.java)
  }
}


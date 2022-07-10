package com.winor30.ktor.plugins.cloud.pubsub

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.Base64

data class PubsubMessage<T>(val message: Message) {

  companion object {
    val mapper = jacksonObjectMapper()
  }

  data class Message(val messageId: String, val publishTime: String, val data: String)

  inline fun <reified R : T> toDecodedData(): R {
    val decoded = String(Base64.getDecoder().decode(this.message.data))
    return mapper.readValue(decoded, R::class.java)
  }
}


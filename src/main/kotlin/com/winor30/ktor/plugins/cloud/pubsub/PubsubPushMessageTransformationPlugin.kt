package com.winor30.ktor.plugins.cloud.pubsub

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.serialization.Configuration
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.BaseRouteScopedPlugin
import io.ktor.server.request.ApplicationReceivePipeline
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import java.util.Base64

class PubsubPushMessageTransformationPlugin<T : Any> :
  BaseRouteScopedPlugin<Configuration, PubsubPushMessageTransformationPlugin<T>> {

  companion object {
    val mapper = jacksonObjectMapper()
  }

  override val key: AttributeKey<PubsubPushMessageTransformationPlugin<T>> =
    AttributeKey("PubsubPushMessageTransformationPlugin")

  override fun install(
    pipeline: ApplicationCallPipeline,
    configure: Configuration.() -> Unit
  ): PubsubPushMessageTransformationPlugin<T> {
    val plugin = PubsubPushMessageTransformationPlugin<T>()
    pipeline.receivePipeline.intercept(ApplicationReceivePipeline.Before) { data ->
      if (data !is ByteReadChannel) {
        proceedWith(data)
        return@intercept
      }

      val reqBodyString = data.readUTF8Line()
      if (reqBodyString == null) {
        proceedWith(data)
        return@intercept
      }

      val encodedPubsubMessage = mapper.readValue<PubsubMessage<T>>(reqBodyString)
      proceedWith(encodedPubsubMessage)

    }
    return plugin
  }
}


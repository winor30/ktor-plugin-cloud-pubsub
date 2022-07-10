# Ktor Plugin Cloud Pub/Sub
====

A ktor plugin that Cloud Pub/Sub's push trigger messages into a defined type

## Description
Cloud-Pubsub has both Pull and Push subscriptions: Pull subscriptions use a library provided by GCP to retrieve published messages, while Push subscriptions use a REST API to send message to the subscriber via the REST API.

The message format for Push subscriptions is Base 64 encoded data as shown in [Push subscriptions / Receive messages]().
Therefore, Base64 decoding is required on the subscriber side.
In this library, when using Cloud Pub/Sub with [Ktor](https://jp.ktor.work/), the mapping to Cloud Pub/Sub format messages at the time of Push subscription and Base64 decoding to an arbitrary type is done by provided as a ktor plugin.

## Requirement

- ktor 2.0.0 or higher

## Usage

```kotlin
import com.winor30.ktor.plugins.cloud.pubsub.PubsubPushMessageTransformationPlugin
import com.winor30.ktor.plugins.cloud.pubsub.PubsubMessage

...

fun Application.module() {
  routing {
    route("/post") {
      // use ktor plugin with RouteScopedPlugin
      install(PubsubPushMessageTransformationPlugin<DecodedNFTGetActionParameter>())
      post {
        val param = call.receive<PubsubMessage<DecodedPublishedMessage>>()
        println(param)

        val decoded: DecodedPublishedMessage = param.toDecodedData()
        println(decoded)
      }
    }
  }
}

// type of messages published by Cloud Pub/Sub
data class DecodedPublishedMessage(
  val name: String,
  val userId: String,
)

```

## Install

```groovy
dependencies {
    implementation 'com.winor30:ktor-plugin-cloud-pubsub:0.0.2'
}
```

## Contribution

Please feel free to Contribute!

## Licence

[MIT License](./LICENSE)

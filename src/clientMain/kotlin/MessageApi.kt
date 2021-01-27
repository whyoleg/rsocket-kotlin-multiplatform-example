import io.rsocket.kotlin.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.*
import kotlinx.serialization.protobuf.*

@OptIn(ExperimentalSerializationApi::class)
actual class MessageApi(private val rSocket: RSocket, private val proto: ProtoBuf) {
    actual suspend fun send(chatId: Int, content: String): Message = proto.decodeFromPayload(
        rSocket.requestResponse(
            proto.encodeToPayload(route = "messages.send", SendMessage(chatId, content))
        )
    )

    actual suspend fun history(chatId: Int, limit: Int): List<Message> = proto.decodeFromPayload(
        rSocket.requestResponse(
            proto.encodeToPayload(route = "messages.history", HistoryMessages(chatId, limit))
        )
    )

    actual fun messages(chatId: Int, fromMessageId: Int): Flow<Message> = rSocket.requestStream(
        proto.encodeToPayload(route = "messages.stream", StreamMessages(chatId, fromMessageId))
    ).map {
        proto.decodeFromPayload(it)
    }
}

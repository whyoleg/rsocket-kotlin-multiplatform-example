import io.ktor.utils.io.core.*
import io.rsocket.kotlin.*
import kotlinx.serialization.*
import kotlinx.serialization.protobuf.*

@OptIn(ExperimentalSerializationApi::class)
actual class ChatApi(private val rSocket: RSocket, private val proto: ProtoBuf) {
    actual suspend fun all(): List<Chat> = proto.decodeFromPayload(
        rSocket.requestResponse(Payload(route = "chats.all", ByteReadPacket.Empty))
    )

    actual suspend fun new(name: String): Chat = proto.decodeFromPayload(
        rSocket.requestResponse(
            proto.encodeToPayload(route = "chats.new", NewChat(name))
        )
    )

    actual suspend fun delete(id: Int) {
        rSocket.requestResponse(
            proto.encodeToPayload(route = "chats.delete", DeleteChat(id))
        ).release()
    }
}

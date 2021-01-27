import io.ktor.utils.io.core.*
import io.rsocket.kotlin.*
import kotlinx.serialization.*
import kotlinx.serialization.protobuf.*

@OptIn(ExperimentalSerializationApi::class)
actual class UserApi(private val rSocket: RSocket, private val proto: ProtoBuf) {

    actual suspend fun getMe(): User = proto.decodeFromPayload(
        rSocket.requestResponse(Payload(route = "users.getMe", ByteReadPacket.Empty))
    )

    actual suspend fun deleteMe() {
        rSocket.fireAndForget(Payload(route = "users.deleteMe", ByteReadPacket.Empty))
    }

    actual suspend fun all(): List<User> = proto.decodeFromPayload(
        rSocket.requestResponse(Payload(route = "users.all", ByteReadPacket.Empty))
    )
}

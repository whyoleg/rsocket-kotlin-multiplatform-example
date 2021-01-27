import io.ktor.utils.io.core.*
import io.rsocket.kotlin.metadata.*
import io.rsocket.kotlin.payload.*

fun Payload(route: String, packet: ByteReadPacket): Payload = buildPayload {
    data(packet)
    metadata(RoutingMetadata(route))
}

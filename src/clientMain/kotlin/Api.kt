import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.rsocket.kotlin.*
import io.rsocket.kotlin.core.*
import io.rsocket.kotlin.payload.*
import io.rsocket.kotlin.transport.ktor.*
import io.rsocket.kotlin.transport.ktor.client.*

class Api(rSocket: RSocket) {
    private val proto = ConfiguredProtoBuf
    val users = UserApi(rSocket, proto)
    val chats = ChatApi(rSocket, proto)
    val messages = MessageApi(rSocket, proto)
}

suspend fun connectToApiUsingWS(name: String): Api {
    val client = HttpClient {
        install(WebSockets)
        install(RSocketSupport) {
            connector = connector(name)
        }
    }

    return Api(client.rSocket(port = 9000))
}

@OptIn(InternalAPI::class)
suspend fun connectToApiUsingTCP(name: String): Api {
    val transport = aSocket(SelectorManager()).tcp().clientTransport("0.0.0.0", 8000)
    return Api(connector(name).connect(transport))
}

private fun connector(name: String): RSocketConnector = RSocketConnector {
    connectionConfig {
        setupPayload { buildPayload { data(name) } }
    }
}

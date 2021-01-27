import kotlinx.coroutines.flow.*

suspend fun main() {
    val api = connectToApiUsingTCP("Hanna")

    api.users.all().forEach {
        println(it)
    }

    val chat = api.chats.all().firstOrNull() ?: api.chats.new("rsocket-kotlin chat")

    val sentMessage = api.messages.send(chat.id, "RSocket is awesome! (from JVM TCP)")
    println(sentMessage)

    api.messages.messages(chat.id, -1).collect {
        println("Received: $it")
    }
}

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main(): Unit = runBlocking {
    val api = connectToApiUsingTCP("Gloria")

    api.users.all().forEach {
        println(it)
    }

    val chat = api.chats.all().firstOrNull() ?: api.chats.new("rsocket-kotlin chat")

    val sentMessage = api.messages.send(chat.id, "RSocket is awesome! (from Native)")
    println(sentMessage)

    api.messages.messages(chat.id, -1).collect {
        println("Received: $it")
    }
}

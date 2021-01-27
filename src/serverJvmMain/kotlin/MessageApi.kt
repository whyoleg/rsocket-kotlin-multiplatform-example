import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
actual class MessageApi(
    private val messages: Messages,
    private val chats: Chats,
) {

    private val listeners = mutableListOf<SendChannel<Message>>()

    actual suspend fun send(chatId: Int, content: String): Message {
        if (chatId !in chats) error("No chat with id '$chatId'")
        val userId = currentSession().userId
        val message = messages.create(userId, chatId, content)
        listeners.forEach { it.send(message) }
        return message
    }

    actual suspend fun history(chatId: Int, limit: Int): List<Message> {
        if (chatId !in chats) error("No chat with id '$chatId'")
        return messages.takeLast(chatId, limit)
    }

    actual fun messages(chatId: Int, fromMessageId: Int): Flow<Message> = flow {
        messages.takeAfter(chatId, fromMessageId).forEach { emit(it) }
        emitAll(channelFlow<Message> {
            listeners += channel
            awaitClose {
                listeners -= channel
            }
        }.buffer())
    }
}


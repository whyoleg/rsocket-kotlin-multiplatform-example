import kotlinx.atomicfu.*
import java.util.concurrent.*

class Chats {
    private val chats: MutableMap<Int, Chat> = ConcurrentHashMap()
    private val chatsId = atomic(0)

    val values: List<Chat> get() = chats.values.toList()

    fun getOrNull(id: Int): Chat? = chats[id]

    fun delete(id: Int) {
        chats -= id
    }

    fun create(name: String): Chat {
        if (chats.values.any { it.name == name }) error("Chat with such name already exists")
        val chatId = chatsId.incrementAndGet()
        val chat = Chat(chatId, name)
        chats[chatId] = chat
        return chat
    }

    fun exists(id: Int): Boolean = id in chats
}

operator fun Chats.get(id: Int): Chat = getOrNull(id) ?: error("No user with id '$id' exists")
operator fun Chats.minusAssign(id: Int): Unit = delete(id)
operator fun Chats.contains(id: Int): Boolean = exists(id)

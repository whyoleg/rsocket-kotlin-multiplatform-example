actual class ChatApi(
    private val chats: Chats,
    private val messages: Messages,
) {

    actual suspend fun all(): List<Chat> = chats.values.toList()

    actual suspend fun new(name: String): Chat = chats.create(name)

    actual suspend fun delete(id: Int) {
        messages.deleteForChat(id)
        chats -= id
    }
}


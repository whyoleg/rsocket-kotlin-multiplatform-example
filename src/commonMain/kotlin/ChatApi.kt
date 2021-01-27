import kotlinx.serialization.*

@Serializable
data class Chat(
    val id: Int,
    val name: String,
)

expect class ChatApi {
    suspend fun all(): List<Chat>
    suspend fun new(name: String): Chat
    suspend fun delete(id: Int)
}

@Serializable
data class NewChat(val name: String)

@Serializable
data class DeleteChat(val id: Int)

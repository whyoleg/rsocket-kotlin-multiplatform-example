import kotlinx.serialization.*

@Serializable
data class User(
    val id: Int,
    val name: String,
)

expect class UserApi {
    suspend fun getMe(): User
    suspend fun deleteMe()
    suspend fun all(): List<User>
}

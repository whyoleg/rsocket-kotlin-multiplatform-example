import kotlinx.atomicfu.*
import java.util.concurrent.*

class Users {
    private val users: MutableMap<Int, User> = ConcurrentHashMap()
    private val usersId = atomic(0)

    val values: List<User> get() = users.values.toList()

    fun getOrCreate(name: String): User =
        users.values.find { it.name == name } ?: run {
            val userId = usersId.incrementAndGet()
            User(userId, name).also { users[userId] = it }
        }

    fun getOrNull(id: Int): User? = users[id]

    fun delete(id: Int) {
        users -= id
    }
}

operator fun Users.get(id: Int): User = getOrNull(id) ?: error("No user with id '$id' exists")
operator fun Users.minusAssign(id: Int): Unit = delete(id)

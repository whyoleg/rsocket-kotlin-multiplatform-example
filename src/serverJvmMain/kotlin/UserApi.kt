actual class UserApi(
    private val users: Users,
) {

    actual suspend fun getMe(): User {
        val userId = currentSession().userId
        return users[userId]
    }

    actual suspend fun deleteMe() {
        val userId = currentSession().userId
        users -= userId
    }

    actual suspend fun all(): List<User> = users.values
}


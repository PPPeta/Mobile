package ru.stroykrep.app.data.repo

import ru.stroykrep.app.data.dao.UserDao
import ru.stroykrep.app.data.entity.User
import ru.stroykrep.app.data.session.SessionManager

sealed class AuthResult {
    data class Success(val userId: Long) : AuthResult()
    data class Failure(val message: String) : AuthResult()
}

class AuthRepository(
    private val userDao: UserDao,
    private val session: SessionManager
) {

    suspend fun register(
        fullName: String,
        phone: String,
        email: String,
        password: String
    ): AuthResult {
        val existing = userDao.findByEmail(email.trim().lowercase())
        if (existing != null) {
            return AuthResult.Failure("Email уже зарегистрирован")
        }
        val id = userDao.insert(
            User(
                fullName = fullName.trim(),
                phone = phone.trim(),
                email = email.trim().lowercase(),
                password = password
            )
        )
        session.setCurrentUserId(id)
        return AuthResult.Success(id)
    }

    suspend fun login(email: String, password: String): AuthResult {
        val user = userDao.login(email.trim().lowercase(), password)
            ?: return AuthResult.Failure("Неверный email или пароль")
        session.setCurrentUserId(user.id)
        return AuthResult.Success(user.id)
    }

    fun logout() = session.logout()

    suspend fun currentUser(): User? {
        val id = session.getCurrentUserId()
        if (id <= 0) return null
        return userDao.findById(id)
    }

    suspend fun updateProfile(user: User) {
        userDao.update(user)
    }

    suspend fun changePassword(
        userId: Long,
        oldPassword: String,
        newPassword: String
    ): AuthResult {
        val user = userDao.findById(userId)
            ?: return AuthResult.Failure("Пользователь не найден")
        if (user.password != oldPassword) {
            return AuthResult.Failure("Неверный текущий пароль")
        }
        userDao.update(user.copy(password = newPassword))
        return AuthResult.Success(userId)
    }
}

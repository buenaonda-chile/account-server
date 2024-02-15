package com.example.accountserver.`interface`.user

import com.example.accountserver.domain.user.User
import com.example.accountserver.domain.user.UserRepository
import com.example.accountserver.error.UserDoesNotExistsException
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class UserService(
    private val userRepository: UserRepository
) {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    suspend fun getUserInfo(userId: Long): UserInfo = userRepository.findById(userId)?.toUserInfo()?: throw UserDoesNotExistsException

    suspend fun getUserInfos(userIds: List<Long>): List<UserInfo> {
        return userRepository.findAllByIdIsIn(userIds).map { it.toUserInfo() }
    }

    suspend fun modifyUserInfo(
        userId: Long,
        request: UserInfoRequest
    ): UserInfo {
        val user = userRepository.findById(userId)?: throw UserDoesNotExistsException

        return userRepository.save(
            user.apply {
                username = request.username?: username
                isActive = request.isActive?: isActive
                isBanned = request.isBanned?: isBanned
            }
        ).toUserInfo()
    }


    private fun User.toUserInfo(): UserInfo = run {
        UserInfo(
            username = username,
            email = email,
            isActive = isActive,
            isBanned = isBanned,
            createdAt = createdAt.format(dateTimeFormatter),
            updatedAt = updatedAt.format(dateTimeFormatter),
        )
    }
}
package com.example.accountserver.`interface`.user

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
class UserInfoController(
    private val userService: UserService
){
    @GetMapping("/{userId}")
    suspend fun getUserInfo(
        @PathVariable userId: Long,
    ): UserInfo = userService.getUserInfo(userId)

    @GetMapping
    suspend fun getUsersInfo(
        @RequestParam("userIds") userIds: List<Long>,
    ): UserInfosResponse = UserInfosResponse(userService.getUserInfos(userIds))

    @PatchMapping("/{userId}")
    suspend fun putUserInfo(
        @PathVariable userId: Long,
        @RequestBody userInfoRequest: UserInfoRequest
    ): UserInfo = userService.modifyUserInfo(userId, userInfoRequest)
}
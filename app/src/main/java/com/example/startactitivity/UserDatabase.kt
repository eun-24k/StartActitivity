package com.example.startactitivity

object UserDatabase {

    var user1: User = User("이름","e", "a")

    var totalUserData: ArrayList<User> = arrayListOf(user1)

    // 회원가입한 사용자를 저장하는 함수
    fun addUser(user: User) {
        totalUserData.add(user)
    }

    // 모든 사용자 정보를 가져오는 함수
    fun getTotalUser(): ArrayList<User> {
        return totalUserData
    }

    // 아이디를 가지고 해당 사용자 정보 가져오는 함수
    fun getUser(id: String): User? {
        return totalUserData.find { it.id == id }
    }

    // 사용자 정보를 수정하는 함수
    fun editUserData(user: User) {
        getUser(user.id)?.let {
            it.name = user.name
            it.password = user.password
        }
    }
}
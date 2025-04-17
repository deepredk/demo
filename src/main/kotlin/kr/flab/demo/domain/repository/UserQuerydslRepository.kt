package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.User
import java.time.LocalDate
import java.time.LocalDateTime

interface UserQuerydslRepository {
    fun findByUsernameContaining(username: String): List<User>
    fun findByEmailDomain(domain: String): List<User>
    fun findByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<User>
    fun findByUsernameWithProfileFetch(username: String): User?
    fun countByEmailDomain(domain: String): Long

    // Dynamic query methods
    fun searchUsers(
        username: String? = null,
        email: String? = null,
        createdAfter: LocalDateTime? = null,
        createdBefore: LocalDateTime? = null
    ): List<User>

    fun findUsersWithProfileCriteria(
        fullName: String? = null,
        birthDateAfter: LocalDate? = null,
        birthDateBefore: LocalDate? = null,
        phoneNumber: String? = null
    ): List<User>
}

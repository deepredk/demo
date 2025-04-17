package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>, UserQuerydslRepository {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile WHERE u.username = :username")
    fun findByUsernameWithProfile(username: String): User?

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile")
    fun findAllWithProfile(): List<User>
}

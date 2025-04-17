package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.UserProfile
import kr.flab.demo.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, Long> {
    fun findByUser(user: User): UserProfile?
}

package kr.flab.demo.domain.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.flab.demo.domain.entity.QUser
import kr.flab.demo.domain.entity.QUserProfile
import kr.flab.demo.domain.entity.User
import kr.flab.demo.domain.entity.UserProfile
import java.time.LocalDate
import java.time.LocalDateTime
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class UserQuerydslRepositoryImpl(
    private val entityManager: EntityManager
) : UserQuerydslRepository {

    private val queryFactory: JPAQueryFactory by lazy { JPAQueryFactory(entityManager) }
    private val user = QUser.user
    private val profile = QUserProfile.userProfile

    override fun findByUsernameContaining(username: String): List<User> {
        return queryFactory
            .selectFrom(user)
            .where(user.username.contains(username))
            .fetch()
    }

    override fun findByEmailDomain(domain: String): List<User> {
        return queryFactory
            .selectFrom(user)
            .where(user.email.endsWith("@$domain"))
            .fetch()
    }

    override fun findByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): List<User> {
        return queryFactory
            .selectFrom(user)
            .where(user.createdAt.between(start, end))
            .fetch()
    }

    override fun findByUsernameWithProfileFetch(username: String): User? {
        return queryFactory
            .selectFrom(user)
            .leftJoin(user.profile, profile).fetchJoin()
            .where(user.username.eq(username))
            .fetchOne()
    }

    override fun countByEmailDomain(domain: String): Long {
        return queryFactory
            .select(user.count())
            .from(user)
            .where(user.email.endsWith("@$domain"))
            .fetchOne() ?: 0L
    }

    override fun searchUsers(
        username: String?,
        email: String?,
        createdAfter: LocalDateTime?,
        createdBefore: LocalDateTime?
    ): List<User> {
        val builder = BooleanBuilder()

        // Dynamically add conditions only if parameters are not null
        username?.let { builder.and(user.username.contains(it)) }
        email?.let { builder.and(user.email.contains(it)) }
        createdAfter?.let { builder.and(user.createdAt.after(it)) }
        createdBefore?.let { builder.and(user.createdAt.before(it)) }

        // If no conditions were added, return all users
        return queryFactory
            .selectFrom(user)
            .where(builder)
            .fetch()
    }

    override fun findUsersWithProfileCriteria(
        fullName: String?,
        birthDateAfter: LocalDate?,
        birthDateBefore: LocalDate?,
        phoneNumber: String?
    ): List<User> {
        val builder = BooleanBuilder()

        // Join with profile is needed only if any profile criteria is specified
        val needsProfileJoin = fullName != null || birthDateAfter != null || 
                              birthDateBefore != null || phoneNumber != null

        // Dynamically add conditions for profile fields if they are not null
        fullName?.let { builder.and(profile.fullName.contains(it)) }
        birthDateAfter?.let { builder.and(profile.birthDate.after(it)) }
        birthDateBefore?.let { builder.and(profile.birthDate.before(it)) }
        phoneNumber?.let { builder.and(profile.phoneNumber.contains(it)) }

        val query = queryFactory.selectFrom(user)

        // Only join with profile if needed
        if (needsProfileJoin) {
            query.join(user.profile, profile)
        }

        return query
            .where(builder)
            .distinct() // Avoid duplicates if joining
            .fetch()
    }
}

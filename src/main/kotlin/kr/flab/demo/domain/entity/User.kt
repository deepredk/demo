package kr.flab.demo.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

enum class AuthProvider {
    LOCAL, GOOGLE, KAKAO, NAVER, APPLE
}

enum class Role {
    USER, ADMIN
}

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 50, unique = true)
    val username: String,

    @Column(nullable = false, length = 100)
    val email: String,

    @Column(length = 100)
    val password: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val provider: AuthProvider = AuthProvider.LOCAL,

    @Column
    val providerId: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    val emailVerified: Boolean = false,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val profile: UserProfile? = null
)

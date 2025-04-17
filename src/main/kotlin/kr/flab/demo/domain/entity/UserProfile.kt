package kr.flab.demo.domain.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "user_profiles")
class UserProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(length = 100)
    val fullName: String,
    
    @Column
    val bio: String? = null,
    
    @Column
    val birthDate: LocalDate? = null,
    
    @Column(length = 20)
    val phoneNumber: String? = null,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)

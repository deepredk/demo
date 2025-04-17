package kr.flab.demo.domain.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "employees")
class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false, length = 100)
    val name: String,
    
    @Column(nullable = false, length = 100, unique = true)
    val email: String,
    
    @Column
    val hireDate: LocalDate = LocalDate.now(),
    
    @Column
    val salary: Double? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    val department: Department
)

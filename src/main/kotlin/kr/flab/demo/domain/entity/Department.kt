package kr.flab.demo.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "departments")
class Department(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false, length = 100)
    val name: String,
    
    @Column(length = 500)
    val description: String? = null,
    
    @OneToMany(mappedBy = "department", cascade = [CascadeType.ALL], orphanRemoval = true)
    val employees: MutableList<Employee> = mutableListOf()
)

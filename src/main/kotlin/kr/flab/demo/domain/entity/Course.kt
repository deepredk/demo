package kr.flab.demo.domain.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "courses")
class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false, length = 100)
    val title: String,
    
    @Column(length = 500)
    val description: String? = null,
    
    @Column
    val credits: Int? = null,
    
    @Column
    val startDate: LocalDate? = null,
    
    @Column
    val endDate: LocalDate? = null,
    
    @ManyToMany(mappedBy = "courses")
    val students: MutableSet<Student> = mutableSetOf()
)

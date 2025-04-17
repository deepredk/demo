package kr.flab.demo.domain.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "students")
class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false, length = 100)
    val name: String,
    
    @Column(nullable = false, length = 100, unique = true)
    val email: String,
    
    @Column
    val enrollmentDate: LocalDate = LocalDate.now(),
    
    @Column(length = 20)
    val studentId: String? = null,
    
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "student_course",
        joinColumns = [JoinColumn(name = "student_id")],
        inverseJoinColumns = [JoinColumn(name = "course_id")]
    )
    val courses: MutableSet<Course> = mutableSetOf()
)

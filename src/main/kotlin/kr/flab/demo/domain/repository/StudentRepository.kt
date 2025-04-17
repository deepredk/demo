package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.Student
import kr.flab.demo.domain.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<Student, Long> {
    fun findByEmail(email: String): Student?
    fun findByStudentId(studentId: String): Student?

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c = :course")
    fun findByCourse(course: Course): List<Student>

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.email = :email")
    fun findByEmailWithCourses(email: String): Student?

    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.studentId = :studentId")
    fun findByStudentIdWithCourses(studentId: String): Student?

    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses")
    fun findAllWithCourses(): List<Student>

    @Query("SELECT DISTINCT s FROM Student s JOIN FETCH s.courses c WHERE c = :course")
    fun findByCourseWithCourses(course: Course): List<Student>
}

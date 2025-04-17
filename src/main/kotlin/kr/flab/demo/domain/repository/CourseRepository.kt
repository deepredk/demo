package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.Course
import kr.flab.demo.domain.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Long> {
    fun findByTitle(title: String): Course?

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s = :student")
    fun findByStudent(student: Student): List<Course>

    @Query("SELECT c FROM Course c WHERE c.startDate <= CURRENT_DATE AND c.endDate >= CURRENT_DATE")
    fun findActiveCourses(): List<Course>

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.title = :title")
    fun findByTitleWithStudents(title: String): Course?

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
    fun findAllWithStudents(): List<Course>

    @Query("SELECT DISTINCT c FROM Course c JOIN FETCH c.students s WHERE s = :student")
    fun findByStudentWithStudents(student: Student): List<Course>

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students WHERE c.startDate <= CURRENT_DATE AND c.endDate >= CURRENT_DATE")
    fun findActiveCoursesWithStudents(): List<Course>
}

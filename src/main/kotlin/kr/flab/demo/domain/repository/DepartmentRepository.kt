package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.Department
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : JpaRepository<Department, Long> {
    fun findByName(name: String): Department?

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.employees WHERE d.name = :name")
    fun findByNameWithEmployees(name: String): Department?

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.employees")
    fun findAllWithEmployees(): List<Department>
}

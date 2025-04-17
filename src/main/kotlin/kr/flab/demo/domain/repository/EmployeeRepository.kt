package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.Employee
import kr.flab.demo.domain.entity.Department
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, Long> {
    fun findByEmail(email: String): Employee?
    fun findByDepartment(department: Department): List<Employee>
}

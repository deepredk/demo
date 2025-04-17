package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDate

@DataJpaTest
class NPlus1IssueTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository

    @Autowired
    private lateinit var departmentRepository: DepartmentRepository

    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @Autowired
    private lateinit var studentRepository: StudentRepository

    @Autowired
    private lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup() {
        // Create users and profiles
        for (i in 1..5) {
            val user = User(
                username = "user$i",
                email = "user$i@example.com"
            )
            userRepository.save(user)

            val profile = UserProfile(
                fullName = "User $i",
                birthDate = LocalDate.of(1990, 1, i),
                phoneNumber = "123-456-789$i",
                user = user
            )
            userProfileRepository.save(profile)
        }

        // Create departments and employees
        for (i in 1..3) {
            val department = Department(
                name = "Department $i",
                description = "Description for Department $i"
            )
            departmentRepository.save(department)

            for (j in 1..3) {
                val employee = Employee(
                    name = "Employee $j of Department $i",
                    email = "employee$j.dept$i@example.com",
                    hireDate = LocalDate.now().minusDays((i * 10 + j).toLong()),
                    salary = 50000.0 + (i * 10000.0) + (j * 1000.0),
                    department = department
                )
                employeeRepository.save(employee)
            }
        }

        // Create students and courses
        val courses = mutableListOf<Course>()
        for (i in 1..4) {
            val course = Course(
                title = "Course $i",
                description = "Description for Course $i",
                credits = 3 + (i % 2),
                startDate = LocalDate.now().minusDays(30),
                endDate = LocalDate.now().plusDays(60)
            )
            courseRepository.save(course)
            courses.add(course)
        }

        for (i in 1..6) {
            val student = Student(
                name = "Student $i",
                email = "student$i@example.com",
                studentId = "S1000$i"
            )
            
            // Assign courses to students
            student.courses.add(courses[0]) // All students take Course 1
            if (i % 2 == 0) student.courses.add(courses[1]) // Even students take Course 2
            if (i % 3 == 0) student.courses.add(courses[2]) // Students divisible by 3 take Course 3
            if (i % 4 == 0) student.courses.add(courses[3]) // Students divisible by 4 take Course 4
            
            studentRepository.save(student)
        }

        // Flush and clear to ensure data is saved
        entityManager.flush()
        entityManager.clear()
    }

    @Test
    fun `test N+1 issue with User and UserProfile`() {
        println("[DEBUG_LOG] === Testing User-UserProfile relationship ===")
        
        // N+1 problem demonstration
        println("[DEBUG_LOG] --- Without fetch join (N+1 problem) ---")
        val users = userRepository.findAll()
        for (user in users) {
            println("[DEBUG_LOG] User: ${user.username}, Profile: ${user.profile?.fullName}")
        }
        
        // Solution with fetch join
        println("[DEBUG_LOG] --- With fetch join (solution) ---")
        val usersWithProfiles = userRepository.findAllWithProfile()
        for (user in usersWithProfiles) {
            println("[DEBUG_LOG] User: ${user.username}, Profile: ${user.profile?.fullName}")
        }
    }

    @Test
    fun `test N+1 issue with Department and Employee`() {
        println("[DEBUG_LOG] === Testing Department-Employee relationship ===")
        
        // N+1 problem demonstration
        println("[DEBUG_LOG] --- Without fetch join (N+1 problem) ---")
        val departments = departmentRepository.findAll()
        for (department in departments) {
            println("[DEBUG_LOG] Department: ${department.name}, Employees: ${department.employees.size}")
        }
        
        // Solution with fetch join
        println("[DEBUG_LOG] --- With fetch join (solution) ---")
        val departmentsWithEmployees = departmentRepository.findAllWithEmployees()
        for (department in departmentsWithEmployees) {
            println("[DEBUG_LOG] Department: ${department.name}, Employees: ${department.employees.size}")
        }
    }

    @Test
    fun `test N+1 issue with Student and Course`() {
        println("[DEBUG_LOG] === Testing Student-Course relationship ===")
        
        // N+1 problem demonstration
        println("[DEBUG_LOG] --- Without fetch join (N+1 problem) ---")
        val students = studentRepository.findAll()
        for (student in students) {
            println("[DEBUG_LOG] Student: ${student.name}, Courses: ${student.courses.size}")
        }
        
        // Solution with fetch join
        println("[DEBUG_LOG] --- With fetch join (solution) ---")
        val studentsWithCourses = studentRepository.findAllWithCourses()
        for (student in studentsWithCourses) {
            println("[DEBUG_LOG] Student: ${student.name}, Courses: ${student.courses.size}")
        }
    }

    @Test
    fun `test N+1 issue with Course and Student`() {
        println("[DEBUG_LOG] === Testing Course-Student relationship ===")
        
        // N+1 problem demonstration
        println("[DEBUG_LOG] --- Without fetch join (N+1 problem) ---")
        val courses = courseRepository.findAll()
        for (course in courses) {
            println("[DEBUG_LOG] Course: ${course.title}, Students: ${course.students.size}")
        }
        
        // Solution with fetch join
        println("[DEBUG_LOG] --- With fetch join (solution) ---")
        val coursesWithStudents = courseRepository.findAllWithStudents()
        for (course in coursesWithStudents) {
            println("[DEBUG_LOG] Course: ${course.title}, Students: ${course.students.size}")
        }
    }
}

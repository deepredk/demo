package kr.flab.demo.domain.repository

import kr.flab.demo.domain.entity.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDate
import java.time.LocalDateTime

@DataJpaTest
class EntityRelationshipTest {

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

    @Test
    fun `test one-to-one relationship between User and UserProfile`() {
        // Create a user
        val user = User(
            username = "johndoe",
            email = "john.doe@example.com"
        )
        userRepository.save(user)

        // Create a profile for the user
        val profile = UserProfile(
            fullName = "John Doe",
            birthDate = LocalDate.of(1990, 1, 15),
            phoneNumber = "123-456-7890",
            user = user
        )
        userProfileRepository.save(profile)

        // Flush and clear to ensure data is saved and retrieved from DB
        entityManager.flush()
        entityManager.clear()

        // Retrieve the user and check if profile is correctly associated
        val retrievedUser = userRepository.findByUsername("johndoe")
        assertThat(retrievedUser).isNotNull
        
        val retrievedProfile = userProfileRepository.findByUser(retrievedUser!!)
        assertThat(retrievedProfile).isNotNull
        assertThat(retrievedProfile?.fullName).isEqualTo("John Doe")
        assertThat(retrievedProfile?.user?.id).isEqualTo(retrievedUser.id)
    }

    @Test
    fun `test one-to-many relationship between Department and Employee`() {
        // Create a department
        val department = Department(
            name = "Engineering",
            description = "Software Engineering Department"
        )
        departmentRepository.save(department)

        // Create employees for the department
        val employee1 = Employee(
            name = "Alice Smith",
            email = "alice.smith@example.com",
            hireDate = LocalDate.of(2020, 3, 15),
            salary = 75000.0,
            department = department
        )
        
        val employee2 = Employee(
            name = "Bob Johnson",
            email = "bob.johnson@example.com",
            hireDate = LocalDate.of(2021, 5, 10),
            salary = 70000.0,
            department = department
        )
        
        employeeRepository.saveAll(listOf(employee1, employee2))

        // Flush and clear to ensure data is saved and retrieved from DB
        entityManager.flush()
        entityManager.clear()

        // Retrieve the department and check if employees are correctly associated
        val retrievedDepartment = departmentRepository.findByName("Engineering")
        assertThat(retrievedDepartment).isNotNull
        
        val employees = employeeRepository.findByDepartment(retrievedDepartment!!)
        assertThat(employees).hasSize(2)
        assertThat(employees.map { it.name }).containsExactlyInAnyOrder("Alice Smith", "Bob Johnson")
    }

    @Test
    fun `test many-to-many relationship between Student and Course`() {
        // Create students
        val student1 = Student(
            name = "Emma Wilson",
            email = "emma.wilson@example.com",
            studentId = "S12345"
        )
        
        val student2 = Student(
            name = "Michael Brown",
            email = "michael.brown@example.com",
            studentId = "S67890"
        )
        
        studentRepository.saveAll(listOf(student1, student2))

        // Create courses
        val course1 = Course(
            title = "Introduction to Programming",
            description = "Basic programming concepts",
            credits = 3,
            startDate = LocalDate.now().minusDays(30),
            endDate = LocalDate.now().plusDays(60)
        )
        
        val course2 = Course(
            title = "Database Systems",
            description = "Relational database design and SQL",
            credits = 4,
            startDate = LocalDate.now().minusDays(15),
            endDate = LocalDate.now().plusDays(75)
        )
        
        courseRepository.saveAll(listOf(course1, course2))

        // Establish relationships
        student1.courses.add(course1)
        student1.courses.add(course2)
        student2.courses.add(course1)
        
        studentRepository.saveAll(listOf(student1, student2))

        // Flush and clear to ensure data is saved and retrieved from DB
        entityManager.flush()
        entityManager.clear()

        // Retrieve students and check if courses are correctly associated
        val retrievedStudent = studentRepository.findByEmail("emma.wilson@example.com")
        assertThat(retrievedStudent).isNotNull
        assertThat(retrievedStudent?.courses).hasSize(2)
        
        // Retrieve courses and check if students are correctly associated
        val retrievedCourse = courseRepository.findByTitle("Introduction to Programming")
        assertThat(retrievedCourse).isNotNull
        
        val studentsInCourse = studentRepository.findByCourse(retrievedCourse!!)
        assertThat(studentsInCourse).hasSize(2)
        assertThat(studentsInCourse.map { it.name }).containsExactlyInAnyOrder("Emma Wilson", "Michael Brown")
    }
}

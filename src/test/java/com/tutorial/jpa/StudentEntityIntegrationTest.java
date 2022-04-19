package com.tutorial.jpa;

import com.tutorial.jpa.domain.Gender;
import com.tutorial.jpa.entity.Student;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class StudentEntityIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(StudentEntityIntegrationTest.class);

    private EntityManagerFactory emf;
    private EntityManager em;

    @Test
    public void persistStudent() {
        persistStudent(createStudent());
        List<Student> students = getAllStudents();
        log.info(students.toString());

        Assertions.assertEquals(1, students.size());
    }

    @BeforeEach
    public void setup() {
        emf = Persistence.createEntityManagerFactory("jpa-tutorial-entity");
        em = emf.createEntityManager();
    }

    @AfterEach
    public void destroy() {
        if (null != em) {
            em.close();
        }
        if (null != emf) {
            emf.close();
        }
    }

    private Student createStudent() {
        Student student = new Student();
        student.setName("Vishnu");
        student.setGender(Gender.MALE);
        student.setAge(28);
        student.setBirthDate(Date.from(LocalDate.of(1993, 10, 01).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return student;
    }

    private List<Student> getAllStudents() {
        String selectQuery = "SELECT student FROM Student student";
        TypedQuery<Student> selectStudentsQuery = em.createQuery(selectQuery, Student.class);
        List<Student> students = selectStudentsQuery.getResultList();
        return students;
    }

    private void persistStudent(Student student) {
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
        em.clear();
    }
}

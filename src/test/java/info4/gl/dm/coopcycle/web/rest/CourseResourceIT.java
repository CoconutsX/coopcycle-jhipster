package info4.gl.dm.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import info4.gl.dm.coopcycle.IntegrationTest;
import info4.gl.dm.coopcycle.domain.Course;
import info4.gl.dm.coopcycle.repository.CourseRepository;
import info4.gl.dm.coopcycle.repository.EntityManager;
import info4.gl.dm.coopcycle.service.dto.CourseDTO;
import info4.gl.dm.coopcycle.service.mapper.CourseMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CourseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CourseResourceIT {

    private static final Long DEFAULT_I_D = 1L;
    private static final Long UPDATED_I_D = 2L;

    private static final Long DEFAULT_I_D_DELIVERY_PERSON = 1L;
    private static final Long UPDATED_I_D_DELIVERY_PERSON = 2L;

    private static final String ENTITY_API_URL = "/api/courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course().iD(DEFAULT_I_D).iDDeliveryPerson(DEFAULT_I_D_DELIVERY_PERSON);
        return course;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course().iD(UPDATED_I_D).iDDeliveryPerson(UPDATED_I_D_DELIVERY_PERSON);
        return course;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Course.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        course = createEntity(em);
    }

    @Test
    void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().collectList().block().size();
        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testCourse.getiDDeliveryPerson()).isEqualTo(DEFAULT_I_D_DELIVERY_PERSON);
    }

    @Test
    void createCourseWithExistingId() throws Exception {
        // Create the Course with an existing ID
        course.setId(1L);
        CourseDTO courseDTO = courseMapper.toDto(course);

        int databaseSizeBeforeCreate = courseRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkiDIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().collectList().block().size();
        // set the field null
        course.setiD(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkiDDeliveryPersonIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().collectList().block().size();
        // set the field null
        course.setiDDeliveryPerson(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCoursesAsStream() {
        // Initialize the database
        courseRepository.save(course).block();

        List<Course> courseList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CourseDTO.class)
            .getResponseBody()
            .map(courseMapper::toEntity)
            .filter(course::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(courseList).isNotNull();
        assertThat(courseList).hasSize(1);
        Course testCourse = courseList.get(0);
        assertThat(testCourse.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testCourse.getiDDeliveryPerson()).isEqualTo(DEFAULT_I_D_DELIVERY_PERSON);
    }

    @Test
    void getAllCourses() {
        // Initialize the database
        courseRepository.save(course).block();

        // Get all the courseList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(course.getId().intValue()))
            .jsonPath("$.[*].iD")
            .value(hasItem(DEFAULT_I_D.intValue()))
            .jsonPath("$.[*].iDDeliveryPerson")
            .value(hasItem(DEFAULT_I_D_DELIVERY_PERSON.intValue()));
    }

    @Test
    void getCourse() {
        // Initialize the database
        courseRepository.save(course).block();

        // Get the course
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, course.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(course.getId().intValue()))
            .jsonPath("$.iD")
            .value(is(DEFAULT_I_D.intValue()))
            .jsonPath("$.iDDeliveryPerson")
            .value(is(DEFAULT_I_D_DELIVERY_PERSON.intValue()));
    }

    @Test
    void getNonExistingCourse() {
        // Get the course
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCourse() throws Exception {
        // Initialize the database
        courseRepository.save(course).block();

        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).block();
        updatedCourse.iD(UPDATED_I_D).iDDeliveryPerson(UPDATED_I_D_DELIVERY_PERSON);
        CourseDTO courseDTO = courseMapper.toDto(updatedCourse);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, courseDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testCourse.getiDDeliveryPerson()).isEqualTo(UPDATED_I_D_DELIVERY_PERSON);
    }

    @Test
    void putNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, courseDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.save(course).block();

        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testCourse.getiDDeliveryPerson()).isEqualTo(DEFAULT_I_D_DELIVERY_PERSON);
    }

    @Test
    void fullUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.save(course).block();

        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse.iD(UPDATED_I_D).iDDeliveryPerson(UPDATED_I_D_DELIVERY_PERSON);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testCourse.getiDDeliveryPerson()).isEqualTo(UPDATED_I_D_DELIVERY_PERSON);
    }

    @Test
    void patchNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, courseDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(courseDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCourse() {
        // Initialize the database
        courseRepository.save(course).block();

        int databaseSizeBeforeDelete = courseRepository.findAll().collectList().block().size();

        // Delete the course
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, course.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

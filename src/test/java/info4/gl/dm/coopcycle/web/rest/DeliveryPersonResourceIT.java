package info4.gl.dm.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import info4.gl.dm.coopcycle.IntegrationTest;
import info4.gl.dm.coopcycle.domain.DeliveryPerson;
import info4.gl.dm.coopcycle.repository.DeliveryPersonRepository;
import info4.gl.dm.coopcycle.repository.EntityManager;
import info4.gl.dm.coopcycle.service.dto.DeliveryPersonDTO;
import info4.gl.dm.coopcycle.service.mapper.DeliveryPersonMapper;
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
 * Integration tests for the {@link DeliveryPersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DeliveryPersonResourceIT {

    private static final Long DEFAULT_I_D = 1L;
    private static final Long UPDATED_I_D = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_VEHICULE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICULE_TYPE = "BBBBBBBBBB";

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;

    private static final String ENTITY_API_URL = "/api/delivery-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @Autowired
    private DeliveryPersonMapper deliveryPersonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DeliveryPerson deliveryPerson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryPerson createEntity(EntityManager em) {
        DeliveryPerson deliveryPerson = new DeliveryPerson()
            .iD(DEFAULT_I_D)
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .phone(DEFAULT_PHONE)
            .vehiculeType(DEFAULT_VEHICULE_TYPE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
        return deliveryPerson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryPerson createUpdatedEntity(EntityManager em) {
        DeliveryPerson deliveryPerson = new DeliveryPerson()
            .iD(UPDATED_I_D)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .phone(UPDATED_PHONE)
            .vehiculeType(UPDATED_VEHICULE_TYPE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        return deliveryPerson;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DeliveryPerson.class).block();
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
        deliveryPerson = createEntity(em);
    }

    @Test
    void createDeliveryPerson() throws Exception {
        int databaseSizeBeforeCreate = deliveryPersonRepository.findAll().collectList().block().size();
        // Create the DeliveryPerson
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryPerson testDeliveryPerson = deliveryPersonList.get(deliveryPersonList.size() - 1);
        assertThat(testDeliveryPerson.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testDeliveryPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeliveryPerson.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testDeliveryPerson.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDeliveryPerson.getVehiculeType()).isEqualTo(DEFAULT_VEHICULE_TYPE);
        assertThat(testDeliveryPerson.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testDeliveryPerson.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    void createDeliveryPersonWithExistingId() throws Exception {
        // Create the DeliveryPerson with an existing ID
        deliveryPerson.setId(1L);
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        int databaseSizeBeforeCreate = deliveryPersonRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkiDIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryPersonRepository.findAll().collectList().block().size();
        // set the field null
        deliveryPerson.setiD(null);

        // Create the DeliveryPerson, which fails.
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryPersonRepository.findAll().collectList().block().size();
        // set the field null
        deliveryPerson.setName(null);

        // Create the DeliveryPerson, which fails.
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryPersonRepository.findAll().collectList().block().size();
        // set the field null
        deliveryPerson.setSurname(null);

        // Create the DeliveryPerson, which fails.
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryPersonRepository.findAll().collectList().block().size();
        // set the field null
        deliveryPerson.setLatitude(null);

        // Create the DeliveryPerson, which fails.
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = deliveryPersonRepository.findAll().collectList().block().size();
        // set the field null
        deliveryPerson.setLongitude(null);

        // Create the DeliveryPerson, which fails.
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllDeliveryPeopleAsStream() {
        // Initialize the database
        deliveryPersonRepository.save(deliveryPerson).block();

        List<DeliveryPerson> deliveryPersonList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DeliveryPersonDTO.class)
            .getResponseBody()
            .map(deliveryPersonMapper::toEntity)
            .filter(deliveryPerson::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(deliveryPersonList).isNotNull();
        assertThat(deliveryPersonList).hasSize(1);
        DeliveryPerson testDeliveryPerson = deliveryPersonList.get(0);
        assertThat(testDeliveryPerson.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testDeliveryPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeliveryPerson.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testDeliveryPerson.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDeliveryPerson.getVehiculeType()).isEqualTo(DEFAULT_VEHICULE_TYPE);
        assertThat(testDeliveryPerson.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testDeliveryPerson.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    void getAllDeliveryPeople() {
        // Initialize the database
        deliveryPersonRepository.save(deliveryPerson).block();

        // Get all the deliveryPersonList
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
            .value(hasItem(deliveryPerson.getId().intValue()))
            .jsonPath("$.[*].iD")
            .value(hasItem(DEFAULT_I_D.intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].surname")
            .value(hasItem(DEFAULT_SURNAME))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE))
            .jsonPath("$.[*].vehiculeType")
            .value(hasItem(DEFAULT_VEHICULE_TYPE))
            .jsonPath("$.[*].latitude")
            .value(hasItem(DEFAULT_LATITUDE.doubleValue()))
            .jsonPath("$.[*].longitude")
            .value(hasItem(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    void getDeliveryPerson() {
        // Initialize the database
        deliveryPersonRepository.save(deliveryPerson).block();

        // Get the deliveryPerson
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, deliveryPerson.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(deliveryPerson.getId().intValue()))
            .jsonPath("$.iD")
            .value(is(DEFAULT_I_D.intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.surname")
            .value(is(DEFAULT_SURNAME))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE))
            .jsonPath("$.vehiculeType")
            .value(is(DEFAULT_VEHICULE_TYPE))
            .jsonPath("$.latitude")
            .value(is(DEFAULT_LATITUDE.doubleValue()))
            .jsonPath("$.longitude")
            .value(is(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    void getNonExistingDeliveryPerson() {
        // Get the deliveryPerson
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDeliveryPerson() throws Exception {
        // Initialize the database
        deliveryPersonRepository.save(deliveryPerson).block();

        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();

        // Update the deliveryPerson
        DeliveryPerson updatedDeliveryPerson = deliveryPersonRepository.findById(deliveryPerson.getId()).block();
        updatedDeliveryPerson
            .iD(UPDATED_I_D)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .phone(UPDATED_PHONE)
            .vehiculeType(UPDATED_VEHICULE_TYPE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(updatedDeliveryPerson);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deliveryPersonDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
        DeliveryPerson testDeliveryPerson = deliveryPersonList.get(deliveryPersonList.size() - 1);
        assertThat(testDeliveryPerson.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testDeliveryPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeliveryPerson.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testDeliveryPerson.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDeliveryPerson.getVehiculeType()).isEqualTo(UPDATED_VEHICULE_TYPE);
        assertThat(testDeliveryPerson.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testDeliveryPerson.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    void putNonExistingDeliveryPerson() throws Exception {
        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();
        deliveryPerson.setId(count.incrementAndGet());

        // Create the DeliveryPerson
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deliveryPersonDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDeliveryPerson() throws Exception {
        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();
        deliveryPerson.setId(count.incrementAndGet());

        // Create the DeliveryPerson
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDeliveryPerson() throws Exception {
        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();
        deliveryPerson.setId(count.incrementAndGet());

        // Create the DeliveryPerson
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeliveryPersonWithPatch() throws Exception {
        // Initialize the database
        deliveryPersonRepository.save(deliveryPerson).block();

        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();

        // Update the deliveryPerson using partial update
        DeliveryPerson partialUpdatedDeliveryPerson = new DeliveryPerson();
        partialUpdatedDeliveryPerson.setId(deliveryPerson.getId());

        partialUpdatedDeliveryPerson
            .iD(UPDATED_I_D)
            .surname(UPDATED_SURNAME)
            .phone(UPDATED_PHONE)
            .vehiculeType(UPDATED_VEHICULE_TYPE)
            .longitude(UPDATED_LONGITUDE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeliveryPerson.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryPerson))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
        DeliveryPerson testDeliveryPerson = deliveryPersonList.get(deliveryPersonList.size() - 1);
        assertThat(testDeliveryPerson.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testDeliveryPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeliveryPerson.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testDeliveryPerson.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDeliveryPerson.getVehiculeType()).isEqualTo(UPDATED_VEHICULE_TYPE);
        assertThat(testDeliveryPerson.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testDeliveryPerson.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    void fullUpdateDeliveryPersonWithPatch() throws Exception {
        // Initialize the database
        deliveryPersonRepository.save(deliveryPerson).block();

        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();

        // Update the deliveryPerson using partial update
        DeliveryPerson partialUpdatedDeliveryPerson = new DeliveryPerson();
        partialUpdatedDeliveryPerson.setId(deliveryPerson.getId());

        partialUpdatedDeliveryPerson
            .iD(UPDATED_I_D)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .phone(UPDATED_PHONE)
            .vehiculeType(UPDATED_VEHICULE_TYPE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeliveryPerson.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryPerson))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
        DeliveryPerson testDeliveryPerson = deliveryPersonList.get(deliveryPersonList.size() - 1);
        assertThat(testDeliveryPerson.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testDeliveryPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeliveryPerson.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testDeliveryPerson.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDeliveryPerson.getVehiculeType()).isEqualTo(UPDATED_VEHICULE_TYPE);
        assertThat(testDeliveryPerson.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testDeliveryPerson.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    void patchNonExistingDeliveryPerson() throws Exception {
        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();
        deliveryPerson.setId(count.incrementAndGet());

        // Create the DeliveryPerson
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, deliveryPersonDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDeliveryPerson() throws Exception {
        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();
        deliveryPerson.setId(count.incrementAndGet());

        // Create the DeliveryPerson
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDeliveryPerson() throws Exception {
        int databaseSizeBeforeUpdate = deliveryPersonRepository.findAll().collectList().block().size();
        deliveryPerson.setId(count.incrementAndGet());

        // Create the DeliveryPerson
        DeliveryPersonDTO deliveryPersonDTO = deliveryPersonMapper.toDto(deliveryPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryPersonDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DeliveryPerson in the database
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDeliveryPerson() {
        // Initialize the database
        deliveryPersonRepository.save(deliveryPerson).block();

        int databaseSizeBeforeDelete = deliveryPersonRepository.findAll().collectList().block().size();

        // Delete the deliveryPerson
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, deliveryPerson.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DeliveryPerson> deliveryPersonList = deliveryPersonRepository.findAll().collectList().block();
        assertThat(deliveryPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

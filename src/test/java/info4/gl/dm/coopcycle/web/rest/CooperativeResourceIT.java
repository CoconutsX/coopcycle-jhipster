package info4.gl.dm.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import info4.gl.dm.coopcycle.IntegrationTest;
import info4.gl.dm.coopcycle.domain.Cooperative;
import info4.gl.dm.coopcycle.repository.CooperativeRepository;
import info4.gl.dm.coopcycle.repository.EntityManager;
import info4.gl.dm.coopcycle.service.dto.CooperativeDTO;
import info4.gl.dm.coopcycle.service.mapper.CooperativeMapper;
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
 * Integration tests for the {@link CooperativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CooperativeResourceIT {

    private static final Long DEFAULT_I_D = 1L;
    private static final Long UPDATED_I_D = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_URL = "AAAAAAAAAA";
    private static final String UPDATED_WEB_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cooperatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CooperativeRepository cooperativeRepository;

    @Autowired
    private CooperativeMapper cooperativeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Cooperative cooperative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cooperative createEntity(EntityManager em) {
        Cooperative cooperative = new Cooperative()
            .iD(DEFAULT_I_D)
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .webURL(DEFAULT_WEB_URL);
        return cooperative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cooperative createUpdatedEntity(EntityManager em) {
        Cooperative cooperative = new Cooperative()
            .iD(UPDATED_I_D)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .webURL(UPDATED_WEB_URL);
        return cooperative;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Cooperative.class).block();
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
        cooperative = createEntity(em);
    }

    @Test
    void createCooperative() throws Exception {
        int databaseSizeBeforeCreate = cooperativeRepository.findAll().collectList().block().size();
        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeCreate + 1);
        Cooperative testCooperative = cooperativeList.get(cooperativeList.size() - 1);
        assertThat(testCooperative.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testCooperative.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCooperative.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCooperative.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCooperative.getWebURL()).isEqualTo(DEFAULT_WEB_URL);
    }

    @Test
    void createCooperativeWithExistingId() throws Exception {
        // Create the Cooperative with an existing ID
        cooperative.setId(1L);
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        int databaseSizeBeforeCreate = cooperativeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkiDIsRequired() throws Exception {
        int databaseSizeBeforeTest = cooperativeRepository.findAll().collectList().block().size();
        // set the field null
        cooperative.setiD(null);

        // Create the Cooperative, which fails.
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = cooperativeRepository.findAll().collectList().block().size();
        // set the field null
        cooperative.setAddress(null);

        // Create the Cooperative, which fails.
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCooperativesAsStream() {
        // Initialize the database
        cooperativeRepository.save(cooperative).block();

        List<Cooperative> cooperativeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CooperativeDTO.class)
            .getResponseBody()
            .map(cooperativeMapper::toEntity)
            .filter(cooperative::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(cooperativeList).isNotNull();
        assertThat(cooperativeList).hasSize(1);
        Cooperative testCooperative = cooperativeList.get(0);
        assertThat(testCooperative.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testCooperative.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCooperative.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCooperative.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCooperative.getWebURL()).isEqualTo(DEFAULT_WEB_URL);
    }

    @Test
    void getAllCooperatives() {
        // Initialize the database
        cooperativeRepository.save(cooperative).block();

        // Get all the cooperativeList
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
            .value(hasItem(cooperative.getId().intValue()))
            .jsonPath("$.[*].iD")
            .value(hasItem(DEFAULT_I_D.intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS))
            .jsonPath("$.[*].webURL")
            .value(hasItem(DEFAULT_WEB_URL));
    }

    @Test
    void getCooperative() {
        // Initialize the database
        cooperativeRepository.save(cooperative).block();

        // Get the cooperative
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cooperative.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cooperative.getId().intValue()))
            .jsonPath("$.iD")
            .value(is(DEFAULT_I_D.intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS))
            .jsonPath("$.webURL")
            .value(is(DEFAULT_WEB_URL));
    }

    @Test
    void getNonExistingCooperative() {
        // Get the cooperative
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCooperative() throws Exception {
        // Initialize the database
        cooperativeRepository.save(cooperative).block();

        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();

        // Update the cooperative
        Cooperative updatedCooperative = cooperativeRepository.findById(cooperative.getId()).block();
        updatedCooperative.iD(UPDATED_I_D).name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS).webURL(UPDATED_WEB_URL);
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(updatedCooperative);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cooperativeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
        Cooperative testCooperative = cooperativeList.get(cooperativeList.size() - 1);
        assertThat(testCooperative.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testCooperative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCooperative.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCooperative.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCooperative.getWebURL()).isEqualTo(UPDATED_WEB_URL);
    }

    @Test
    void putNonExistingCooperative() throws Exception {
        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();
        cooperative.setId(count.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cooperativeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCooperative() throws Exception {
        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();
        cooperative.setId(count.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCooperative() throws Exception {
        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();
        cooperative.setId(count.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCooperativeWithPatch() throws Exception {
        // Initialize the database
        cooperativeRepository.save(cooperative).block();

        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();

        // Update the cooperative using partial update
        Cooperative partialUpdatedCooperative = new Cooperative();
        partialUpdatedCooperative.setId(cooperative.getId());

        partialUpdatedCooperative.name(UPDATED_NAME).phone(UPDATED_PHONE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCooperative.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCooperative))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
        Cooperative testCooperative = cooperativeList.get(cooperativeList.size() - 1);
        assertThat(testCooperative.getiD()).isEqualTo(DEFAULT_I_D);
        assertThat(testCooperative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCooperative.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCooperative.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCooperative.getWebURL()).isEqualTo(DEFAULT_WEB_URL);
    }

    @Test
    void fullUpdateCooperativeWithPatch() throws Exception {
        // Initialize the database
        cooperativeRepository.save(cooperative).block();

        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();

        // Update the cooperative using partial update
        Cooperative partialUpdatedCooperative = new Cooperative();
        partialUpdatedCooperative.setId(cooperative.getId());

        partialUpdatedCooperative.iD(UPDATED_I_D).name(UPDATED_NAME).phone(UPDATED_PHONE).address(UPDATED_ADDRESS).webURL(UPDATED_WEB_URL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCooperative.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCooperative))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
        Cooperative testCooperative = cooperativeList.get(cooperativeList.size() - 1);
        assertThat(testCooperative.getiD()).isEqualTo(UPDATED_I_D);
        assertThat(testCooperative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCooperative.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCooperative.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCooperative.getWebURL()).isEqualTo(UPDATED_WEB_URL);
    }

    @Test
    void patchNonExistingCooperative() throws Exception {
        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();
        cooperative.setId(count.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cooperativeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCooperative() throws Exception {
        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();
        cooperative.setId(count.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCooperative() throws Exception {
        int databaseSizeBeforeUpdate = cooperativeRepository.findAll().collectList().block().size();
        cooperative.setId(count.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cooperativeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cooperative in the database
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCooperative() {
        // Initialize the database
        cooperativeRepository.save(cooperative).block();

        int databaseSizeBeforeDelete = cooperativeRepository.findAll().collectList().block().size();

        // Delete the cooperative
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cooperative.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Cooperative> cooperativeList = cooperativeRepository.findAll().collectList().block();
        assertThat(cooperativeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

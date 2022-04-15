package info4.gl.dm.coopcycle.web.rest;

import static info4.gl.dm.coopcycle.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import info4.gl.dm.coopcycle.IntegrationTest;
import info4.gl.dm.coopcycle.domain.Order;
import info4.gl.dm.coopcycle.domain.enumeration.State;
import info4.gl.dm.coopcycle.repository.EntityManager;
import info4.gl.dm.coopcycle.repository.OrderRepository;
import info4.gl.dm.coopcycle.service.dto.OrderDTO;
import info4.gl.dm.coopcycle.service.mapper.OrderMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OrderResourceIT {

    private static final Long DEFAULT_I_D_ORDER = 1L;
    private static final Long UPDATED_I_D_ORDER = 2L;

    private static final Integer DEFAULT_I_D_COOP = 1;
    private static final Integer UPDATED_I_D_COOP = 2;

    private static final Integer DEFAULT_I_D_CUSTOMER = 1;
    private static final Integer UPDATED_I_D_CUSTOMER = 2;

    private static final Integer DEFAULT_I_D_COURSE = 1;
    private static final Integer UPDATED_I_D_COURSE = 2;

    private static final Integer DEFAULT_PRICE = 3;
    private static final Integer UPDATED_PRICE = 4;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final State DEFAULT_ORDER_STATE = State.Ordered;
    private static final State UPDATED_ORDER_STATE = State.Paid;

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .iDOrder(DEFAULT_I_D_ORDER)
            .iDCoop(DEFAULT_I_D_COOP)
            .iDCustomer(DEFAULT_I_D_CUSTOMER)
            .iDCourse(DEFAULT_I_D_COURSE)
            .price(DEFAULT_PRICE)
            .date(DEFAULT_DATE)
            .orderState(DEFAULT_ORDER_STATE);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .iDOrder(UPDATED_I_D_ORDER)
            .iDCoop(UPDATED_I_D_COOP)
            .iDCustomer(UPDATED_I_D_CUSTOMER)
            .iDCourse(UPDATED_I_D_COURSE)
            .price(UPDATED_PRICE)
            .date(UPDATED_DATE)
            .orderState(UPDATED_ORDER_STATE);
        return order;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Order.class).block();
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
        order = createEntity(em);
    }

    @Test
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().collectList().block().size();
        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getiDOrder()).isEqualTo(DEFAULT_I_D_ORDER);
        assertThat(testOrder.getiDCoop()).isEqualTo(DEFAULT_I_D_COOP);
        assertThat(testOrder.getiDCustomer()).isEqualTo(DEFAULT_I_D_CUSTOMER);
        assertThat(testOrder.getiDCourse()).isEqualTo(DEFAULT_I_D_COURSE);
        assertThat(testOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrder.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOrder.getOrderState()).isEqualTo(DEFAULT_ORDER_STATE);
    }

    @Test
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);
        OrderDTO orderDTO = orderMapper.toDto(order);

        int databaseSizeBeforeCreate = orderRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkiDOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setiDOrder(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkiDCoopIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setiDCoop(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkiDCustomerIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setiDCustomer(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkiDCourseIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setiDCourse(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOrdersAsStream() {
        // Initialize the database
        orderRepository.save(order).block();

        List<Order> orderList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(OrderDTO.class)
            .getResponseBody()
            .map(orderMapper::toEntity)
            .filter(order::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(orderList).isNotNull();
        assertThat(orderList).hasSize(1);
        Order testOrder = orderList.get(0);
        assertThat(testOrder.getiDOrder()).isEqualTo(DEFAULT_I_D_ORDER);
        assertThat(testOrder.getiDCoop()).isEqualTo(DEFAULT_I_D_COOP);
        assertThat(testOrder.getiDCustomer()).isEqualTo(DEFAULT_I_D_CUSTOMER);
        assertThat(testOrder.getiDCourse()).isEqualTo(DEFAULT_I_D_COURSE);
        assertThat(testOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrder.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testOrder.getOrderState()).isEqualTo(DEFAULT_ORDER_STATE);
    }

    @Test
    void getAllOrders() {
        // Initialize the database
        orderRepository.save(order).block();

        // Get all the orderList
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
            .value(hasItem(order.getId().intValue()))
            .jsonPath("$.[*].iDOrder")
            .value(hasItem(DEFAULT_I_D_ORDER.intValue()))
            .jsonPath("$.[*].iDCoop")
            .value(hasItem(DEFAULT_I_D_COOP))
            .jsonPath("$.[*].iDCustomer")
            .value(hasItem(DEFAULT_I_D_CUSTOMER))
            .jsonPath("$.[*].iDCourse")
            .value(hasItem(DEFAULT_I_D_COURSE))
            .jsonPath("$.[*].price")
            .value(hasItem(DEFAULT_PRICE))
            .jsonPath("$.[*].date")
            .value(hasItem(sameInstant(DEFAULT_DATE)))
            .jsonPath("$.[*].orderState")
            .value(hasItem(DEFAULT_ORDER_STATE.toString()));
    }

    @Test
    void getOrder() {
        // Initialize the database
        orderRepository.save(order).block();

        // Get the order
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, order.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(order.getId().intValue()))
            .jsonPath("$.iDOrder")
            .value(is(DEFAULT_I_D_ORDER.intValue()))
            .jsonPath("$.iDCoop")
            .value(is(DEFAULT_I_D_COOP))
            .jsonPath("$.iDCustomer")
            .value(is(DEFAULT_I_D_CUSTOMER))
            .jsonPath("$.iDCourse")
            .value(is(DEFAULT_I_D_COURSE))
            .jsonPath("$.price")
            .value(is(DEFAULT_PRICE))
            .jsonPath("$.date")
            .value(is(sameInstant(DEFAULT_DATE)))
            .jsonPath("$.orderState")
            .value(is(DEFAULT_ORDER_STATE.toString()));
    }

    @Test
    void getNonExistingOrder() {
        // Get the order
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).block();
        updatedOrder
            .iDOrder(UPDATED_I_D_ORDER)
            .iDCoop(UPDATED_I_D_COOP)
            .iDCustomer(UPDATED_I_D_CUSTOMER)
            .iDCourse(UPDATED_I_D_COURSE)
            .price(UPDATED_PRICE)
            .date(UPDATED_DATE)
            .orderState(UPDATED_ORDER_STATE);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, orderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getiDOrder()).isEqualTo(UPDATED_I_D_ORDER);
        assertThat(testOrder.getiDCoop()).isEqualTo(UPDATED_I_D_COOP);
        assertThat(testOrder.getiDCustomer()).isEqualTo(UPDATED_I_D_CUSTOMER);
        assertThat(testOrder.getiDCourse()).isEqualTo(UPDATED_I_D_COURSE);
        assertThat(testOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrder.getOrderState()).isEqualTo(UPDATED_ORDER_STATE);
    }

    @Test
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, orderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .iDOrder(UPDATED_I_D_ORDER)
            .iDCustomer(UPDATED_I_D_CUSTOMER)
            .iDCourse(UPDATED_I_D_COURSE)
            .price(UPDATED_PRICE)
            .date(UPDATED_DATE)
            .orderState(UPDATED_ORDER_STATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getiDOrder()).isEqualTo(UPDATED_I_D_ORDER);
        assertThat(testOrder.getiDCoop()).isEqualTo(DEFAULT_I_D_COOP);
        assertThat(testOrder.getiDCustomer()).isEqualTo(UPDATED_I_D_CUSTOMER);
        assertThat(testOrder.getiDCourse()).isEqualTo(UPDATED_I_D_COURSE);
        assertThat(testOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrder.getOrderState()).isEqualTo(UPDATED_ORDER_STATE);
    }

    @Test
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .iDOrder(UPDATED_I_D_ORDER)
            .iDCoop(UPDATED_I_D_COOP)
            .iDCustomer(UPDATED_I_D_CUSTOMER)
            .iDCourse(UPDATED_I_D_COURSE)
            .price(UPDATED_PRICE)
            .date(UPDATED_DATE)
            .orderState(UPDATED_ORDER_STATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getiDOrder()).isEqualTo(UPDATED_I_D_ORDER);
        assertThat(testOrder.getiDCoop()).isEqualTo(UPDATED_I_D_COOP);
        assertThat(testOrder.getiDCustomer()).isEqualTo(UPDATED_I_D_CUSTOMER);
        assertThat(testOrder.getiDCourse()).isEqualTo(UPDATED_I_D_COURSE);
        assertThat(testOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrder.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testOrder.getOrderState()).isEqualTo(UPDATED_ORDER_STATE);
    }

    @Test
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, orderDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrder() {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeDelete = orderRepository.findAll().collectList().block().size();

        // Delete the order
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, order.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package info4.gl.dm.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import info4.gl.dm.coopcycle.IntegrationTest;
import info4.gl.dm.coopcycle.domain.Product;
import info4.gl.dm.coopcycle.repository.EntityManager;
import info4.gl.dm.coopcycle.repository.ProductRepository;
import info4.gl.dm.coopcycle.service.dto.ProductDTO;
import info4.gl.dm.coopcycle.service.mapper.ProductMapper;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProductResourceIT {

    private static final Long DEFAULT_I_D_PRODUCT = 1L;
    private static final Long UPDATED_I_D_PRODUCT = 2L;

    private static final Long DEFAULT_I_D_MENU = 1L;
    private static final Long UPDATED_I_D_MENU = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Integer DEFAULT_STOCK = 0;
    private static final Integer UPDATED_STOCK = 1;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .iDProduct(DEFAULT_I_D_PRODUCT)
            .iDMenu(DEFAULT_I_D_MENU)
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .stock(DEFAULT_STOCK);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .iDProduct(UPDATED_I_D_PRODUCT)
            .iDMenu(UPDATED_I_D_MENU)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK);
        return product;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Product.class).block();
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
        product = createEntity(em);
    }

    @Test
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().collectList().block().size();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getiDProduct()).isEqualTo(DEFAULT_I_D_PRODUCT);
        assertThat(testProduct.getiDMenu()).isEqualTo(DEFAULT_I_D_MENU);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
    }

    @Test
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        int databaseSizeBeforeCreate = productRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkiDProductIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setiDProduct(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkiDMenuIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setiDMenu(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProductsAsStream() {
        // Initialize the database
        productRepository.save(product).block();

        List<Product> productList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ProductDTO.class)
            .getResponseBody()
            .map(productMapper::toEntity)
            .filter(product::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(productList).isNotNull();
        assertThat(productList).hasSize(1);
        Product testProduct = productList.get(0);
        assertThat(testProduct.getiDProduct()).isEqualTo(DEFAULT_I_D_PRODUCT);
        assertThat(testProduct.getiDMenu()).isEqualTo(DEFAULT_I_D_MENU);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
    }

    @Test
    void getAllProducts() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList
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
            .value(hasItem(product.getId().intValue()))
            .jsonPath("$.[*].iDProduct")
            .value(hasItem(DEFAULT_I_D_PRODUCT.intValue()))
            .jsonPath("$.[*].iDMenu")
            .value(hasItem(DEFAULT_I_D_MENU.intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].price")
            .value(hasItem(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.[*].stock")
            .value(hasItem(DEFAULT_STOCK));
    }

    @Test
    void getProduct() {
        // Initialize the database
        productRepository.save(product).block();

        // Get the product
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, product.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(product.getId().intValue()))
            .jsonPath("$.iDProduct")
            .value(is(DEFAULT_I_D_PRODUCT.intValue()))
            .jsonPath("$.iDMenu")
            .value(is(DEFAULT_I_D_MENU.intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.price")
            .value(is(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.stock")
            .value(is(DEFAULT_STOCK));
    }

    @Test
    void getNonExistingProduct() {
        // Get the product
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProduct() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).block();
        updatedProduct.iDProduct(UPDATED_I_D_PRODUCT).iDMenu(UPDATED_I_D_MENU).name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getiDProduct()).isEqualTo(UPDATED_I_D_PRODUCT);
        assertThat(testProduct.getiDMenu()).isEqualTo(UPDATED_I_D_MENU);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
    }

    @Test
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.stock(UPDATED_STOCK);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getiDProduct()).isEqualTo(DEFAULT_I_D_PRODUCT);
        assertThat(testProduct.getiDMenu()).isEqualTo(DEFAULT_I_D_MENU);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
    }

    @Test
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .iDProduct(UPDATED_I_D_PRODUCT)
            .iDMenu(UPDATED_I_D_MENU)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .stock(UPDATED_STOCK);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getiDProduct()).isEqualTo(UPDATED_I_D_PRODUCT);
        assertThat(testProduct.getiDMenu()).isEqualTo(UPDATED_I_D_MENU);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
    }

    @Test
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, productDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProduct() {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeDelete = productRepository.findAll().collectList().block().size();

        // Delete the product
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, product.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

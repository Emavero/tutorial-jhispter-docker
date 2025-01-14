package com.betterprojectsfaster.tutorial.jhipsterdocker.web.rest;

import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductOrderAsserts.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterprojectsfaster.tutorial.jhipsterdocker.IntegrationTest;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.Product;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductOrder;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrder;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.User;
import com.betterprojectsfaster.tutorial.jhipsterdocker.repository.ProductOrderRepository;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.ProductOrderService;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ProductOrderDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper.ProductOrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductOrderResourceIT {

    private static final Integer DEFAULT_AMOUNT = 0;
    private static final Integer UPDATED_AMOUNT = 1;

    private static final String ENTITY_API_URL = "/api/product-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Mock
    private ProductOrderRepository productOrderRepositoryMock;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Mock
    private ProductOrderService productOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductOrderMockMvc;

    private ProductOrder productOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createEntity(EntityManager em) {
        ProductOrder productOrder = new ProductOrder().amount(DEFAULT_AMOUNT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productOrder.setProduct(product);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        productOrder.setBuyer(user);
        // Add required entity
        ShoppingOrder shoppingOrder;
        if (TestUtil.findAll(em, ShoppingOrder.class).isEmpty()) {
            shoppingOrder = ShoppingOrderResourceIT.createEntity(em);
            em.persist(shoppingOrder);
            em.flush();
        } else {
            shoppingOrder = TestUtil.findAll(em, ShoppingOrder.class).get(0);
        }
        productOrder.setOverallOrder(shoppingOrder);
        return productOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createUpdatedEntity(EntityManager em) {
        ProductOrder productOrder = new ProductOrder().amount(UPDATED_AMOUNT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        productOrder.setProduct(product);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        productOrder.setBuyer(user);
        // Add required entity
        ShoppingOrder shoppingOrder;
        if (TestUtil.findAll(em, ShoppingOrder.class).isEmpty()) {
            shoppingOrder = ShoppingOrderResourceIT.createUpdatedEntity(em);
            em.persist(shoppingOrder);
            em.flush();
        } else {
            shoppingOrder = TestUtil.findAll(em, ShoppingOrder.class).get(0);
        }
        productOrder.setOverallOrder(shoppingOrder);
        return productOrder;
    }

    @BeforeEach
    public void initTest() {
        productOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createProductOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);
        var returnedProductOrderDTO = om.readValue(
            restProductOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductOrderDTO.class
        );

        // Validate the ProductOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductOrder = productOrderMapper.toEntity(returnedProductOrderDTO);
        assertProductOrderUpdatableFieldsEquals(returnedProductOrder, getPersistedProductOrder(returnedProductOrder));
    }

    @Test
    @Transactional
    void createProductOrderWithExistingId() throws Exception {
        // Create the ProductOrder with an existing ID
        productOrder.setId(1L);
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productOrder.setAmount(null);

        // Create the ProductOrder, which fails.
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductOrders() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get all the productOrderList
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(productOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        // Get the productOrder
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, productOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productOrder.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    @Transactional
    void getNonExistingProductOrder() throws Exception {
        // Get the productOrder
        restProductOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrder
        ProductOrder updatedProductOrder = productOrderRepository.findById(productOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductOrder are not directly saved in db
        em.detach(updatedProductOrder);
        updatedProductOrder.amount(UPDATED_AMOUNT);
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(updatedProductOrder);

        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductOrderToMatchAllProperties(updatedProductOrder);
    }

    @Test
    @Transactional
    void putNonExistingProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductOrderWithPatch() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrder using partial update
        ProductOrder partialUpdatedProductOrder = new ProductOrder();
        partialUpdatedProductOrder.setId(productOrder.getId());

        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductOrder, productOrder),
            getPersistedProductOrder(productOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductOrderWithPatch() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productOrder using partial update
        ProductOrder partialUpdatedProductOrder = new ProductOrder();
        partialUpdatedProductOrder.setId(productOrder.getId());

        partialUpdatedProductOrder.amount(UPDATED_AMOUNT);

        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductOrderUpdatableFieldsEquals(partialUpdatedProductOrder, getPersistedProductOrder(partialUpdatedProductOrder));
    }

    @Test
    @Transactional
    void patchNonExistingProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productOrder.setId(longCount.incrementAndGet());

        // Create the ProductOrder
        ProductOrderDTO productOrderDTO = productOrderMapper.toDto(productOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.saveAndFlush(productOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productOrder
        restProductOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, productOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productOrderRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProductOrder getPersistedProductOrder(ProductOrder productOrder) {
        return productOrderRepository.findById(productOrder.getId()).orElseThrow();
    }

    protected void assertPersistedProductOrderToMatchAllProperties(ProductOrder expectedProductOrder) {
        assertProductOrderAllPropertiesEquals(expectedProductOrder, getPersistedProductOrder(expectedProductOrder));
    }

    protected void assertPersistedProductOrderToMatchUpdatableProperties(ProductOrder expectedProductOrder) {
        assertProductOrderAllUpdatablePropertiesEquals(expectedProductOrder, getPersistedProductOrder(expectedProductOrder));
    }
}

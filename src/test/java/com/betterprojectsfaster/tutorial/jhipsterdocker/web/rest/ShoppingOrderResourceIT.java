package com.betterprojectsfaster.tutorial.jhipsterdocker.web.rest;

import static com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrderAsserts.*;
import static com.betterprojectsfaster.tutorial.jhipsterdocker.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.betterprojectsfaster.tutorial.jhipsterdocker.IntegrationTest;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrder;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.User;
import com.betterprojectsfaster.tutorial.jhipsterdocker.repository.ShoppingOrderRepository;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.ShoppingOrderService;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ShoppingOrderDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper.ShoppingOrderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ShoppingOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShoppingOrderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_TOTAL_AMOUNT = 0F;
    private static final Float UPDATED_TOTAL_AMOUNT = 1F;

    private static final LocalDate DEFAULT_ORDERED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDERED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/shopping-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShoppingOrderRepository shoppingOrderRepository;

    @Mock
    private ShoppingOrderRepository shoppingOrderRepositoryMock;

    @Autowired
    private ShoppingOrderMapper shoppingOrderMapper;

    @Mock
    private ShoppingOrderService shoppingOrderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingOrderMockMvc;

    private ShoppingOrder shoppingOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingOrder createEntity(EntityManager em) {
        ShoppingOrder shoppingOrder = new ShoppingOrder().name(DEFAULT_NAME).totalAmount(DEFAULT_TOTAL_AMOUNT).ordered(DEFAULT_ORDERED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        shoppingOrder.setBuyer(user);
        return shoppingOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingOrder createUpdatedEntity(EntityManager em) {
        ShoppingOrder shoppingOrder = new ShoppingOrder().name(UPDATED_NAME).totalAmount(UPDATED_TOTAL_AMOUNT).ordered(UPDATED_ORDERED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        shoppingOrder.setBuyer(user);
        return shoppingOrder;
    }

    @BeforeEach
    public void initTest() {
        shoppingOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createShoppingOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShoppingOrder
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);
        var returnedShoppingOrderDTO = om.readValue(
            restShoppingOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingOrderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShoppingOrderDTO.class
        );

        // Validate the ShoppingOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShoppingOrder = shoppingOrderMapper.toEntity(returnedShoppingOrderDTO);
        assertShoppingOrderUpdatableFieldsEquals(returnedShoppingOrder, getPersistedShoppingOrder(returnedShoppingOrder));
    }

    @Test
    @Transactional
    void createShoppingOrderWithExistingId() throws Exception {
        // Create the ShoppingOrder with an existing ID
        shoppingOrder.setId(1L);
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shoppingOrder.setName(null);

        // Create the ShoppingOrder, which fails.
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);

        restShoppingOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShoppingOrders() throws Exception {
        // Initialize the database
        shoppingOrderRepository.saveAndFlush(shoppingOrder);

        // Get all the shoppingOrderList
        restShoppingOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].ordered").value(hasItem(DEFAULT_ORDERED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShoppingOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(shoppingOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShoppingOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(shoppingOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShoppingOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(shoppingOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShoppingOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(shoppingOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShoppingOrder() throws Exception {
        // Initialize the database
        shoppingOrderRepository.saveAndFlush(shoppingOrder);

        // Get the shoppingOrder
        restShoppingOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, shoppingOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingOrder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.ordered").value(DEFAULT_ORDERED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingShoppingOrder() throws Exception {
        // Get the shoppingOrder
        restShoppingOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShoppingOrder() throws Exception {
        // Initialize the database
        shoppingOrderRepository.saveAndFlush(shoppingOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingOrder
        ShoppingOrder updatedShoppingOrder = shoppingOrderRepository.findById(shoppingOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShoppingOrder are not directly saved in db
        em.detach(updatedShoppingOrder);
        updatedShoppingOrder.name(UPDATED_NAME).totalAmount(UPDATED_TOTAL_AMOUNT).ordered(UPDATED_ORDERED);
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(updatedShoppingOrder);

        restShoppingOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShoppingOrderToMatchAllProperties(updatedShoppingOrder);
    }

    @Test
    @Transactional
    void putNonExistingShoppingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingOrder.setId(longCount.incrementAndGet());

        // Create the ShoppingOrder
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShoppingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingOrder.setId(longCount.incrementAndGet());

        // Create the ShoppingOrder
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShoppingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingOrder.setId(longCount.incrementAndGet());

        // Create the ShoppingOrder
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShoppingOrderWithPatch() throws Exception {
        // Initialize the database
        shoppingOrderRepository.saveAndFlush(shoppingOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingOrder using partial update
        ShoppingOrder partialUpdatedShoppingOrder = new ShoppingOrder();
        partialUpdatedShoppingOrder.setId(shoppingOrder.getId());

        restShoppingOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingOrder))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShoppingOrder, shoppingOrder),
            getPersistedShoppingOrder(shoppingOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateShoppingOrderWithPatch() throws Exception {
        // Initialize the database
        shoppingOrderRepository.saveAndFlush(shoppingOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingOrder using partial update
        ShoppingOrder partialUpdatedShoppingOrder = new ShoppingOrder();
        partialUpdatedShoppingOrder.setId(shoppingOrder.getId());

        partialUpdatedShoppingOrder.name(UPDATED_NAME).totalAmount(UPDATED_TOTAL_AMOUNT).ordered(UPDATED_ORDERED);

        restShoppingOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingOrder))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingOrderUpdatableFieldsEquals(partialUpdatedShoppingOrder, getPersistedShoppingOrder(partialUpdatedShoppingOrder));
    }

    @Test
    @Transactional
    void patchNonExistingShoppingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingOrder.setId(longCount.incrementAndGet());

        // Create the ShoppingOrder
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shoppingOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShoppingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingOrder.setId(longCount.incrementAndGet());

        // Create the ShoppingOrder
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShoppingOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingOrder.setId(longCount.incrementAndGet());

        // Create the ShoppingOrder
        ShoppingOrderDTO shoppingOrderDTO = shoppingOrderMapper.toDto(shoppingOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shoppingOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShoppingOrder() throws Exception {
        // Initialize the database
        shoppingOrderRepository.saveAndFlush(shoppingOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shoppingOrder
        restShoppingOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, shoppingOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shoppingOrderRepository.count();
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

    protected ShoppingOrder getPersistedShoppingOrder(ShoppingOrder shoppingOrder) {
        return shoppingOrderRepository.findById(shoppingOrder.getId()).orElseThrow();
    }

    protected void assertPersistedShoppingOrderToMatchAllProperties(ShoppingOrder expectedShoppingOrder) {
        assertShoppingOrderAllPropertiesEquals(expectedShoppingOrder, getPersistedShoppingOrder(expectedShoppingOrder));
    }

    protected void assertPersistedShoppingOrderToMatchUpdatableProperties(ShoppingOrder expectedShoppingOrder) {
        assertShoppingOrderAllUpdatablePropertiesEquals(expectedShoppingOrder, getPersistedShoppingOrder(expectedShoppingOrder));
    }
}

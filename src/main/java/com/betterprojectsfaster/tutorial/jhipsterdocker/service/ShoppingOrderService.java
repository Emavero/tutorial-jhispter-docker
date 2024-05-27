package com.betterprojectsfaster.tutorial.jhipsterdocker.service;

import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ShoppingOrderDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrder}.
 */
public interface ShoppingOrderService {
    /**
     * Save a shoppingOrder.
     *
     * @param shoppingOrderDTO the entity to save.
     * @return the persisted entity.
     */
    ShoppingOrderDTO save(ShoppingOrderDTO shoppingOrderDTO);

    /**
     * Updates a shoppingOrder.
     *
     * @param shoppingOrderDTO the entity to update.
     * @return the persisted entity.
     */
    ShoppingOrderDTO update(ShoppingOrderDTO shoppingOrderDTO);

    /**
     * Partially updates a shoppingOrder.
     *
     * @param shoppingOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShoppingOrderDTO> partialUpdate(ShoppingOrderDTO shoppingOrderDTO);

    /**
     * Get all the shoppingOrders.
     *
     * @return the list of entities.
     */
    List<ShoppingOrderDTO> findAll();

    /**
     * Get all the ShoppingOrderDTO where Shipment is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ShoppingOrderDTO> findAllWhereShipmentIsNull();

    /**
     * Get all the shoppingOrders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShoppingOrderDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" shoppingOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShoppingOrderDTO> findOne(Long id);

    /**
     * Delete the "id" shoppingOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

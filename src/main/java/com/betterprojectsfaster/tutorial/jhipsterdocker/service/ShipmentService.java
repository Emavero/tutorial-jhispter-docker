package com.betterprojectsfaster.tutorial.jhipsterdocker.service;

import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ShipmentDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.betterprojectsfaster.tutorial.jhipsterdocker.domain.Shipment}.
 */
public interface ShipmentService {
    /**
     * Save a shipment.
     *
     * @param shipmentDTO the entity to save.
     * @return the persisted entity.
     */
    ShipmentDTO save(ShipmentDTO shipmentDTO);

    /**
     * Updates a shipment.
     *
     * @param shipmentDTO the entity to update.
     * @return the persisted entity.
     */
    ShipmentDTO update(ShipmentDTO shipmentDTO);

    /**
     * Partially updates a shipment.
     *
     * @param shipmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShipmentDTO> partialUpdate(ShipmentDTO shipmentDTO);

    /**
     * Get all the shipments.
     *
     * @return the list of entities.
     */
    List<ShipmentDTO> findAll();

    /**
     * Get all the shipments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShipmentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" shipment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShipmentDTO> findOne(Long id);

    /**
     * Delete the "id" shipment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

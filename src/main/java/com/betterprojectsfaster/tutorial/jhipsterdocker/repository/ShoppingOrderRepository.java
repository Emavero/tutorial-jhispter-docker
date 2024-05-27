package com.betterprojectsfaster.tutorial.jhipsterdocker.repository;

import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShoppingOrder entity.
 */
@Repository
public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Long> {
    @Query("select shoppingOrder from ShoppingOrder shoppingOrder where shoppingOrder.buyer.login = ?#{authentication.name}")
    List<ShoppingOrder> findByBuyerIsCurrentUser();

    default Optional<ShoppingOrder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ShoppingOrder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ShoppingOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select shoppingOrder from ShoppingOrder shoppingOrder left join fetch shoppingOrder.buyer",
        countQuery = "select count(shoppingOrder) from ShoppingOrder shoppingOrder"
    )
    Page<ShoppingOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select shoppingOrder from ShoppingOrder shoppingOrder left join fetch shoppingOrder.buyer")
    List<ShoppingOrder> findAllWithToOneRelationships();

    @Query("select shoppingOrder from ShoppingOrder shoppingOrder left join fetch shoppingOrder.buyer where shoppingOrder.id =:id")
    Optional<ShoppingOrder> findOneWithToOneRelationships(@Param("id") Long id);
}

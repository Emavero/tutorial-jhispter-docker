package com.betterprojectsfaster.tutorial.jhipsterdocker.repository;

import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.Authority;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {}

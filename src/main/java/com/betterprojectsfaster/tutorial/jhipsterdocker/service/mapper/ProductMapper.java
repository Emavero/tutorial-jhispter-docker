package com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper;

import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.Product;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {}

package com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper;

import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.Product;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ProductOrder;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrder;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.User;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ProductDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ProductOrderDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ShoppingOrderDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOrder} and its DTO {@link ProductOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductOrderMapper extends EntityMapper<ProductOrderDTO, ProductOrder> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "buyer", source = "buyer", qualifiedByName = "userLogin")
    @Mapping(target = "overallOrder", source = "overallOrder", qualifiedByName = "shoppingOrderName")
    ProductOrderDTO toDto(ProductOrder s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("shoppingOrderName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ShoppingOrderDTO toDtoShoppingOrderName(ShoppingOrder shoppingOrder);
}

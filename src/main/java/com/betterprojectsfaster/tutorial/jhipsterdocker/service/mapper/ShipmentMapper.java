package com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper;

import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.Shipment;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrder;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.User;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ShipmentDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ShoppingOrderDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Shipment} and its DTO {@link ShipmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShipmentMapper extends EntityMapper<ShipmentDTO, Shipment> {
    @Mapping(target = "order", source = "order", qualifiedByName = "shoppingOrderName")
    @Mapping(target = "shippedBy", source = "shippedBy", qualifiedByName = "userLogin")
    ShipmentDTO toDto(Shipment s);

    @Named("shoppingOrderName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ShoppingOrderDTO toDtoShoppingOrderName(ShoppingOrder shoppingOrder);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

package com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper;

import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.ShoppingOrder;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.User;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.ShoppingOrderDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShoppingOrder} and its DTO {@link ShoppingOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShoppingOrderMapper extends EntityMapper<ShoppingOrderDTO, ShoppingOrder> {
    @Mapping(target = "buyer", source = "buyer", qualifiedByName = "userLogin")
    ShoppingOrderDTO toDto(ShoppingOrder s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

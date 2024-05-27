package com.betterprojectsfaster.tutorial.jhipsterdocker.service.mapper;

import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.Address;
import com.betterprojectsfaster.tutorial.jhipsterdocker.domain.User;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.AddressDTO;
import com.betterprojectsfaster.tutorial.jhipsterdocker.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    AddressDTO toDto(Address s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}

package com.seregamazur.oauth2.tutorial.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {

    @Named("default")
    UserDTO toDto(User s);

}
package com.dbtest.yashwith.mappers;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.model.auth.UserCreateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User createRequestToModel(final UserCreateRequest userCreateRequest);
}

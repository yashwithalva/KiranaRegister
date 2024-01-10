package com.dbtest.yashwith.mappers;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.model.auth.UserCreateRequest;
import com.dbtest.yashwith.model.user.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "countryCode", source = "countryCode")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    User requestToUser(UserCreateRequest userCreateRequest);


    @Mapping(target = "firstName", source="firstName")
    @Mapping(target = "lastName", source="lastName")
    @Mapping(target = "age", source="age")
    @Mapping(target = "profilePicture", source="profilePicture")
    @Mapping(target = "gender", source="gender")
    @Mapping(target = "phoneNumber", source="phoneNumber")
    @Mapping(target = "createdAt", expression ="java(user.getCreatedAt().getTime())")
    UserProfile userToUserProfile(User user);
}

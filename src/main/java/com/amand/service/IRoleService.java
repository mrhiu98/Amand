package com.amand.service;

import com.amand.dto.RoleDto;
import com.amand.dto.UserDto;

import java.util.List;

public interface IRoleService {
    List<RoleDto> findAll();

    List<String> getRoleCodesByUserDto(UserDto userDto);
}

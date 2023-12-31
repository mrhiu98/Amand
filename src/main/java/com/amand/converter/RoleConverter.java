package com.amand.converter;

import com.amand.dto.RoleDto;
import com.amand.entity.RoleEntity;
import com.amand.form.RoleForm;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {

    public RoleEntity toEntity(RoleDto roleDto) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleDto.getName());
        roleEntity.setCode(roleDto.getCode());
        return roleEntity;
    }

    public RoleDto toDto(RoleEntity roleEntity) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(roleEntity.getId());
        roleDto.setName(roleEntity.getName());
        roleDto.setCode(roleEntity.getCode());
        return roleDto;
    }

    public RoleEntity toEntity (RoleForm roleForm) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleForm.getName());
        roleEntity.setCode(roleForm.getCode());
        return roleEntity;
    }
}

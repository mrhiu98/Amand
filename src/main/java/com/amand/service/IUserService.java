package com.amand.service;

import com.amand.dto.UserDto;
import com.amand.form.UserForm;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IUserService {

    UserDto save(UserForm userForm);

    Map<String, String> validate(UserForm userForm, boolean isAdmin);

    List<UserDto> findAll(Pageable pageable);

    int getTotalItem();
}

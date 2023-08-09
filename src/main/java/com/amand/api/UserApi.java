package com.amand.api;

import com.amand.constant.SystemConstant;
import com.amand.dto.ApiResponse;
import com.amand.dto.UserDto;
import com.amand.repository.UserRepository;
import com.amand.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class UserApi {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping( "/user")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){
        Map<String, String> validateResult = userService.validate(userDto, false);
        if (!CollectionUtils.isEmpty(validateResult)) {
            ApiResponse response = new ApiResponse(SystemConstant.API_STATUS_NG, validateResult);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse(SystemConstant.API_STATUS_OK, List.of(userService.save(userDto)));
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping( "/user")
    public ResponseEntity<?> createUser(){
        return ResponseEntity.ok(userRepository.findAll());
    }
}

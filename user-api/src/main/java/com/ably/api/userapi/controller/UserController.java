package com.ably.api.userapi.controller;

import com.ably.api.userapi.dto.UserDto;
import com.ably.api.userapi.service.UserService;
import com.ably.api.userapi.vo.RequestSetPassword;
import com.ably.api.userapi.vo.RequestUser;
import com.ably.api.userapi.vo.ResponseUser;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/users/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("email") String email) {
        UserDto userDto = userService.getUserDetailsByEmail(email);
        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @PostMapping("/join")
    public ResponseEntity createUser(@RequestHeader(value = "authKey") String authKey, @RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto createUser = userService.createUser(authKey,userDto);

        ResponseUser responseUser = mapper.map(createUser, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @PostMapping("/set/password")
    public ResponseEntity setPassword(@RequestHeader(value = "authKey") String authKey, @RequestBody RequestSetPassword request) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(request, UserDto.class);
        UserDto updateUser = userService.updatePassword(authKey, userDto);

        ResponseUser responseUser = mapper.map(updateUser, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}

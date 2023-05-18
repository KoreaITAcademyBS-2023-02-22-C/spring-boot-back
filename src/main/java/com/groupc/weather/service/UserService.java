package com.groupc.weather.service;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.follow.FollowRequestDto;
import com.groupc.weather.dto.request.user.FindByEmailRequestDto;
import com.groupc.weather.dto.request.user.FindByPasswordRequestDto;
import com.groupc.weather.dto.request.user.LoginUserRequestDto;
import com.groupc.weather.dto.request.user.PatchUserRequestDto;
//import com.groupc.weather.dto.request.user.PatchUserRequestDto2;
import com.groupc.weather.dto.request.user.PostUserRequestDto;
import com.groupc.weather.dto.response.user.FindByEmailResponseDto;
import com.groupc.weather.dto.response.user.FindByPasswordResponseDto;
import com.groupc.weather.dto.response.user.GetUserResponseDto;
import com.groupc.weather.dto.response.user.LoginUserResponseDto;

public interface UserService {
    public ResponseEntity<ResponseDto> postUser(PostUserRequestDto dto);

    public ResponseEntity<? super LoginUserResponseDto> LoginUser(LoginUserRequestDto dto);

    public ResponseEntity<? super FindByEmailResponseDto> FindByEmail(FindByEmailRequestDto dto);

    public ResponseEntity<? super FindByPasswordResponseDto> FindByPassword(FindByPasswordRequestDto dto);

    public ResponseEntity<ResponseDto> patchUser(PatchUserRequestDto dto);

    // public ResponseEntity<ResponseDto> patchUser(String userEmail,
    // PatchUserRequestDto2 requestBody);
    public ResponseEntity<ResponseDto> deleteUser(PostUserRequestDto dto);

    public ResponseEntity<? super GetUserResponseDto> getUser(Integer userNumber);

    public ResponseEntity<ResponseDto> followUser(Integer followingUserNumber);
}

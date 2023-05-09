package com.groupc.weather.dto.request.user;

import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

// 데이터 교환을 하기위한 객체 ( Entity <-> dto )

@Data
@NoArgsConstructor
// Pull Test
public class PostUserRequestDto {
    @NotNull
    private Integer userNumber;
    @NotBlank
    private String userName;
    @NotBlank
    private String userNickname;
    @NotBlank
    private String userPassword;
    @NotBlank
    @Email
    private String userEmail;
    private String userProfileImageUrl;
    @NotBlank
    private Date userBirthday;
    @NotBlank
    private String userGender;
    @NotBlank
    private String userAdress;
    @NotBlank
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
    private String userPhoneNumber;
}
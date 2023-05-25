package com.groupc.weather.dto.request.user;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteUserRequestDto {
    @NotBlank
    private Integer userNumber;
    @NotBlank
    protected String userPassword;
}

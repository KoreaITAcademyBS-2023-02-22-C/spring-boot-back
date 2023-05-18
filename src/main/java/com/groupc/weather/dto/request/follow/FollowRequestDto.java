package com.groupc.weather.dto.request.follow;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FollowRequestDto {
    @NotBlank
    private int userNumber;
    @NotBlank
    private Integer followingUserNumber;
}

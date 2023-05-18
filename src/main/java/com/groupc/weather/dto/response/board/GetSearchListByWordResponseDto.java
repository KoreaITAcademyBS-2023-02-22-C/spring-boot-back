package com.groupc.weather.dto.response.board;

import javax.validation.constraints.NotBlank;

import com.groupc.weather.dto.ResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetSearchListByWordResponseDto extends ResponseDto {

    @NotBlank
    private String searchWord;
    private int userNumber;
    // 상세검색 값(체크박스) 어떻게 가져오는지..
    private String[] temperature = 
        {"spring", "summer", "midsummmer", "fall", "winter", "midwinter"};
    private String[] weatherInfo;

}
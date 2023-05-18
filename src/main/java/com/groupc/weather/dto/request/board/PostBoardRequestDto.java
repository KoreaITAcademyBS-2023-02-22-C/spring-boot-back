package com.groupc.weather.dto.request.board;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostBoardRequestDto {
    @NotBlank
    private int userNumber;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String imageUrl;
    //@NotBlank
    //private List<ImageUrlList> imageUrlList;
    //private List<HashTag> hashtagList;
    @NotBlank
    private String weatherInfo;
    @NotBlank
    private int temperature;

}
@Data
@NoArgsConstructor
@AllArgsConstructor
class ImageUrlList {
    @Id
    @NotBlank
    private int imageNumber;
    @NotBlank
    private String imageUrl;
    @NotBlank
    private int boardNumber;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class HashTag {
    @Id
    @NotBlank
    private int hashtagNumber;

    @NotBlank
    @GeneratedValue
    private String hashtagContent;
}

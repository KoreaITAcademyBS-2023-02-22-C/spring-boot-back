package com.groupc.weather.service;

import org.springframework.http.ResponseEntity;

import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.qnaBoard.PatchQnaBoardRequestDto;
import com.groupc.weather.dto.request.qnaBoard.PostQnaBoardRequestDto;
import com.groupc.weather.dto.response.qnaBoard.GetQnaBoardListResponseDto;
import com.groupc.weather.dto.response.qnaBoard.GetQnaBoardResponseDto;

public interface QnaBoardService {
    public ResponseEntity<ResponseDto> postQnaBoard(PostQnaBoardRequestDto dto);

    public ResponseEntity<? super GetQnaBoardResponseDto> getQnaBoard(Integer boardNumber);
    public ResponseEntity<? super GetQnaBoardListResponseDto> getQnaBoardList();
    public ResponseEntity<? super GetQnaBoardListResponseDto> getSearchQnaBoardList(String searchWord);

    public ResponseEntity<ResponseDto> patchQnaBoard(PatchQnaBoardRequestDto dto);
    
    public ResponseEntity<ResponseDto> deleteQnaBoard(Integer userNumber, Integer boardNumber);



    
}

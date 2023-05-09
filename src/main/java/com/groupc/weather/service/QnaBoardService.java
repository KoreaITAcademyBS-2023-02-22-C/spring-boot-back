package com.groupc.weather.service;

import org.springframework.http.ResponseEntity;

import com.groupc.weather.dto.request.qnaBoard.PatchQnaBoardRequestDto;
import com.groupc.weather.dto.request.qnaBoard.PostQnaBoardRequestDto;
import com.groupc.weather.dto.response.ResponseDto;
import com.groupc.weather.dto.response.qnaBoard.GetQnaBoardListResponseDto;
import com.groupc.weather.dto.response.qnaBoard.GetQnaBoardResponseDto;

public interface QnaBoardService {
    public ResponseEntity<ResponseDto> postQnaBoard(PostQnaBoardRequestDto dto);

    public ResponseEntity<ResponseDto> getQnaBoard(GetQnaBoardResponseDto dto);
    public ResponseEntity<ResponseDto> getQnaBoardListResponseDto(GetQnaBoardListResponseDto dto);

    public ResponseEntity<ResponseDto> patchQnaBoard(PatchQnaBoardRequestDto dto);
    
    public ResponseEntity<ResponseDto> deleteQnaBoard(Integer writerNumber, Integer qnaBoardNumber);


    
}
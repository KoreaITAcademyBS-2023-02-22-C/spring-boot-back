package com.groupc.weather.service;

import org.springframework.http.ResponseEntity;

import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.qnaBoard.PatchQnaCommentRequestDto;
import com.groupc.weather.dto.request.qnaBoard.PostQnaCommentRequestDto;


public interface QnaCommentService {
    public ResponseEntity<ResponseDto> postQnaComment(PostQnaCommentRequestDto dto);
    public ResponseEntity<ResponseDto> patchQnaComment(PatchQnaCommentRequestDto dto);
    public ResponseEntity<ResponseDto> deleteQnaComment(Integer userNumber, Integer qnaCommentNumber);

    //public ResponseEntity<? super GetBoardListResponseDto> getBoard(Integer BoardNumber);
}

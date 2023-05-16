package com.groupc.weather.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.qnaBoard.PatchQnaCommentRequestDto;
import com.groupc.weather.dto.request.qnaBoard.PostQnaCommentRequestDto;
import com.groupc.weather.service.QnaCommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qnaComment")
public class QnaCommentController {
    private final QnaCommentService qnaCommentService;
    
    @PostMapping("post")
    public ResponseEntity<ResponseDto> postQnaComment(
        @Valid @RequestBody PostQnaCommentRequestDto requestBody

    ){
        ResponseEntity<ResponseDto> response = qnaCommentService.postQnaComment(requestBody);
        return response;
    }


    // @GetMapping("/{QnaBoardNumber}")
    // public ResponseEntity<ResponseDto> getQnaComment(
    //     @PathVariable("QnaBoardNumber") Integer qnaBoardNumber){
    //     ResponseEntity<ResponseDto> response = qnaCommentService.getQnaComment(qnaBoardNumber);
    //     return response;
    // }

    @PatchMapping("")
    public ResponseEntity<ResponseDto> patchQnaComment(
        @Valid @RequestBody PatchQnaCommentRequestDto requestBody
    )
    {
        ResponseEntity<ResponseDto> response = qnaCommentService.patchQnaComment(requestBody);
        return response;
    }

    @DeleteMapping("/{userNumber}/{qnaCommentNumber}")
    public ResponseEntity<ResponseDto> deleteQnaComment(
        @PathVariable("userNumber") Integer userNumber,
        @PathVariable("qnaCommentNumber") Integer qnaCommentNumber
    )
    {
        ResponseEntity<ResponseDto> response = qnaCommentService.deleteQnaComment(userNumber,qnaCommentNumber);
        return response;
    }


}

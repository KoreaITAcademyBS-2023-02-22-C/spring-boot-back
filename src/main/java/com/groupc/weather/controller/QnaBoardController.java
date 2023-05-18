package com.groupc.weather.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groupc.weather.dto.request.qnaBoard.PatchQnaBoardRequestDto;
import com.groupc.weather.dto.request.qnaBoard.PostQnaBoardRequestDto;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.response.qnaBoard.GetQnaBoardListResponseDto;
import com.groupc.weather.dto.response.qnaBoard.GetQnaBoardResponseDto;
import com.groupc.weather.service.QnaBoardService;

@RestController
@RequestMapping("/api/v1/qnaBoard")
public class QnaBoardController {
    
    private QnaBoardService qnaBoardService;

    @Autowired
    public QnaBoardController(QnaBoardService qnaBoardService) {
        this.qnaBoardService = qnaBoardService;
    }

    //* 1. 게시물 등록 */
    @PostMapping("")
    public ResponseEntity<ResponseDto> postQnaBoard(
        @Valid @RequestBody PostQnaBoardRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response =
            qnaBoardService.postQnaBoard(requestBody);
        return response;
    }
    
    //* 2. 특정 게시물 조회 */
    @GetMapping("/{qnaBoardNumber}")
    public ResponseEntity<? super GetQnaBoardResponseDto> getQnaBoard(
       @PathVariable Integer qnaBoardNumber 
    ) {
        ResponseEntity<? super GetQnaBoardResponseDto> response =
            qnaBoardService.getQnaBoard(qnaBoardNumber);
        return response;
    }

    //* 3. 게시물 목록 조회 */
    @GetMapping("list")
    public ResponseEntity<? super GetQnaBoardListResponseDto> getQnaBoardList() {
        ResponseEntity<? super GetQnaBoardListResponseDto> response =
            qnaBoardService.getQnaBoardList();
        return response;
    }

    //* 4. 특정 게시물 수정(본인만) */
    @PatchMapping("")
    public ResponseEntity<ResponseDto> patchQnaBoard(
        @Valid @RequestBody PatchQnaBoardRequestDto requestBody
    ) {
        ResponseEntity<ResponseDto> response =
            qnaBoardService.patchQnaBoard(requestBody);
        return response;
    }

    //* 5. 특정 게시물 삭제(본인, 관리자만) */
    @DeleteMapping("/{userNumber}/{qnaBoardNumber}")
    public ResponseEntity<ResponseDto> deleteQnaBoard(
        @PathVariable("userNumber") Integer userNumber,
        @PathVariable("qnaBoardNumber") Integer qnaBoardNumber
    ) {
        ResponseEntity<ResponseDto> response =
            qnaBoardService.deleteQnaBoard(userNumber, qnaBoardNumber);
        return response;
    }

    //* 6. 특정 게시물 검색 */
    @GetMapping("search/{searchWord}")
    public ResponseEntity<? super GetQnaBoardListResponseDto> searchQnaBoard(
        @PathVariable("searchWord") String searchWord
    ) {
        ResponseEntity<? super GetQnaBoardListResponseDto> response =
            qnaBoardService.getSearchQnaBoardList(searchWord);
        return response;
    }

} 


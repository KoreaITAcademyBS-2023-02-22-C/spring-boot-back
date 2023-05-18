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

import com.groupc.weather.dto.response.board.GetBoardFirstViewDto;
import com.groupc.weather.dto.response.board.GetBoardListResponseDto;
import com.groupc.weather.dto.response.board.GetBoardResponseDto;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.board.PatchBoardRequestDto;
import com.groupc.weather.dto.request.board.PostBoardRequestDto;
import com.groupc.weather.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/photoBoard")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    
    // 1. 게시물 작성
    @PostMapping("")
    public ResponseEntity<ResponseDto> postBoard(
            @Valid @RequestBody PostBoardRequestDto requestBody){
        ResponseEntity<ResponseDto> response = boardService.postBoard(requestBody);
        return response;
    }

    // 2. 특정게시물 조회
    @GetMapping("/{boardNumber}")
    public ResponseEntity<? super GetBoardResponseDto> getBoard(
            @PathVariable("boardNumber") Integer boardNumber) {
        ResponseEntity<? super GetBoardResponseDto> response = boardService.getBoard(boardNumber);
        return response;
    }

    // 3. 게시물 목록 조회(본인 작성)
    @GetMapping("/{userNumber}")
    public ResponseEntity<? super GetBoardListResponseDto> getBoardMyList(
        @PathVariable("userNumber") Integer userNumber){
        ResponseEntity<? super GetBoardListResponseDto> response = boardService.getBoardMyList(userNumber);
        return response;
    }

    // 4. TOP5 게시물 목록 조회
    @GetMapping("/top5")
    public ResponseEntity<? super GetBoardListResponseDto>getBoardtop5(){
        ResponseEntity<? super GetBoardListResponseDto> response = boardService.getBoardTop5();
        return response;
    }

    // 5. 일반게시물 목록 조회(최신순)
    @GetMapping("/list")
    public ResponseEntity<? super GetBoardListResponseDto> getBoard(){
        ResponseEntity<? super GetBoardListResponseDto> response = boardService.getBoardList();
        return response;
    }

    // 6. 첫화면 일반 게시물 목록
    // @GetMapping("/firstImage")
    // public ResponseEntity<? super GetBoardFirstViewDto> getBoardFirstView(){
    //     ResponseEntity<? super GetBoardFirstViewDto> response = boardService.getBoardList();
    //     return response;

    // }

    // 7. 특정 게시물 수정
    @PatchMapping("")
    public ResponseEntity<ResponseDto> patchBoard(
            @Valid @RequestBody Integer userNumber, PatchBoardRequestDto dto){
        ResponseEntity<ResponseDto> response = boardService.patchBoard(userNumber, dto);
        return response;
    }

    // 8 . 특정 게시물 삭제
    @DeleteMapping("/{userNumber}/{boardNumber}")
    public ResponseEntity<ResponseDto> deleteBoard(
        @PathVariable("userNumber") Integer userNumber,
        @PathVariable("boardNumber") Integer boardNumber
    ) {
        ResponseEntity<ResponseDto> response = boardService.deleteBoard(userNumber, boardNumber);
        return response;
    }


    // 9. 특정 게시물 좋아요 등록



    // 10. 특정 게시물 좋아요 해제



    // 11. 특정 유저 좋아요 게시물 조회



    // 12. 특정 게시물 검색
    @GetMapping("/{searchWord}/{weatherInfo}/{temperature}") // 이렇게 쓰는 게 맞는 건지..
    public ResponseEntity<? super GetBoardListResponseDto> searchListByWord(
        @PathVariable("searchWord") String searchWord
        // @PathVariable("weatherInfo") String weatherInfo,
        // @PathVariable("temperature") String temperature
    ) {
        ResponseEntity<? super GetBoardListResponseDto> response =
            boardService.getSearchListByWord(searchWord);
        return response;
    }
    
    

    // 13. 특정 게시물 검색(해쉬태그)








}

package com.groupc.weather.service;

import org.springframework.http.ResponseEntity;
import com.groupc.weather.dto.response.board.GetBoardListResponseDto;
import com.groupc.weather.dto.response.board.GetBoardResponseDto;
import com.groupc.weather.dto.response.board.GetBoardFirstViewDto;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.board.PatchBoardRequestDto;
import com.groupc.weather.dto.request.board.PostBoardRequestDto;
public interface BoardService {
    
    public ResponseEntity<ResponseDto> postBoard(PostBoardRequestDto dto);
    
    public ResponseEntity<? super GetBoardListResponseDto> getBoardMyList(Integer userNumber);
    public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber);
    public ResponseEntity<? super GetBoardListResponseDto> getBoardList();
    public ResponseEntity<? super GetBoardListResponseDto> getBoardTop5();
    public ResponseEntity<? super GetBoardFirstViewDto> getBoardFirstView();
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord();
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag();
    
    public ResponseEntity<ResponseDto> patchBoard(Integer userNumber, PatchBoardRequestDto dto);
    
    public ResponseEntity<ResponseDto> deleteBoard(Integer userNumber, Integer boardNumber);
}
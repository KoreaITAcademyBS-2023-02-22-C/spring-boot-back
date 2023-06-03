package com.groupc.weather.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groupc.weather.common.model.AuthenticationObject;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.board.DeleteCommentRequestDto;
import com.groupc.weather.dto.request.board.DeleteCommentRequestDto2;
import com.groupc.weather.dto.request.board.PatchCommentRequestDto;
import com.groupc.weather.dto.request.board.PatchCommentRequestDto2;
import com.groupc.weather.dto.request.board.PostCommentRequestDto;
import com.groupc.weather.dto.request.board.PostCommentRequestDto2;
import com.groupc.weather.service.CommentService;
import com.groupc.weather.service.CommentService2;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/comment")
public class CommentController2 {
    
    private final CommentService2 commentService2;
    

    //댓글 작성
    @PostMapping("/post")                                   
    public ResponseEntity<ResponseDto> postComment(     
    @AuthenticationPrincipal AuthenticationObject authenticationObject, @Valid @RequestBody PostCommentRequestDto2 requestBody){
        
        ResponseEntity<ResponseDto> response = commentService2.postComment(authenticationObject,requestBody);
        return response;
    }

    //댓글 수정
    @PatchMapping("/patch")
    public ResponseEntity<ResponseDto> patchComment( 
        @AuthenticationPrincipal AuthenticationObject authenticationObject, @Valid @RequestBody PatchCommentRequestDto2 requestBody)
        {
        
        ResponseEntity<ResponseDto> response = commentService2.patchComment(authenticationObject, requestBody);

        return response;
    }
    
    //댓글 삭제
    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto> deleteComment(
        @AuthenticationPrincipal AuthenticationObject authenticationObject, @Valid @RequestBody DeleteCommentRequestDto2 requestBody) 
        {
             
            ResponseEntity<ResponseDto> response = commentService2.deleteComment(authenticationObject, requestBody);

             return response;
        }
}

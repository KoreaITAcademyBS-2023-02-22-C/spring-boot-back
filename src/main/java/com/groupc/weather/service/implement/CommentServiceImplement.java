package com.groupc.weather.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groupc.weather.common.util.CustomResponse;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.board.PostCommentRequestDto;
import com.groupc.weather.entity.BoardEntity;
import com.groupc.weather.entity.CommentEntity;
import com.groupc.weather.entity.ManagerEntity;
import com.groupc.weather.entity.UserEntity;
import com.groupc.weather.repository.BoardRepository;
import com.groupc.weather.repository.CommentRepository;
import com.groupc.weather.repository.ManagerRepository;
import com.groupc.weather.repository.UserRepository;
import com.groupc.weather.service.CommentService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CommentServiceImplement implements CommentService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ManagerRepository managerRepository;
    private final CommentRepository commentRepository;

    @Override
    public ResponseEntity<ResponseDto> postComment(PostCommentRequestDto dto) {
    
        // 유저의 번호
        int writerUserNumber = dto.getCommentWriterNumber();
        // 관리자의 번호
        int managerNumber = dto.getManagerNumber();
        // 게시물의 번호 
        int boardNumber = dto.getBoardNumber();

        try {
            UserEntity existedWriterUserNumber = userRepository.findByUserNumber(writerUserNumber);
            ManagerEntity existedManagerNumber = managerRepository.findByManagerNumber(managerNumber);

            // 사용자가 존재하는지
            if(existedWriterUserNumber == null) return CustomResponse.notExistUserNumber();
            // 관리자가 존재하는지
            if(existedManagerNumber == null) return CustomResponse.notExistUserNumber();
            // 존재하는 게시물인지!
            BoardEntity existedBoardNumber = boardRepository.findByBoardNumber(boardNumber);
            if(existedBoardNumber == null) return CustomResponse.notExistBoardNumber();

            CommentEntity commentEntity = new CommentEntity(dto);
            //db저장
            commentRepository.save(commentEntity);
            //dto반환
            return CustomResponse.success();
            
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        
    }


    @Override
    public ResponseEntity<ResponseDto> patchComment(Integer userNumber, PostCommentRequestDto dto) {
            
            Integer usreNumber = dto.getCommentWriterNumber();
            Integer boardNumber = dto.getBoardNumber();

            try {
                //존재하지않는 게시물 반환
                BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
                if(boardEntity == null) return CustomResponse.notExistBoardNumber();

                //존재하지 않는 사용자
                UserEntity existedUserNumber = userRepository.findByUserNumber(usreNumber);
                if(existedUserNumber == null) return CustomResponse.notExistUserNumber();
                
            } catch (Exception exception) {
                exception.printStackTrace();
                return CustomResponse.databaseError();


            }
            
            return CustomResponse.success();
    }

    @Override
    public ResponseEntity<ResponseDto> deleteComment(Integer userNumber, Integer managerNumber, PostCommentRequestDto dto) { 

        Integer commentNumber = dto.getCommentWriterNumber();
        Integer boardNumber = dto.getBoardNumber();


        try {

            if(dto == null) {
                return CustomResponse.validationError();
            }

            //존재하지 않는 게시물 반환
            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if(boardEntity == null) return CustomResponse.notExistBoardNumber();
            //존재하지 않는 사용자 
            UserEntity existedUserNumber = userRepository.findByUserNumber(userNumber);
            if(existedUserNumber == null) return CustomResponse.notExistUserNumber();
            boolean existedManagerNumber = managerRepository.existsByManagerNumber(managerNumber);
            if(!existedManagerNumber) return CustomResponse.notExistManagerNumber();
            
            commentRepository.deleteByBoardNumber(commentNumber);


        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return CustomResponse.success();
        }
    }
    



        
        
        
        

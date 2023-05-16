package com.groupc.weather.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groupc.weather.common.util.CustomResponse;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.common.ManagerDto;
import com.groupc.weather.dto.request.common.UserDto;
import com.groupc.weather.dto.request.qnaBoard.PatchQnaCommentRequestDto;
import com.groupc.weather.dto.request.qnaBoard.PostQnaCommentRequestDto;
import com.groupc.weather.entity.ManagerEntity;
import com.groupc.weather.entity.QnaBoardEntity;
import com.groupc.weather.entity.QnaCommentEntity;

import com.groupc.weather.entity.UserEntity;
import com.groupc.weather.repository.ManagerRepository;
import com.groupc.weather.repository.QnaBoardRepository;
import com.groupc.weather.repository.QnaCommentRepository;
import com.groupc.weather.repository.UserRepository;
import com.groupc.weather.service.QnaCommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaCommentServiceImplement implements QnaCommentService {
    private final QnaBoardRepository qnaBoardRepository;
    private final UserRepository userRepositry;
    private final QnaCommentRepository qnaCommentRepository;
    private final ManagerRepository managerRepository;



    //qna 게시물 댓글 생성
    @Override
    public ResponseEntity<ResponseDto> postQnaComment(PostQnaCommentRequestDto dto) {
        ResponseDto body = null;
        // qna 댓글 작성자하고 싶은 사람 특정
        int qnaCommentWriterNumber = dto.getWriterNumber();
        // qna 보드 특정
        int qnaBoardNumber = dto.getQnaBoardNumber();
        // qna 글 작성자 특정
        int qnaBoardWriterNumber = qnaBoardRepository.findByBoardNumber(dto.getQnaBoardNumber()).getUserNumber();

        try {
            
            boolean existedWriterManagerNumber = managerRepository.existsByManagerNumber(qnaBoardWriterNumber);
            boolean existedWriterUserNumber = userRepositry.existsByUserNumber(qnaBoardWriterNumber);
            boolean existedQnaBoardNumber = qnaBoardRepository.existsByBoardNumber(qnaBoardNumber);

            // TODO: Qna 존재 유무
            if (!existedQnaBoardNumber) {
                return CustomResponse.notExistBoardNumber();
            }
            // TODO: 사용자 존재 유무 및 사용자 판단.
            if (!(existedWriterManagerNumber || existedWriterUserNumber)) {
                return CustomResponse.notExistUserNumber();
            }
            // TODO: 관리자가 아닌데 작성자도 아닐 경우
            if (!existedWriterManagerNumber) {
                if (!(qnaBoardWriterNumber == qnaCommentWriterNumber)) {
                    return CustomResponse.noPermissions();
                }
                // todo: 작성자가 맞을 경우
                UserEntity userEntity = userRepositry.findByUserNumber(dto.getWriterNumber());
                UserDto userDto = new UserDto();
                userDto.setUserNickname(userEntity.getNickname());
                userDto.setUserProfileImageUrl(userEntity.getProfileImageUrl());

                QnaCommentEntity qnaCommentEntity = new QnaCommentEntity(dto, userDto);
                qnaCommentRepository.save(qnaCommentEntity);

                return CustomResponse.success();
            }

            // TODO: 관리자일 경우
            ManagerEntity managerEntity = managerRepository.findByManagerNumber(dto.getWriterNumber());
            ManagerDto managerDto = new ManagerDto();
            managerDto.setManagerNickname(managerEntity.getManagerNickname());
            managerDto.setManagerProfileImageUrl(managerEntity.getProfileImageUrl());
            QnaCommentEntity qnaCommentEntity = new QnaCommentEntity(dto, managerDto);


            qnaCommentRepository.save(qnaCommentEntity);

            // TODO:

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return CustomResponse.success();
    }


    //qna 게시물 댓글 수정
    @Override
    public ResponseEntity<ResponseDto> patchQnaComment(PatchQnaCommentRequestDto dto) {
        ResponseDto body = null;
        // qna 댓글 수정하고 싶은 사람 특정
        int qnaCommentPatcherNumber = dto.getWriterNumber();
        // qna 댓글 중 수정하고자 하는 댓글 특정
        int qnaCommentNumber = dto.getQnaCommentNumber();
        // qna 댓글 작성자 특정
        int qnaCommentWriterNumber = qnaCommentRepository.findByQnaCommentNumber(dto.getQnaCommentNumber()).getUserNumber();
        // qna 댓글 보드 특정
        int qnaBoardNumber = dto.getQnaBoardNumber();
        QnaBoardEntity qnaBoardEntity = qnaBoardRepository.findByBoardNumber(qnaBoardNumber);

    
        // 유저일때랑 관리자일때랑 나눠서 진행
        
        try {            
            QnaCommentEntity qnaCommentEntity = qnaCommentRepository.findByQnaCommentNumber(dto.getQnaCommentNumber());
            boolean existedQnaBoardNumber = qnaBoardRepository.existsByBoardNumber(qnaBoardNumber);
            boolean existedQnaCommentNumber = qnaCommentRepository.existsByQnaCommentNumber(qnaCommentNumber);
            boolean existedPatcherManagerNumber = managerRepository.existsByManagerNumber(qnaCommentPatcherNumber);
            boolean existedPatcherUserNumber = userRepositry.existsByUserNumber(qnaCommentPatcherNumber);
            //TODO : 유저 존재 유무 확인 + 관리자인지 유저인지 확인한후
            if (!(existedPatcherManagerNumber || existedPatcherUserNumber)) {
                return CustomResponse.notExistUserNumber();
            }

            //TODO : 존재 하지 않는 게시물
            if (!existedQnaBoardNumber) {
                return CustomResponse.notExistBoardNumber();
            }
            //TODO : 존재 하지 않는 댓글
            if (!existedQnaCommentNumber){
                return CustomResponse.notExistQnaCommentNumber();
            }

            //TODO : 댓글 작성자와 수정자가 같을때

            if(!(qnaCommentPatcherNumber == qnaCommentWriterNumber)){
                    return CustomResponse.noPermissions(); }
                
            qnaCommentEntity.setContent(dto.getQnaCommentContent());
            //qnaBoardEntity.setReplyComplete(true);
            //qnaBoardRepository.save(qnaBoardEntity);
            qnaCommentRepository.save(qnaCommentEntity);
                
                   
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return CustomResponse.success();
    }





    // 댓글 삭제
    @Override
    public ResponseEntity<ResponseDto> deleteQnaComment(Integer userNumber, Integer qnaCommentNumber) {
        try {
            QnaCommentEntity qnaCommentEntity = qnaCommentRepository.findByQnaCommentNumber(qnaCommentNumber);
            //TODO : 존재하는 코멘트 넘버인지
            if (qnaCommentNumber == null) {
                return CustomResponse.notExistQnaCommentNumber();

            }
            
            //TODO : 존재하는 유저인지 확인
            boolean existedDeleterUserNumber = userRepositry.existsByUserNumber(userNumber);
            //TODO : 유저 존재 유무 확인 + 관리자인지 유저인지 확인한후
            if (!(existedDeleterUserNumber)) {
                return CustomResponse.notExistUserNumber();
            }
            //TODO : 삭제 권한이 있는지.
            //유저일 경우
            if(userNumber>=11){
            boolean equalQnaCommentWriter = qnaCommentEntity.getUserNumber().equals(userNumber);
                if (!equalQnaCommentWriter) {
                    return CustomResponse.noPermissions();
                }
                qnaCommentRepository.deleteByQnaCommentNumber(qnaCommentNumber);
                return CustomResponse.success();
            }
            //관리자일 경우
            qnaCommentRepository.deleteByQnaCommentNumber(qnaCommentNumber);
        
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();

        }
        return CustomResponse.success();
    }





    
}
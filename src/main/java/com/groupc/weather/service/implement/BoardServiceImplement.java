package com.groupc.weather.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.groupc.weather.dto.response.board.GetBoardFirstViewDto;
import com.groupc.weather.common.util.CustomResponse;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.board.BoardRequsetDto;
import com.groupc.weather.dto.request.board.PatchBoardRequestDto;
import com.groupc.weather.dto.request.board.PostBoardRequestDto;
import com.groupc.weather.dto.response.board.GetBoardListResponseDto;
import com.groupc.weather.dto.response.board.GetBoardResponseDto;
import com.groupc.weather.entity.BoardEntity;
import com.groupc.weather.entity.CommentEntity;
import com.groupc.weather.entity.HashListEntity;
import com.groupc.weather.entity.ImageUrlEntity;
import com.groupc.weather.entity.LikeyEntity;
import com.groupc.weather.entity.UserEntity;
import com.groupc.weather.entity.resultSet.BoardCommentLikeyCountResultSet;
import com.groupc.weather.entity.resultSet.GetBoardListResult;
import com.groupc.weather.repository.BoardRepository;
import com.groupc.weather.repository.CommentRepository;
import com.groupc.weather.repository.LikeyRepository;
import com.groupc.weather.repository.UserRepository;
import com.groupc.weather.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplement implements BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeyRepository likeyRepository;

    
    // 게시물 작성
    @Override
    public ResponseEntity<ResponseDto> postBoard(PostBoardRequestDto dto) {

        try {
            // 존재하지 않는 유저 번호
            boolean isexistUsernumber = userRepository.existsByUserNumber(dto.getUserNumber());
            if (!isexistUsernumber)
                {return CustomResponse.notExistUserNumber();}
                BoardEntity BoardEntity = new BoardEntity(dto);
                boardRepository.save(BoardEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return CustomResponse.success();
    }

    // 특정 게시물 조회 (게시물 번호)
    @Override
    public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber) {
        GetBoardResponseDto body = null;

        // try {
        //     // 매게변수 오류
        //     if (boardNumber == null)
        //         return CustomResponse.validationError();

        //     // 존재하지 않는 게시물 번호
        //     if (boardNumber == null)
        //         return CustomResponse.notExistBoardNumber();

        //     BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
        //     int viewCount = boardEntity.getViewCount();
        //     boardEntity.setViewCount(++viewCount);
        //     Integer boardWriterNumber = boardEntity.getUserNumber();
        //     UserEntity userEntity = userRepository.findByUserNumber(boardWriterNumber);
        //     List<LikeyEntity> likeyEntities = likeyRepository.findByBoardNumber(boardNumber);
        //     List<CommentEntity> commentEntities = commentRepository.findByBoardNumber(boardNumber);
        //     List<HashListEntity> hashListEntities = hashTageRepository.findByBoardNumber(boardNumber);
        //     List<ImageUrlEntity> imageUrlEntities = imageUrlRepository.findByBoardNumber(boardNumber);
        //     body = new GetBoardResponseDto(boardEntity, userEntity, likeyEntities, commentEntities, hashListEntities,
        //             imageUrlEntities);

        // } catch (Exception exception) {
        //     exception.printStackTrace();
        //     return CustomResponse.databaseError();
        // }
        return CustomResponse.success();
    }

    // 게시물 최신순 조회
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getBoardList() {
        GetBoardListResponseDto body = null;

        try {
            List<GetBoardListResult> resultSet = boardRepository.getBoardList();
            System.out.println(resultSet.size()); // 게시물 목록 몇개 나오는지 보는건데 쓸까 말까~?
            body = new GetBoardListResponseDto(resultSet);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // top5 조회
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getBoardTop5() {
        GetBoardListResponseDto body = null;
        try {
            List<GetBoardListResult> resultSet = boardRepository.getBoardListTop5();
            System.out.println(resultSet.size());
            body = new GetBoardListResponseDto(resultSet);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    // return에 coustom.success , ResposeEntity 쓸수도잇음.
    // ResponseEntity는 , OK 코드랑 메세지에다가 + 원하는거 보여줌. // 이거는 따로 만들면 Custom으로 쓸수잇는데
    // 따로 안만들어서 이렇게쓰는거임....


    // 본인 게시물 조회
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getBoardMyList(Integer userNumber) {
        GetBoardListResponseDto body = null;

        try {

            List<GetBoardListResult> resultSet = boardRepository.findByUserNumber(userNumber);
            System.out.println(resultSet.size());
            body = new GetBoardListResponseDto(resultSet);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    // 게시물 수정
    @Override
    public ResponseEntity<ResponseDto> patchBoard(Integer userNumber, PatchBoardRequestDto dto) {
        Integer userNumbers = dto.getBoardWriteUserNumber();
        Integer boardNumbers = dto.getBoardNumber();
        String boardTitle = dto.getBoardTitle();
        String boardImageUrl = dto.getBoardImageUrl();
        List<?> boardHashTag = dto.getBoardHashtag();

        try {

            UserEntity userEntity = userRepository.findByUserNumber(userNumbers);
            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumbers);
            // 매게변수
            if (userNumbers == null || boardNumbers == null) {
                return CustomResponse.validationError();
            }
            // 유저 번호 존재여부
            if (userEntity == null)
                return CustomResponse.notExistUserNumber();

            // 게시물 번호 존재여부
            if (boardEntity == null)
                return CustomResponse.notExistBoardNumber();

            // 유저번호와 게시물 작성자 유저번호 일치 여부 (불일치시 권한 없음)
            boolean isMatchedUserNumber = userEntity.equals(boardEntity.getUserNumber());
            if (!isMatchedUserNumber)
                return CustomResponse.noPermissions();

            boardEntity.setTitle(boardTitle);
            boardEntity.setBoardImageUrl(boardImageUrl);
            boardRepository.save(boardEntity);
            // hashTagRepository.save(boardHashTag);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return CustomResponse.success();
    }

    // 게시물 삭제
    @Override
    public ResponseEntity<ResponseDto> deleteBoard(Integer userNumber, Integer boardNumber) {
        try {
            // TODO 존재하지 않는 게시물 번호 반환
            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null)
                return CustomResponse.notExistBoardNumber();

            // TODO 존재 하지 않는 유저 이메일 반환
            boolean existedUserNumber = userRepository.existsByUserNumber(userNumber);
            if (!existedUserNumber)
                return CustomResponse.notExistUserNumber();

            // TODO 권한 x
            boolean equalsWriter = boardEntity.getUserNumber().equals(userNumber);
            if (!equalsWriter)
                return CustomResponse.noPermissions();

            commentRepository.deleteByBoardNumber(boardNumber);
            //likeyRepository.deleteByBoardNumber(boardNumber);
            boardRepository.delete(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return CustomResponse.success();
    }

    //첫화면 게시물 8개 보기
    @Override
    public ResponseEntity<? super GetBoardFirstViewDto> getBoardFirstView() {
    //     GetBoardListResponseDto body = null;
    //     try {
    //         List<GetBoardListResult> resultSet = boardRepository.getBoardFirstView();
    //         System.out.println(resultSet.size());
    //         body = new GetBoardListResponseDto(resultSet);
    //     } catch (Exception exception) {
    //         exception.printStackTrace();
    //         return CustomResponse.databaseError();
    //     }
        return ResponseEntity.status(HttpStatus.OK).body("dd"); //주석처리
    }

    // 특정 게시물 좋아요 등록


    // 특정 게시물 좋아요 해제


    // 특정 유저 좋아요 게시물 조회

   
    // 특정 게시물 검색
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(String searchWord) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSearchListByWord'");
    }
    
    // 특정 게시물 검색(해쉬태그)
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(String hashtag) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSearchListByHashtag'");
    }

}
    


    



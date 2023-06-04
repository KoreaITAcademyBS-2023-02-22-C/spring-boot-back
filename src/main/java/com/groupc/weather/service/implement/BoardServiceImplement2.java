package com.groupc.weather.service.implement;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import com.groupc.weather.dto.response.board.BoardFirstViewDto;
import com.groupc.weather.dto.response.board.BoardListResultDto;
import com.groupc.weather.dto.response.board.BoardListResultTop5Dto;
import com.groupc.weather.dto.response.board.GetBoardFirstViewDto;
import com.groupc.weather.common.model.AuthenticationObject;
import com.groupc.weather.common.util.CustomResponse;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.board.PatchBoardRequestDto;
import com.groupc.weather.dto.request.board.PostBoardRequestDto2;
import com.groupc.weather.dto.request.common.WeatherDto;
import com.groupc.weather.dto.response.board.GetBoardListResponseDto;
import com.groupc.weather.dto.response.board.GetBoardListResponsetop5Dto;
import com.groupc.weather.dto.response.board.GetBoardResponseDto;
import com.groupc.weather.dto.response.board.GetSearchListByWordResponseDto;
import com.groupc.weather.dto.response.board.LikeyListDto;
import com.groupc.weather.entity.BoardEntity;
import com.groupc.weather.entity.CommentEntity;
import com.groupc.weather.entity.HashTagEntity;
import com.groupc.weather.entity.HashtagHasBoardEntity;
import com.groupc.weather.entity.ImageUrlEntity;
import com.groupc.weather.entity.LikeyEntity;
import com.groupc.weather.entity.SearchLogEntity;
import com.groupc.weather.entity.UserEntity;
import com.groupc.weather.entity.primaryKey.LikeyPk;
import com.groupc.weather.entity.resultSet.GetBoardListResult;
import com.groupc.weather.repository.BoardRepository;
import com.groupc.weather.repository.CommentRepository;
import com.groupc.weather.repository.HashtagHasBoardRepository;
import com.groupc.weather.repository.HashtagRepository;
import com.groupc.weather.repository.ImageUrlRepository;
import com.groupc.weather.repository.LikeyRepository;
import com.groupc.weather.repository.SearchLogRepository;
import com.groupc.weather.repository.UserRepository;
import com.groupc.weather.service.BoardService2;
import com.groupc.weather.service.WeatherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplement2 implements BoardService2 {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeyRepository likeyRepository;
    private final ImageUrlRepository imageUrlRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagHasBoardRepository hashtagHasBoardRepository;
    private final SearchLogRepository searchLogRepository;
    private final WeatherService weatherService;

    
    // 게시물 작성
    @Override
    public ResponseEntity<ResponseDto> postBoard(AuthenticationObject authenticationObject, PostBoardRequestDto2 dto) {
        String email = authenticationObject.getEmail();
        boolean isManager = authenticationObject.isManagerFlag();

        try {
            // 존재하지 않는 유저 번호

            boolean isExistUserEmail = userRepository.existsByEmail(email);
            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            WeatherDto weatherDto =  weatherService.getWeatherData(dto.getLocation());
            if (!isExistUserEmail) {
  
                return CustomResponse.notExistUserNumber();
            } 

                BoardEntity boardEntity = new BoardEntity(dto,weatherDto,userNumber);
                boardRepository.save(boardEntity);
                int boardNumber = boardEntity.getBoardNumber();
                List<ImageUrlEntity> imageUrlLists = new ArrayList<>();

                for (String imageListResult: dto.getImageUrlList()) {
                    ImageUrlEntity imageUrlEntity = new ImageUrlEntity(imageListResult, boardEntity.getBoardNumber());
                    imageUrlLists.add(imageUrlEntity);
                }

                imageUrlRepository.saveAll(imageUrlLists); 

                List<HashTagEntity> hashtagEntityList = new ArrayList<>();
                for (String hashtag: dto.getHashtagList()){
                    HashTagEntity hashtagEntity = new HashTagEntity(hashtag);
                    hashtagEntityList.add(hashtagEntity);
                }
                hashtagRepository.saveAll(hashtagEntityList);

                List<HashtagHasBoardEntity> hashtagHasBoardEntityList = new ArrayList<>();
                for (HashTagEntity hashtagEntity: hashtagEntityList) {
                    int hashtagNumber = hashtagEntity.getHashtagNumber();
                    HashtagHasBoardEntity hashtagHasBoardEntity = new HashtagHasBoardEntity(hashtagNumber, boardNumber);
                    hashtagHasBoardEntityList.add(hashtagHasBoardEntity);
                }
                hashtagHasBoardRepository.saveAll(hashtagHasBoardEntityList);

                //  List<HashtagHasBoardEntity> hashtagHasBoardEntityList = new ArrayList<>();

                //  for (String hashtag: dto.getHashtagList()){
                //     HashtagEntity hashtagEntity = new HashtagEntity(hashtag);
                //     hashtagRepository.save(hashtagEntity);

                //     int hashtagNumber = hashtagEntity.getHashtagNumber();
                //     HashtagHasBoardEntity hashtagHasBoardEntity = new HashtagHasBoardEntity(hashtagNumber, boardNumber);

                //     hashtagHasBoardEntityList.add(hashtagHasBoardEntity);
                //  }

                //  hashtagHasBoardRepository.saveAll(hashtagHasBoardEntityList);

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

        try {
            // 매게변수 오류
            if (boardNumber == null)
                return CustomResponse.validationError();
            boolean existsByBoardNumber = boardRepository.existsByBoardNumber(boardNumber);
            // 존재하지 않는 게시물 번호
            if (!existsByBoardNumber)
                return CustomResponse.notExistBoardNumber();

            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
            int viewCount = boardEntity.getViewCount();
            boardEntity.setViewCount(++viewCount);
            Integer boardWriterNumber = boardEntity.getUserNumber();
            UserEntity userEntity = userRepository.findByUserNumber(boardWriterNumber);
            List<LikeyEntity> likeyEntities = likeyRepository.findByBoardNumberForLikeyList(boardNumber);
            List<LikeyListDto> likeyListDtos = new ArrayList<>();
            for(LikeyEntity likeyEntity :likeyEntities){
    
                UserEntity likeUserEntity = userRepository.findByUserNumber(likeyEntity.getUserNumber());
                LikeyListDto likeyListDto = new LikeyListDto(likeyEntity,likeUserEntity);
                likeyListDtos.add(likeyListDto);
            }



            List<CommentEntity> commentEntities = commentRepository.findByBoardNumber(boardNumber);
            List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
            
            List<HashTagEntity> hashListEntities = new ArrayList<>();
            for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                hashListEntities.add(hashtagEntity);
                }


            List<ImageUrlEntity> imageUrlEntities = imageUrlRepository.findByBoardNumber(boardNumber);
            body = new GetBoardResponseDto(boardEntity, userEntity, likeyListDtos, commentEntities, hashListEntities,
                    imageUrlEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
       // return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 게시물 최신순 조회
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getBoardList() {
        GetBoardListResponseDto body = null;

        try {
            List<GetBoardListResult> resultSet = boardRepository.getBoardList();
            //System.out.println(resultSet.size());  게시물 목록 몇개 나오는지 보는건데 쓸까 말까~?

            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                String boardFirstImageUrl=boardRepository.getBoardFirstImageUrl(boardNumber);
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }

               
               
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // top5 조회
    @Override
    public ResponseEntity<? super GetBoardListResponsetop5Dto> getBoardTop5() {
        GetBoardListResponsetop5Dto body = null;
        try {

            List<GetBoardListResult> resultSet = boardRepository.getBoardListTop5();
            List<BoardListResultTop5Dto> boardListResultTop5Dtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
        
                BoardListResultTop5Dto BoardListResultTop5Dto = new BoardListResultTop5Dto(result);
                boardListResultTop5Dtos.add(BoardListResultTop5Dto);
            }
            

            body = new GetBoardListResponsetop5Dto(boardListResultTop5Dtos);
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
    public ResponseEntity<? super GetBoardListResponseDto> getBoardMyList(String userEmail) {
        GetBoardListResponseDto body =  null;
        Integer userNumber = userRepository.findByEmail(userEmail).getUserNumber();
        boolean isExistUserEmail = userRepository.existsByEmail(userEmail);

        try {
            if (!isExistUserEmail) {
  
                return CustomResponse.notExistUserNumber();
            } 

            List<GetBoardListResult> resultSet = boardRepository.getMyBoardList(userNumber);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
        //return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 게시물 수정
    @Override
    public ResponseEntity<ResponseDto> patchBoard(String userEmail, PatchBoardRequestDto dto) {
        Integer userNumber = dto.getBoardWriteUserNumber();
        Integer boardNumber = dto.getBoardNumber();
        String boardTitle = dto.getBoardTitle();
        List<ImageUrlEntity> modifyImageUrlLists = dto.getImageUrlList();
        List<String> modifyHashTags = dto.getBoardHashtag();

        List<ImageUrlEntity> imageUrlEntities = new ArrayList<>();
        List<HashTagEntity> hashtagEntities = new ArrayList<>();
        // 로그인하면 토큰을 반환시켜주고 , 해당토큰을 헤더에 넣고 이걸 실행하면
        // 이메일이 받아와짐 왜냐면 컨트롤러에서 이메일을 받아오게 했기 때문에
        //
        try {
            UserEntity userEntity = userRepository.findByUserNumber(userNumber); // 작성자유저넘버 불러오기
            BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber); // 게시물번호 불러오기
// 매게변수
            if (boardNumber == null || boardNumber == null) {
                return CustomResponse.validationError();
            }
            // 유저 번호 존재여부
            if (userEntity == null)
                return CustomResponse.notExistUserNumber();

            // 게시물 번호 존재여부
            if (boardEntity == null)
                return CustomResponse.notExistBoardNumber();

            // 유저번호와 게시물 작성자 유저번호 일치 여부 (불일치시 권한 없음)
            UserEntity tryUserEntity = userRepository.findByEmail(userEmail); // 게시물의 수정을 시도하려는 사람의 정보
            Integer tryUserNumber = tryUserEntity.getUserNumber(); // 해당 정보에서 유저넘버를 가져옴
            boolean isMatchedUserNumber = tryUserNumber.equals(boardEntity.getUserNumber());
             // tryUserNumber = 수정시도하려는 유저넘버  / boardEntity.getUserNumber() = 게시물 작성자 넘버
            if (!isMatchedUserNumber)
                return CustomResponse.noPermissions();
            for (ImageUrlEntity imageList : modifyImageUrlLists) {
                ImageUrlEntity imageUrlEntity = new ImageUrlEntity(imageList, boardNumber);
                imageUrlEntities.add(imageUrlEntity);
            }
        for(String hashTagList : modifyHashTags){
                HashTagEntity hashtagEntity = new HashTagEntity(hashTagList);
                hashtagEntities.add(hashtagEntity);
        }

            hashtagRepository.saveAll(hashtagEntities);
            imageUrlRepository.saveAll(imageUrlEntities);
            boardEntity.setTitle(boardTitle);
            boardRepository.save(boardEntity);

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
        GetBoardFirstViewDto body = null;
    try {

        List<GetBoardListResult> resultSet = boardRepository.getBoardFirstView();
        List<BoardFirstViewDto> boardFirstViewDtos = new ArrayList<>();
        for(GetBoardListResult result:resultSet){
            int boardNumber = result.getBoardNumber();
            BoardFirstViewDto BoardFirstViewDto = new BoardFirstViewDto(result);
            boardFirstViewDtos.add(BoardFirstViewDto);
        }
        

        body = new GetBoardFirstViewDto(boardFirstViewDtos);
    } catch (Exception exception) {
        exception.printStackTrace();
        return CustomResponse.databaseError();
    }
    return ResponseEntity.status(HttpStatus.OK).body(body);
    }



    // 특정 게시물 좋아요 등록
    
    @Override
    public ResponseEntity<ResponseDto> likeBoard(LikeyPk likeyPk) {


        
        try {
            boolean isExistUsernumber = userRepository.existsByUserNumber(likeyPk.getUserNumber());
            if (!isExistUsernumber) return CustomResponse.notExistUserNumber();

            boolean isEixstBoardNumber = boardRepository.existsByBoardNumber(likeyPk.getBoardNumber());
            if (!isEixstBoardNumber) return CustomResponse.notExistBoardNumber();
            boolean isExistLikey = likeyRepository.existsById(likeyPk);
            if(isExistLikey) return CustomResponse.alreadyLikeBoard();


            LikeyEntity likeyEntity = new LikeyEntity(likeyPk.getUserNumber(),likeyPk.getBoardNumber());
            likeyRepository.save(likeyEntity);

        }
        catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }


        return CustomResponse.success();
    }



    // 특정 게시물 좋아요 해제

    @Override
    public ResponseEntity<ResponseDto> likeDeleteBoard(Integer userNumber, Integer boardNumber) {

        LikeyPk likeyPk = new LikeyPk(userNumber, boardNumber);
        
        try {
            boolean isExistUsernumber = userRepository.existsByUserNumber(userNumber);
            if (!isExistUsernumber) return CustomResponse.notExistUserNumber();

            boolean isEixstBoardNumber = boardRepository.existsByBoardNumber(boardNumber);
            if (!isEixstBoardNumber) return CustomResponse.notExistBoardNumber();
            boolean isExistLikey = likeyRepository.existsById(likeyPk);
            if(!isExistLikey) return CustomResponse.notLikeBoard();


            likeyRepository.deleteById(likeyPk);


        }
        catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return CustomResponse.success();
    }


    // 특정 유저 좋아요 게시물 조회
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getLikeBoardList(Integer userNumber) {
        GetBoardListResponseDto body = null;
        boolean isExistUsernumber = userRepository.existsByUserNumber(userNumber);

        try {
            
            if (!isExistUsernumber) return CustomResponse.notExistUserNumber();
            List<GetBoardListResult> resultSet = boardRepository.getLikeBoardList(userNumber);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
 
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

   
    // 특정 검색어 게시물 리스트 검색 - 비회원 
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(String searchWord) {
        GetBoardListResponseDto body = null;

        try {
            if (searchWord.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchListByWord(searchWord);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();

                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                }

                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 검색어 게시물 리스트 검색 - 회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(AuthenticationObject authenticationObject, String searchWord) {
        GetBoardListResponseDto body = null;
        String email = authenticationObject.getEmail();

        try {
            if (searchWord.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchListByWord(searchWord);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();

                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                }

                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }

            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, userNumber);
            searchLogRepository.save(searchLogEntity);

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 검색어 게시물 리스트 검색 (검색어 + 날씨) - 비회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(String searchWord, String weather) {
        GetBoardListResponseDto body = null;

        try {
            if (searchWord.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchListByWordAndWeather(searchWord, weather);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            
            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 검색어 게시물 리스트 검색 (검색어 + 날씨) - 회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(AuthenticationObject authenticationObject,
            String searchWord, String weather
    ) {
        GetBoardListResponseDto body = null;
        String email = authenticationObject.getEmail();

        try {
            if (searchWord.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchListByWordAndWeather(searchWord, weather);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            
            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, userNumber);
            searchLogRepository.save(searchLogEntity);
            
            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }


    // 특정 검색어 게시물 리스트 검색 (검색어 + 기온) - 비회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(String searchWord,
            Integer minTemperature, Integer maxTemperature
    ) {
        GetBoardListResponseDto body = null;

        try {
            if (searchWord.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchListByWordAndTemperatures(searchWord, minTemperature, maxTemperature);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            
            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 검색어 게시물 리스트 검색 (검색어 + 기온) - 회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(AuthenticationObject authenticationObject,
        String searchWord, Integer minTemperature, Integer maxTemperature
    ) {
        GetBoardListResponseDto body = null;
        String email = authenticationObject.getEmail();

        try {
            if (searchWord.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchListByWordAndTemperatures(searchWord, minTemperature, maxTemperature);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }

            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, userNumber);
            searchLogRepository.save(searchLogEntity);
            
            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 검색어 게시물 리스트 검색 (검색어 + 날씨 + 기온) - 비회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(String searchWord, String weather,
        Integer minTemperature, Integer maxTemperature
    ) {
        GetBoardListResponseDto body = null;

        try {
            if (searchWord.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchListByWordAndAll(searchWord, weather, minTemperature, maxTemperature);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            
            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 검색어 게시물 리스트 검색 (검색어 + 날씨 + 기온) - 회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByWord(AuthenticationObject authenticationObject, 
        String searchWord, String weather, Integer minTemperature, Integer maxTemperature
    ) {
        GetBoardListResponseDto body = null;
        String email = authenticationObject.getEmail();

        try {
            if (searchWord.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchListByWordAndAll(searchWord, weather, minTemperature, maxTemperature);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);
                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            
            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, userNumber);
            searchLogRepository.save(searchLogEntity);

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    
    // 특정 해시태그로 게시물 리스트 검색 - 비회원
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(String hashtag) {
        GetBoardListResponseDto body = null;

        try {
            if (hashtag.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchHashtagByWord(hashtag);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 해시태그로 게시물 리스트 검색 - 회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(
        AuthenticationObject authenticationObject, String hashtag
    ) {
        GetBoardListResponseDto body = null;
        String email = authenticationObject.getEmail();

        try {
            if (hashtag.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchHashtagByWord(hashtag);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            
            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            SearchLogEntity searchLogEntity = new SearchLogEntity(hashtag, userNumber);
            searchLogRepository.save(searchLogEntity);

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 해시태그로 게시물 리스트 검색(해시태그 + 날씨) - 비회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(String hashtag, String weather
        ) {
        GetBoardListResponseDto body = null;

        try {
            if (hashtag.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchHashtagByWordAndWeather(hashtag, weather);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 해시태그로 게시물 리스트 검색(해시태그 + 날씨) - 회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(AuthenticationObject authenticationObject,
        String hashtag, String weather
    ) {
        GetBoardListResponseDto body = null;
        String email = authenticationObject.getEmail();

        try {
            if (hashtag.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchHashtagByWordAndWeather(hashtag, weather);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }

            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            SearchLogEntity searchLogEntity = new SearchLogEntity(hashtag, userNumber);
            searchLogRepository.save(searchLogEntity);

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 해시태그로 게시물 리스트 검색(해시태그 + 기온) - 비회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(String hashtag, Integer minTemperature, Integer maxTemperature) {
        GetBoardListResponseDto body = null;

        try {
            if (hashtag.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchHashtagByWordAndTemperatures(hashtag, minTemperature, maxTemperature);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 해시태그로 게시물 리스트 검색(해시태그 + 기온) - 회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(AuthenticationObject authenticationObject, 
        String hashtag, Integer minTemperature, Integer maxTemperature
    ) {
        GetBoardListResponseDto body = null;
        String email = authenticationObject.getEmail();

        try {
            if (hashtag.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchHashtagByWordAndTemperatures(hashtag, minTemperature, maxTemperature);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }

            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            SearchLogEntity searchLogEntity = new SearchLogEntity(hashtag, userNumber);
            searchLogRepository.save(searchLogEntity);

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 해시태그로 게시물 리스트 검색(해시태그 + 날씨 + 기온) - 비회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(String hashtag, String weather, Integer minTemperature, Integer maxTemperature) {
        GetBoardListResponseDto body = null;
        
        try {
            if (hashtag.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchHashtagByWordAndAll(hashtag, weather, minTemperature, maxTemperature);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 해시태그로 게시물 리스트 검색(해시태그 + 날씨 + 기온) - 회원
    public ResponseEntity<? super GetBoardListResponseDto> getSearchListByHashtag(AuthenticationObject authenticationObject,
        String hashtag, String weather, Integer minTemperature, Integer maxTemperature
    ) {
        GetBoardListResponseDto body = null;
        String email = authenticationObject.getEmail();

        try {
            if (hashtag.isBlank()) return CustomResponse.validationError();

            List<GetBoardListResult> resultSet = boardRepository.getSearchHashtagByWordAndAll(hashtag, weather, minTemperature, maxTemperature);
            List<BoardListResultDto> boardListResultDtos = new ArrayList<>();
            for(GetBoardListResult result:resultSet){
                int boardNumber = result.getBoardNumber();
                List<HashtagHasBoardEntity> hashtagHasBoardEntities = hashtagHasBoardRepository.findByBoardNumber(boardNumber);

                List<HashTagEntity> hashListEntities = new ArrayList<>();
                for(HashtagHasBoardEntity hashtagHasBoardEntity : hashtagHasBoardEntities){
                    int hashtagNumber = hashtagHasBoardEntity.getHashtagNumber();
                    HashTagEntity hashtagEntity = hashtagRepository.findByHashtagNumber(hashtagNumber);
                    hashListEntities.add(hashtagEntity);
                    }
                BoardListResultDto boardListResultDto = new BoardListResultDto(result, hashListEntities);
                boardListResultDtos.add(boardListResultDto);
            }
            Integer userNumber = userRepository.findByEmail(email).getUserNumber();
            SearchLogEntity searchLogEntity = new SearchLogEntity(hashtag, userNumber);
            searchLogRepository.save(searchLogEntity);

            body = new GetBoardListResponseDto(boardListResultDtos);
        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }


}







package com.groupc.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.groupc.weather.entity.BoardEntity;
import com.groupc.weather.entity.resultSet.BoardCommentLikeyCountResultSet;
import com.groupc.weather.entity.resultSet.BoardCommentResultSet;
import com.groupc.weather.entity.resultSet.GetBoardListResult;
import com.groupc.weather.entity.resultSet.HashTagResultSet;
import com.groupc.weather.entity.resultSet.LikeyResultSet;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

    public BoardEntity findByBoardNumber(int boardNumber);

    public List<GetBoardListResult> findByBoardWriterNumber(Integer userNumber);

    @Query(value =
    "SELECT " + 
    "B.board_number AS boardNumber, " +  
    "C.comment_number AS commentNumber, " +
    "C.user_number AS userNumber, " +  
    "C.content AS commentContent, " + 
    "C.write_datetime AS commentDatetime, " + 
    "C.user_nickname AS commentUserNickname, " +
    "C.user_profile_image_url AS commentUserProfileImageUrl, "+ 
    "FROM Board B, Comment C " +
    "Where B.board_number = C.board_number" +
    "GROUP BY B.board_number " +
    "ORDER BY C.write_datetime DESC",
    nativeQuery = true
)
public List<BoardCommentResultSet> getBoardCommentList();


@Query(value =
"SELECT " + 
"B.board_number AS boardNumber, " +
"B.title AS boardTitle, " +
"B.content AS boardContent, " +
"B.write_datetime AS boardWriterDatetime, " +
"B.weather_info AS weatherInfo, " +
"B.temperature AS temperature, " +
"B.view_count AS viewCount, " +
"U.nickname AS writerNickname, " +
"U.profile_image_url AS writerProfileImageUrl, " +
"count(DISTINCT C.comment_number) AS commentCount, " +
"count(DISTINCT L.user_number) AS likeCount " +
"FROM Board B, Comment C, Likey L, User U "  +
"Where B.board_number= C.board_number, " +
"AND B.user_number = U.user_number " +
"AND B.board_number = L.board_number" +
"GROUP BY B.board_number ",
nativeQuery = true
)
public List<BoardCommentLikeyCountResultSet> getBoardCommentLikeyList();


@Query(value =
"SELECT " + 
"B.board_number AS boardNumber, " +
"L.user_number AS userNumber, " +
"L.user_nickname AS userNickname, " +
"L.user_profile_image_url AS commentUserProfileImageUrl, "+
"GROUP BY B.board_number ",
nativeQuery = true
)
public List<LikeyResultSet> getLikeList();


@Query(value =
"SELECT " + 
"B.board_number AS boardNumber, " +
"H.hashtag_number AS hashtagNumber, " +
"H.hashtage_content AS hashTageContent, " +
"GROUP BY B.board_number ",
nativeQuery = true
)
public List<HashTagResultSet> getHashTagList();

// 쿼리문 작성하기!!!

public List<GetBoardListResult> getBoardList();

// 쿼리문 작성하기!!! List는 ORDER BY boardWriteDatetime DESC
// Top5는  LIST 쿼리문에서  + ORDER BY viewCount , LIMIT 5 하면댐
public List<GetBoardListResult> getBoardListTop5();


// 본인작성 게시물 목록 쿼리문
public List<GetBoardListResult> getBoardMyList();

//첫화면 8개게시물
// top 5 에서 limit을 8개로 바꾸고 , 화면 첫 사진만 보고 게시물 번호만 이두개만 나타냄
// boardNumber , boardFisrtImageUrl , Limit 8 , ORDER BY writeDateTime DESC 

public List<GetBoardListResult> getBoardFirstView();

}
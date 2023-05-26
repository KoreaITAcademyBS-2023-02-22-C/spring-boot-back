package com.groupc.weather.dto.response.user;

import java.util.List;

import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.entity.BoardEntity;
import com.groupc.weather.entity.CommentEntity;
import com.groupc.weather.entity.FollowingEntity;
import com.groupc.weather.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponseDto extends ResponseDto {
    private String userName;
    private int userNumber;
    private String userNickname;
    private String userProfileImageUrl;
    private String userGender;
    private List<Follower> userFollowerList;
    private int userFollowerCount;
    private List<Following> userFollowingList;
    private int userFollowingCount;
    private int boardCount;
    private int commentCount;

    // public GetUserResponseDto(
    // UserEntity userEntity, BoardEntity boardEntity, CommentEntity commentEntity,
    // List<FollowingEntity> followingEntities, List<FollowerEntity>
    // followerEntities) {
    // super("SU", "Success");

    // this.userNumber = userEntity.getUserNumber();
    // this.userNickname = userEntity.getNickname();
    // this.userProfileImageUrl = userEntity.getProfileImageUrl();
    // this.userGender = userEntity.getGender();
    // this.boardCount = boardEntity.getViewCount();
    // this.commentCount = commentEntity.getCommentCount();

    // this.userFollowerList = Follower.createList(followerEntities);
    // this.userFollowerCount = followerEntities.getFollowerCount();
    // this.userFollowingList = Following.createList(followingEntities);
    // this.userFollowingCount = followingEntities.getFollowingCount();
    // }

    public GetUserResponseDto(
            UserEntity userEntity) {
        super("SU", "Success");

        this.userName = userEntity.getName();
        this.userNumber = userEntity.getUserNumber();
        this.userNickname = userEntity.getNickname();
        this.userProfileImageUrl = userEntity.getProfileImageUrl();
        this.userGender = userEntity.getGender();
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class BoardCount {
    private int userNumber;
    private int boardNumber;

    BoardCount(BoardEntity boardEntity, UserEntity userEntity) {
        this.userNumber = userEntity.getUserNumber();
        this.boardNumber = boardEntity.getBoardNumber();
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class CommentCount {
    private int userNumber;
    private int commentNumber;

    CommentCount(CommentEntity commentEntity, UserEntity userEntity) {
        this.userNumber = userEntity.getUserNumber();
        this.commentNumber = commentEntity.getBoardNumber();
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Follower {
    private int followerUserNumber;
    private String followerNickname;
    private String followerProfileImageUrl;

    Follower(FollowerEntity followerEntity) {
        this.followerUserNumber = followerEntity.getFollowerUserNumber();
        this.followerNickname = followerEntity.getFollowerNickname();
        this.followerProfileImageUrl = followerEntity.getFollowerProfileImageUrl();
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Following {
    private int followingUserNumber;
    private String followingNickname;
    private String followingProfileImageUrl;

    Following(FollowingEntity followingEntity) {
        this.followingUserNumber = followingEntity.getFollowingUserNumber();
        this.followingNickname = followingEntity.getFollowingNickname();
        this.followingProfileImageUrl = followingEntity.getFollowingProfileImageUrl();
    }
}

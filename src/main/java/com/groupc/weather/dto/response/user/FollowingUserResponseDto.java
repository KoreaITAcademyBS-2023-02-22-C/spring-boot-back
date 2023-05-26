package com.groupc.weather.dto.response.user;

import java.util.ArrayList;
import java.util.List;

import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.entity.FollowingEntity;
import com.groupc.weather.entity.resultSet.GetFollowingListResultSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class FollowingUserResponseDto extends ResponseDto {
    private List<FollowingEntity> followingUserList;

    public FollowingUserResponseDto(List<FollowingEntity> getFollowingListResultSet) {

        super("SU", "Success");

        this.followingUserList = getFollowingListResultSet;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class FollowingList {
        private Integer userNumber;
        private String nickname;
        private String profileImageUrl;
        private int followingCount;

        public FollowingList(GetFollowingListResultSet getFollowingListResultSet) {
            this.userNumber = getFollowingListResultSet.getUserNumber();
            this.nickname = getFollowingListResultSet.getUserNickname();
            this.profileImageUrl = getFollowingListResultSet.getUserProfileImageUrl();
            this.followingCount = getFollowingListResultSet.getFollowingCount();
        }
    }
}

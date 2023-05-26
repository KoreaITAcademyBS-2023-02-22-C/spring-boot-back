package com.groupc.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.groupc.weather.entity.FollowingEntity;
import com.groupc.weather.entity.primaryKey.FollowingPk;
import com.groupc.weather.entity.resultSet.GetFollowerListResultSet;
import com.groupc.weather.entity.resultSet.GetFollowingListResultSet;
//import com.groupc.weather.entity.resultSet.GetTop5FollowerListResult;

@Repository
public interface FollowRepository extends JpaRepository<FollowingEntity, FollowingPk> {

        // public List<GetTop5FollowerListResult> getTop5ListBy();

        // public List<GetFollowingListResultSet> findByFollowingNumber(Integer
        // followingNumber);

        public List<FollowingEntity> findByFollowingNumber(Integer followingNumber);

        // 특정 유저 조회시 유저의 이름, 번호, URL, 유저에 대한 follower 카운트가 뜨는 쿼리문.
        // @Query(value = "SELECT " +
        // "U.user_number AS UserNumber," +
        // "U.nickname AS UserNickname," +
        // "U.profile_image_url AS UserProfileImageUrl, " +
        // "count(DISTINCT F.follower_number) AS FollowerCount " +
        // "FROM User U, Following F " +
        // "WHERE U.user_number = F.following_number " +
        // "AND U.user_number = :following_number " +
        // "GROUP BY U.user_number " +
        // "ORDER BY FollowerCount Desc ", nativeQuery = true)

        @Query(value = "SELECT " +
                        "U.user_number AS UserNumber," +
                        "U.nickname AS UserNickname," +
                        "U.profile_image_url AS UserProfileImageUrl, " +
                        "F.follower_number AS FollowerNumber " +
                        "FROM User U, Following F " +
                        "WHERE U.user_number = F.following_number " +
                        "AND U.user_number = :following_number ", nativeQuery = true)
        public List<GetFollowerListResultSet> getFollowerUserList(@Param("following_number") Integer followingNumber);

        @Query(value = "SELECT " +
                        "U.user_number AS UserNumber," +
                        "U.nickname AS UserNickname," +
                        "U.profile_image_url AS UserProfileImageUrl, " +
                        "count(DISTINCT F.following_number) AS FolloingCount " +
                        "FROM User U, Following F " +
                        "WHERE U.user_number = F.follower_number " +
                        "AND U.user_number = :follower_number " +
                        "GROUP BY U.user_number " +
                        "ORDER BY FolloingCount Desc ", nativeQuery = true)
        public List<GetFollowingListResultSet> getFollowingUserList(@Param("follower_number") Integer followerNumber);
}

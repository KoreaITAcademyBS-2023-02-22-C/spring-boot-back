package com.groupc.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupc.weather.entity.FollowEntity;
import com.groupc.weather.entity.primaryKey.FollowingPk;
import com.groupc.weather.entity.resultSet.GetTop5FollowerListResult;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, FollowingPk> {

    // Top5 받아오는 쿼리 추가.

    // public List<GetTop5FollowerListResult> getTop5ListBy();
}

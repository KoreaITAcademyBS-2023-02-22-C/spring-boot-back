package com.groupc.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupc.weather.entity.FollowingEntity;

public interface FollowingRepository extends JpaRepository<FollowingEntity, Integer> {

}

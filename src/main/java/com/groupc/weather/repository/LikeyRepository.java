package com.groupc.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupc.weather.entity.LikeyEntity;
import com.groupc.weather.entity.primaryKey.LikeyPk;
@Repository
public interface LikeyRepository extends JpaRepository<LikeyEntity, LikeyPk> {
    
}

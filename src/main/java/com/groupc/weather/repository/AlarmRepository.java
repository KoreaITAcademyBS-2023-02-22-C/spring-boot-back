package com.groupc.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupc.weather.entity.AlarmEntity;
@Repository
public interface AlarmRepository extends JpaRepository<AlarmEntity,Integer>  {
    
    public List<AlarmEntity> getAlarmEntities();
}

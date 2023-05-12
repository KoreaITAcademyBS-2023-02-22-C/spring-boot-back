package com.groupc.weather.service;

import org.springframework.http.ResponseEntity;

import com.groupc.weather.dto.response.alarm.GetAlarmListResponseDto;

public interface AlarmService {

    public ResponseEntity<? super GetAlarmListResponseDto> getBoardList();

    
}

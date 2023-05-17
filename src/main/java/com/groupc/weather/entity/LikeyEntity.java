package com.groupc.weather.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.groupc.weather.entity.primaryKey.LikeyPk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="likey")
@Table(name = "likey")
@IdClass(LikeyPk.class)
public class LikeyEntity {
    @Id
    private int boardNumber;     
    @Id
    private int userNumber;
    private boolean like;  // 좋아요를 눌렀을 경우 등록되고 한번 더 누르면 취소가 될 때 사용!?

}


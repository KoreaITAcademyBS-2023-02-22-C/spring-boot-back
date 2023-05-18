package com.groupc.weather.entity;

import com.groupc.weather.dto.request.board.PostBoardRequestDto;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Board")
@Table(name="Board")
public class BoardEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer boardNumber;
        private Integer userNumber;
        private String title;
        private String content;
        private String boardImageUrl;
        private String writeDatetime;
        private int temperature;
        private String weatherInfo;
        private int viewCount;

        public BoardEntity(PostBoardRequestDto dto) {
                Date now = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String writeDatetime = simpleDateFormat.format(now);

                this.userNumber = dto.getUserNumber();
                this.title = dto.getTitle();
                this.content = dto.getContent();
                this.boardImageUrl = dto.getImageUrl();
                this.weatherInfo = writeDatetime;
                this.viewCount = 0;
        }

}

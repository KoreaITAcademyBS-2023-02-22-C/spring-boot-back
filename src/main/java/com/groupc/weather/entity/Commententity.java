package com.groupc.weather.entity;

import com.groupc.weather.dto.request.board.PostCommentRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {

    private int commentNumber;
    private int boardNumber;
    private int userNumber;
    private String commentContent;
    private String writeDatetime;
    private String userNickname;
    private String userImageProfileUrl;
    private Integer managerNumber;
    private String managerProfileUrl;

    public CommentEntity(PostCommentRequestDto dto, UserEntity userEntity, ManagerEntity managerEntity) {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String writeDatetime = simpleDateFormat.format(now);
        
        this.managerProfileUrl = null;
        this.managerNumber = null;

        this.userImageProfileUrl =dto.getUserImageProfileUrl();
        this.userNickname = dto.();
        this.boardNumber = dto.getBoardNumber();
        this.userNumber2 = dto.getWriterNumber();
        this.commentContent = dto.getCommentContent();
        this.writeDatetime = writeDatetime;
}
}
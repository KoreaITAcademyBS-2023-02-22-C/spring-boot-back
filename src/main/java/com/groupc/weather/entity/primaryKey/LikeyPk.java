package com.groupc.weather.entity.primaryKey;

import java.io.Serializable;
import javax.persistence.Column;
import lombok.Data;

@Data

public class LikeyPk implements Serializable {

    @Column(name = "boardNumber")
    private int boardNumber;
    @Column(name = "userNumber")
    private String userNumber;
    
}

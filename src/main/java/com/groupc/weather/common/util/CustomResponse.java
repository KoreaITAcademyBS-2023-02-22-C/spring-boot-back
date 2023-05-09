package com.groupc.weather.common.util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.groupc.weather.dto.response.ResponseDto;

public class CustomResponse {

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto body = new ResponseDto("SU", "SUCCESS");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
    
    public static ResponseEntity<ResponseDto> databaseError() {

       ResponseDto errorBody = new ResponseDto("DE", "Database Error");    
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody); 
    } 

    public static ResponseEntity<ResponseDto> validationError() {
       
        ResponseDto errorBody = new ResponseDto("VF","Request Parameter Validation Failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
}


public static ResponseEntity<ResponseDto> existUserEmail() {
       
    ResponseDto errorBody = new ResponseDto("EU","Not-Existent User Email");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
}

public static ResponseEntity<ResponseDto> existUserNickname() {
       
    ResponseDto errorBody = new ResponseDto("EM","Not-Existent User Nickname");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
}

public static ResponseEntity<ResponseDto> existUserPhoneNumber() {
       
    ResponseDto errorBody = new ResponseDto("EP","Not-Existent User PhoneNumber");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
}

    public static ResponseEntity<ResponseDto> notExistBoardNumber() {
        
        ResponseDto errorBody = new ResponseDto("NB", "Not-Existent Board Number");        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> signInFailed() {
        
        ResponseDto errorBody = new ResponseDto("SF", "Sign In Failed");        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    public static ResponseEntity<ResponseDto> notExistUserEmail() {
        
        ResponseDto errorBody = new ResponseDto("NU", "Not-Existent User Email");        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody); //너가 누구인지 모른다
    }

    public static ResponseEntity<ResponseDto> signUpFailed() {
        
        ResponseDto errorBody = new ResponseDto("SF", "Sign In Failed");        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }
    
    public static ResponseEntity<ResponseDto> noPermissions() {
        
        ResponseDto errorBody = new ResponseDto("NT", "No Permissions");        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody); //너가 누구인지는 알지만 권한이없음
    }
}
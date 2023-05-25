package com.groupc.weather.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.groupc.weather.common.util.CustomResponse;
import com.groupc.weather.dto.ResponseDto;
import com.groupc.weather.dto.request.follow.FollowRequestDto;
import com.groupc.weather.dto.request.user.DeleteUserRequestDto;
import com.groupc.weather.dto.request.user.FindByEmailRequestDto;
import com.groupc.weather.dto.request.user.FindByPasswordRequestDto;
import com.groupc.weather.dto.request.user.LoginUserRequestDto;
import com.groupc.weather.dto.request.user.PatchUserRequestDto;
import com.groupc.weather.dto.request.user.PostUserRequestDto;
import com.groupc.weather.dto.response.user.FindByEmailResponseDto;
import com.groupc.weather.dto.response.user.FindByPasswordResponseDto;
import com.groupc.weather.dto.response.user.GetTop5FollowerResponseDto;
import com.groupc.weather.dto.response.user.GetUserResponseDto;
import com.groupc.weather.dto.response.user.LoginUserResponseDto;
import com.groupc.weather.entity.BoardEntity;
import com.groupc.weather.entity.CommentEntity;
import com.groupc.weather.entity.FollowEntity;
import com.groupc.weather.entity.UserEntity;
import com.groupc.weather.entity.resultSet.GetTop5FollowerListResult;
import com.groupc.weather.provider.JwtProvider;
import com.groupc.weather.repository.BoardRepository;
import com.groupc.weather.repository.CommentRepository;
import com.groupc.weather.repository.FollowRepository;
import com.groupc.weather.repository.UserRepository;
import com.groupc.weather.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private UserRepository userRepository;
    private FollowRepository followRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplement(
            UserRepository userRepository,
            FollowRepository followRepository,
            JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.followRepository = followRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // 유저 service 관리자 service 똑같은 기능 따로 만들어서 해도 된다하심.
    // 유저 등록
    @Override
    public ResponseEntity<ResponseDto> postUser(PostUserRequestDto dto) {

        String email = dto.getUserEmail();
        String nickname = dto.getUserNickname();
        String phoneNumber = dto.getUserPhoneNumber();
        String password = dto.getUserPassword();

        try {
            // 이메일 중복 반환.
            boolean hasEmail = userRepository.existsByEmail(email);
            if (hasEmail)
                return CustomResponse.existUserEmail();
            // 닉네임 중복 반환
            boolean hasNickname = userRepository.existsByNickname(nickname);
            if (hasNickname)
                return CustomResponse.existUserNickname();
            // 핸드폰 번호 중복 반환
            boolean hasPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
            if (hasPhoneNumber)
                return CustomResponse.existUserPhoneNumber();

            // 패스워드 암호화
            String encodedPassword = passwordEncoder.encode(password);
            dto.setUserPassword(encodedPassword);

            // 유저 레코드 삽입
            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

        } catch (Exception exception) {
            // 데이터베이스 오류
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return CustomResponse.success();

    }

    // 유저 로그인
    @Override
    public ResponseEntity<? super LoginUserResponseDto> LoginUser(LoginUserRequestDto dto) {

        LoginUserResponseDto body = null;

        String email = dto.getUserEmail();
        String password = dto.getUserPassword();

        try {
            // 로그인 실패 반환. ( 이메일 )
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null)
                return CustomResponse.signInFailedEmail();

            // 로그인 실패 반환. ( 패스워드 )
            String encordedPassword = userEntity.getPassword();
            boolean equaledPassword = passwordEncoder.matches(password, encordedPassword);
            if (!equaledPassword)
                return CustomResponse.signInFailedPassword();

            String jwt = jwtProvider.create(email);
            body = new LoginUserResponseDto(jwt);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 유저 이메일 찾기
    @Override
    public ResponseEntity<? super FindByEmailResponseDto> FindByEmail(FindByEmailRequestDto dto) {

        FindByEmailResponseDto body = null;

        String name = dto.getUserName();
        String phoneNumber = dto.getUserPhoneNumber();

        try {
            // 존재하지 않는 이름 반환.
            UserEntity userEntity = userRepository.findByName(name);
            if (userEntity == null)
                return CustomResponse.undifindeUsername();

            // 존재하지 않는 폰 번호 반환.
            boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
            if (!existsByPhoneNumber)
                return CustomResponse.undifindephonenumber();

            // 해당하는 이메일 반환.
            String userEmail = userEntity.getEmail();
            body = new FindByEmailResponseDto(userEmail);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 유저 비밀번호 찾기 (encoding 복호화 기능)
    @Override
    public ResponseEntity<? super FindByPasswordResponseDto> FindByPassword(FindByPasswordRequestDto dto) {

        FindByPasswordResponseDto body = null;

        String email = dto.getUserEmail();
        String phoneNumber = dto.getUserPhoneNumber();

        try {
            // 존재하지 않는 이메일 반환.
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null)
                return CustomResponse.undifindeEmail();

            // 존재하지 않는 폰 번호 반환.
            boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
            if (!existsByPhoneNumber)
                return CustomResponse.undifindephonenumber();

            // 해당하는 비밀번호 반환.
            String userPassword = userEntity.getPassword();
            body = new FindByPasswordResponseDto(userPassword);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 유저 정보 수정
    @Override
    public ResponseEntity<ResponseDto> patchUser(PatchUserRequestDto dto) {

        Integer userNumber = dto.getUserNumber();
        String userEmail = dto.getUserEmail();
        String userPassword = dto.getUserPassword();
        String userNickname = dto.getUserNickname();
        String userPhoneNumber = dto.getUserPhoneNumber();
        String userAddress = dto.getUserPassword();
        String userProfileImageUrl = dto.getUserProfileImageUrl();
        String userGender = dto.getUserGender();
        String userBirthDay = dto.getUserBirthday();

        try {
            // 존재하지 않는 유저 번호 반환
            UserEntity userEntity = userRepository.findByUserNumber(userNumber);
            if (userEntity == null)
                return CustomResponse.undifindUserNumber();

            // 권한 없음
            boolean equalWriter = userEntity.getEmail().equals(userEmail);
            if (!equalWriter)
                return CustomResponse.noPermissions();

            userEntity.setEmail(userEmail);
            String encodedPassword = passwordEncoder.encode(userPassword);
            userEntity.setPassword(encodedPassword);
            userEntity.setNickname(userNickname);
            userEntity.setPhoneNumber(userPhoneNumber);
            userEntity.setAddress(userAddress);
            userEntity.setProfileImageUrl(userProfileImageUrl);
            userEntity.setGender(userGender);
            userEntity.setBirthday(userBirthDay);

            userRepository.save(userEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return CustomResponse.success();
    }

    // 유저 정보 삭제
    @Override
    public ResponseEntity<ResponseDto> deleteUser(DeleteUserRequestDto dto) {

        Integer userNumber = dto.getUserNumber();
        String password = dto.getUserPassword();

        try {

            // 존재하지 않는 유저 번호 반환.
            UserEntity userEntity = userRepository.findByUserNumber(userNumber);
            if (userEntity == null)
                return CustomResponse.undifindUserNumber();

            // 일치하지 않는 비밀번호 반환.
            String encordedPassword = userEntity.getPassword();
            boolean equaledPassword = passwordEncoder.matches(password, encordedPassword);
            if (!equaledPassword)
                return CustomResponse.signInFailedPassword();

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return CustomResponse.success();
    }

    // 특정 유저 조회
    public ResponseEntity<? super GetUserResponseDto> getUser(Integer userNumber) {

        GetUserResponseDto body = null;

        try {
            // 존재하지 않는 조회하려는 유저 반환.
            UserEntity userEntity = userRepository.findByUserNumber(userNumber);
            if (userEntity == null)
                return CustomResponse.notExistUserNumber();

            // 리스트 하나로 묶고 쓰자. // follow / board / comment 는 작업중이라서 나중에 테스트.
            // List<FollowerEntity> followerEntities =
            // followerRepository.findFollowerList(userNumber);
            // List<FollowEntity> followingEntities =
            // followingRepository.findFollowingList(userNumber);
            // BoardEntity boardEntity = boardRepository.findByUserNumber(userNumber);
            // CommentEntity commentEntity = commentRepository.findByUserNumber(userNumber);

            userEntity = userRepository.findByUserNumber(userNumber);

            // FollowerEntity 없어서 오류 발생.
            // body = new GetUserResponseDto(userEntity, boardEntity, commentEntity,
            // followingEntities, null);
            body = new GetUserResponseDto(userEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 유저 팔로우
    @Override
    public ResponseEntity<ResponseDto> followUser(FollowRequestDto dto) {

        ResponseDto responseBody = null;

        Integer followerNumber = dto.getFollowerNumber();
        Integer followingNumber = dto.getFollowingNumber();

        try {

            // 존재하지 않는 팔로우하려는 유저 반환.
            // 유저 목록에 존재하는 유저인가.
            UserEntity userEntity = userRepository.findByUserNumber(followingNumber);
            if (userEntity == null)
                return CustomResponse.undifindUserNumber();

            // follow Entity에 입력한 데이터 저장.
            FollowEntity followEntity = new FollowEntity(dto);
            followRepository.save(followEntity);

            responseBody = new ResponseDto("SU", "Success");

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
        return CustomResponse.success();
    }

    // 팔로우 해제

    // Top5 팔로우 유저 조회
    @Override
    public ResponseEntity<? super GetTop5FollowerResponseDto> getFollowerTop5() {

        GetTop5FollowerResponseDto body = null;

        try {

            List<GetTop5FollowerListResult> resultSet = userRepository.getTop5ListBy();
            body = new GetTop5FollowerResponseDto(resultSet);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}

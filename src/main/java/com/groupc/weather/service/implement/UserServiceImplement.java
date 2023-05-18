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
import com.groupc.weather.dto.request.user.FindByEmailRequestDto;
import com.groupc.weather.dto.request.user.FindByPasswordRequestDto;
import com.groupc.weather.dto.request.user.LoginUserRequestDto;
import com.groupc.weather.dto.request.user.PatchUserRequestDto;
import com.groupc.weather.dto.request.user.PostUserRequestDto;
import com.groupc.weather.dto.response.user.FindByEmailResponseDto;
import com.groupc.weather.dto.response.user.FindByPasswordResponseDto;
import com.groupc.weather.dto.response.user.GetUserResponseDto;
import com.groupc.weather.dto.response.user.LoginUserResponseDto;
import com.groupc.weather.entity.BoardEntity;
import com.groupc.weather.entity.CommentEntity;
import com.groupc.weather.entity.FollowingEntity;
import com.groupc.weather.entity.UserEntity;
import com.groupc.weather.provider.JwtProvider;
import com.groupc.weather.repository.BoardRepository;
import com.groupc.weather.repository.CommentRepository;
import com.groupc.weather.repository.UserRepository;
import com.groupc.weather.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplement(
            UserRepository userRepository,
            JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
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

            // 이거 반복문 돌려서 이름이랑 폰 번호 일치하는 사람의 이메일을 찾는 로직이 있어햐 하지 않나?
            // ==> DB 쿼리에서 WHRER 조건문 달아서 구분.
            String currentName = userEntity.getName(); // Entity 에 저장된 유저의 데이터 (name)
            String currentPhoneNumber = userEntity.getPhoneNumber();

            // 해당하는 이메일 반환.
            if (currentName == name && currentPhoneNumber == phoneNumber) { // name => 사용자가 입력한 유저의 데이터 (name)
                String userEmail = userEntity.getEmail();
                body = new FindByEmailResponseDto(userEmail);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 유저 비밀번호 찾기
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
            String currentEmail = userEntity.getEmail();
            String currentPoneNumber = userEntity.getPhoneNumber();

            if (currentEmail.equals(email) && currentPoneNumber == phoneNumber) {
                String userPassword = userEntity.getPassword();
                body = new FindByPasswordResponseDto(userPassword);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 유저 정보 수정
    @Override
    public ResponseEntity<ResponseDto> patchUser(PatchUserRequestDto dto) {

        int userNumber = dto.getUserNumber();
        String userEmail = dto.getUserEmail();
        String userPassword = dto.getUserPassword();
        String userNickname = dto.getUserNickname();
        String userPhoneNumber = dto.getUserPhoneNumber();
        String userAddress = dto.getUserPassword();
        String userProfileImageUrl = dto.getUserProfileImageUrl();
        String userGender = dto.getUserGender();
        String userBirthDay = dto.getUserBirhDay(); // 회원 수정할 때 성별, 생일도 수정해야하나??

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
            userEntity.setPassword(userPassword);
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
    public ResponseEntity<ResponseDto> deleteUser(PostUserRequestDto dto) {

        String userEmail = dto.getUserEmail();
        String userPassword = dto.getUserPassword();

        try {

            // 존재하는 이메일인지
            boolean existsUserEmail = userRepository.existsByEmail(userEmail);

            // 저 이메일이랑 맞는 비밀번호인지 어떻게 알수있는가??
            // 밑에 있는 로직은 유저레포지토리에 있는 존재하는 수많은 패스워드중 하나 일텐데 어떻게 이메일에 쓸때
            // 쓰는 비밀번호랑 같은지 알 수 있는가??? 이것도 mysql쿼리문으로 해야 하는가???
            boolean existsUserPassword = userRepository.existsByPassword(userPassword);

            if (!existsUserEmail)
                return CustomResponse.signInFailed();
            if (!existsUserPassword)
                return CustomResponse.signInFailed();

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
            // 존재하지 않는 조회한 유저 반환.
            UserEntity userEntity = userRepository.findByUserNumber(userNumber);
            if (userEntity == null)
                return CustomResponse.notExistUserNumber();

            List<FollowerEntity> followerEntities = followerRepository.findFollowerList(userNumber);
            List<FollowingEntity> followingEntities = followingRepository.findFollowingList(userNumber);
            userEntity = userRepository.findByUserNumber(userNumber);
            BoardEntity boardEntity = boardRepository.findByUserNumber(userNumber);
            CommentEntity commentEntity = commentRepository.findByUserNumber(userNumber);

            // FollowerEntity 없어서 오류 발생.
            body = new GetUserResponseDto(userEntity, boardEntity, commentEntity, followingEntities, null);

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    // 특정 유저 팔로우
    @Override
    public ResponseEntity<ResponseDto> followUser(Integer followingUserNumber) {

        try {
            // 로그인 한 상태가 아닐 때 반환.????

            // 존재하지 않는 팔로우하려는 유저 반환.
            // 유저 목록에 존재하는 유저인가.
            UserEntity userEntity = userRepository.findByUserNumber(followingUserNumber);
            if (userEntity == null)
                return CustomResponse.undifindUserNumber();

            // 본인의 number을 팔로잉 엔티티의 followerNumber에 저장

            //

        } catch (Exception exception) {
            exception.printStackTrace();
            return CustomResponse.databaseError();
        }
    }

    // 특정 유저 follow

}

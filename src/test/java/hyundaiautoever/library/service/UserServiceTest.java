package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.AuthType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.UserDto;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Love;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoveRepository loveRepository;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private ApplyRepository applyRepository;


    @Mock
    private ReserveRepository reserveRepository;


    @Mock
    private ReviewRepository reviewRepository;


    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    public void 회원가입_성공() throws Exception {
        //given
        UserRequest.CreateUserRequest request = createUserRequest();
        User user = createUserEntity(request);

        // mocking
        given(userRepository.save(any())).willReturn(user);
        given(userRepository.findById(any()))
                .willReturn(Optional.ofNullable(user));

        //when
        Long userId = userService.createUser(request);

        //then
        Optional<User> optionalUser = userRepository.findById(userId);
        assertTrue(optionalUser.isPresent(), "사용자가 저장되었음");
        User savedUser = optionalUser.get();
        assertEquals(request.getName(), savedUser.getName());
        assertEquals(request.getPersonalId(), savedUser.getPersonalId());
        assertEquals(request.getEmail(), savedUser.getEmail());
        assertEquals(request.getPartType(), savedUser.getPartType());
        assertEquals(request.getNickname(), savedUser.getNickname());
        assertNotNull(savedUser.getId());
    }


    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        UserRequest.CreateUserRequest request = createUserRequest();
        userService.createUser(request);

        //mocking
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);


        UserRequest.CreateUserRequest request2 = createUserRequest();

        // When
        LibraryException.DataDuplicateException exception = assertThrows(
                LibraryException.DataDuplicateException.class,
                () -> userService.createUser(request2)
        );

        //then
        assertEquals(ExceptionCode.DATA_DUPLICATE_EXCEPTION.getMessage(), exception.getExceptionCode().getMessage(), "중복 회원 예외 메시지 일치");
    }

    @Test
    public void 로그인_성공() {
        // Given
        UserRequest.LoginRequest request = createLoginRequest();
        User user = createUserWithPassword(request.getPersonalId(), request.getPassword());

        // mocking
        given(userRepository.findByPersonalId(request.getPersonalId())).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(true);

        // When
        UserDto.LoginDto loginDto = userService.loginUser(request);

        // Then
        assertNotNull(loginDto, "loginDto가 null이 아니어야 함");
        assertEquals(user.getId(), loginDto.getId(), "ID 일치");
    }

    @Test
    public void 존재하지_않는_회원_로그인_실패() throws Exception {
        // 로그인 request
        UserRequest.LoginRequest request = createLoginRequest();

        // mocking
        given(userRepository.findByPersonalId(request.getPersonalId())).willReturn(Optional.empty());

        //when
        LibraryException.LoginPersonalIdException exception = assertThrows(
                LibraryException.LoginPersonalIdException.class,
                () -> userService.loginUser(request)
        );

        //then
        assertEquals(ExceptionCode.PERSONALID_ERROR.getMessage(), exception.getExceptionCode().getMessage(), "로그인 아이디 예외 메시지 일치");
    }

    @Test
    public void 비밀번호_불일치_로그인_실패() throws Exception {
        //given
        UserRequest.LoginRequest request = createLoginRequest();
        User user = createUserWithPassword(request.getPersonalId(), "passwordok");

        // mocking
        given(userRepository.findByPersonalId(request.getPersonalId())).willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(false);

        //when
        LibraryException.LoginPasswordException exception = assertThrows(
                LibraryException.LoginPasswordException.class,
                () -> userService.loginUser(request)
        );

        //then
        assertEquals(ExceptionCode.PASSWORD_ERROR.getMessage(), exception.getExceptionCode().getMessage(), "로그인 비밀번호 예외 메시지 일치");
    }



    @Test
    public void 회원_전체_조회() throws Exception {
        //given
        List<User> userList = createSampleUserList(5);

        // mocking
        given(userRepository.findAll()).willReturn(userList);

        //when
        List<User> result = userService.findUsers();

        //then
        assertNotNull(result, "result가 null이 아니어야 함");
        assertEquals(userList.size(), result.size(), "result 크기 일치");
    }

    @Test
    public void 아이디_중복_체크_중복O() {
        // given
        String personalId = "id";
        given(userRepository.existsByPersonalId(personalId)).willReturn(true);

        // when
        boolean result = userService.checkPersonalId(personalId);

        // then
        assertTrue(result);
    }

    @Test
    public void 아이디_중복_체크_중복_X() {
        // given
        String personalId = "non";
        given(userRepository.existsByPersonalId(personalId)).willReturn(false);

        // when
        boolean result = userService.checkPersonalId(personalId);

        // then
        assertFalse(result);
    }

    @Test
    public void 이메일_중복_체크_중복O() {
        // given
        String email = "id@test.com";
        given(userRepository.existsByEmail(email)).willReturn(true);

        // when
        boolean result = userService.checkEmail(email);

        // then
        assertTrue(result);
    }

    @Test
    public void 이메일_중복_체크_중복X() {
        // given
        String email = "non@test.com";
        given(userRepository.existsByEmail(email)).willReturn(false);

        // when
        boolean result = userService.checkEmail(email);

        // then
        assertFalse(result);
    }

    @Test
    public void 닉네임_중복_체크_중복_O() {
        // given
        String nickname = "nick";
        given(userRepository.existsByNickname(nickname)).willReturn(true);

        // when
        boolean result = userService.checkNickname(nickname);

        // then
        assertTrue(result);
    }

    @Test
    public void 닉네임_중복_체크_중복_X() {
        // given
        String nickname = "non";
        given(userRepository.existsByNickname(nickname)).willReturn(false);

        // when
        boolean result = userService.checkNickname(nickname);

        // then
        assertFalse(result);
    }

    @Test
    public void 프로필수정_성공() {
        // given
        String personalId = "id";
        String password = "pass";
        String newPassword = "newPassword";
        String email = "newemail@example.com";
        PartType partType = PartType.MOBIS;
        String nickname = "newNickname";

        UserRequest.UpdateProfileRequest request = new UserRequest.UpdateProfileRequest();
        request.setPersonalId(personalId);
        request.setPassword(password);
        request.setNewPassword(newPassword);
        request.setEmail(email);
        request.setPartType(partType);
        request.setNickname(nickname);

        User existingUser = User.builder()
                .personalId(personalId)
                .password(passwordEncoder.encode(password))
                .email("email@test.com")
                .partType(PartType.FACTORY)
                .nickname("nick")
                .build();

        given(userRepository.findByPersonalId(personalId)).willReturn(Optional.of(existingUser));
        given(passwordEncoder.matches(password, existingUser.getPassword())).willReturn(true);

        // when
        userService.updateProfile(request);

        // then
        assertEquals(passwordEncoder.encode(newPassword), existingUser.getPassword());
        assertEquals(email, existingUser.getEmail());
        assertEquals(partType, existingUser.getPartType());
        assertEquals(nickname, existingUser.getNickname());
    }

    @Test
    public void 프로필수정_personalId_X() {
        // given
        String nonExistingPersonalId = "non";

        UserRequest.UpdateProfileRequest request = new UserRequest.UpdateProfileRequest();
        request.setPersonalId(nonExistingPersonalId);
        request.setPassword("password");

        given(userRepository.findByPersonalId(nonExistingPersonalId)).willReturn(Optional.empty());

        // when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> userService.updateProfile(request)
        );

        // then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION, exception.getExceptionCode());
    }

    @Test
    public void 프로필수정_비밀번호오류() {
        // given
        String personalId = "id";
        String incorrectPassword = "incorrect";

        UserRequest.UpdateProfileRequest request = new UserRequest.UpdateProfileRequest();
        request.setPersonalId(personalId);
        request.setPassword(incorrectPassword);

        User existingUser = User.builder()
                .personalId(personalId)
                .password(passwordEncoder.encode("password"))
                .build();

        given(userRepository.findByPersonalId(personalId)).willReturn(Optional.of(existingUser));
        given(passwordEncoder.matches(incorrectPassword, existingUser.getPassword())).willReturn(false);

        // when
        LibraryException.LoginPasswordException exception = assertThrows(
                LibraryException.LoginPasswordException.class,
                () -> userService.updateProfile(request)
        );

        // then
        assertEquals(ExceptionCode.PASSWORD_ERROR, exception.getExceptionCode());
    }

    @Test
    public void ADMIN_권한관리_성공() {
        // given
        String personalId = "id";
        String auth = "ADMIN";

        User existingUser = User.builder()
                .personalId(personalId)
                .build();

        given(userRepository.findByPersonalId(personalId)).willReturn(Optional.of(existingUser));

        // when
        UserDto.LoginDto result = userService.updateAuth(personalId, auth);

        // then
        assertEquals(AuthType.ADMIN, existingUser.getAuthType());
        assertEquals(existingUser.getPersonalId(), result.getPersonalId());
    }


    @Test
    public void 회원탈퇴_성공() {
        // Given
        String personalId = "user123";

        User user = User.builder()
                .personalId(personalId)
                .build();

        given(userRepository.findByPersonalId(personalId)).willReturn(Optional.of(user));
        given(rentRepository.findByUser(user)).willReturn(Collections.emptyList());
        given(loveRepository.findByUser(user)).willReturn(Collections.emptyList());
        given(reserveRepository.findByUser(user)).willReturn(Collections.emptyList());
        doNothing().when(userRepository).deleteById(user.getId());

        // When
        Response response = userService.deleteUser(personalId);

        // Then
        assertEquals(Response.ok().getCode(), response.getCode());
        assertNull(response.getData());
    }

    @Test
    public void 아이디_찾기() {
        // given
        User user = createUser();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // when
        String result = userService.getFindId(user.getEmail());

        // then
        assertEquals(user.getPersonalId(), result);
    }

    @Test
    public void 아이디_찾기_없음() {
        // given
        String email = "no@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        // when/then
        assertThrows(LibraryException.DataNotFoundException.class, () -> {
            userService.getFindId(email);
        });
    }

    @Test
    public void 비밀번호재설정_성공() {
        // given
        User user = Mockito.mock(User.class);
        UserRequest.ResetPassword request = new UserRequest.ResetPassword();
        request.setPersonalId(user.getPersonalId());
        request.setNewPassword("newPassword");

        when(userRepository.findByPersonalId(request.getPersonalId())).thenReturn(Optional.of(user));


        // Mocking passwordEncoder.encode() method
        String encodePassword = "encodePassword";
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn(encodePassword);

        // when
        assertDoesNotThrow(() -> userService.resetPassword(request));

        // then
        // 함수 호출 확인
        verify(userRepository).findByPersonalId(request.getPersonalId());
        verify(user).updateUserPassword(encodePassword);
    }

    @Test
    public void 비밀번호_재설정_아이디_못찾음() {
        // given
        String personalId = "no";

        UserRequest.ResetPassword request = new UserRequest.ResetPassword();
        request.setPersonalId(personalId);
        request.setNewPassword("newPassword");

        when(userRepository.findByPersonalId(personalId)).thenReturn(Optional.empty());

        // when/then
        assertThrows(LibraryException.DataNotFoundException.class, () -> {
            userService.resetPassword(request);
        });
    }


    private UserRequest.CreateUserRequest createUserRequest() {
        UserRequest.CreateUserRequest request = new UserRequest.CreateUserRequest();
        request.setEmail("test@join.com");
        request.setPartType(PartType.MOBIS);
        request.setName("회원가입테스트");
        request.setNickname("joinnickname");
        request.setPassword("test");
        request.setPersonalId("jointest");
        return request;
    }

    private UserRequest.LoginRequest createLoginRequest() {
        UserRequest.LoginRequest request = new UserRequest.LoginRequest();
        request.setPersonalId("jointest");
        request.setPassword("test");

        return request;
    }

    private User createUser() {
        User user = new User("회원가입테스트","jointest", passwordEncoder.encode("test"),"test@join.com", PartType.MOBIS, "joinnickname");
        return user;
    }

    private User createByUserName(String name, String personalId, String email, String nickname) {
        User user = new User(name,personalId, passwordEncoder.encode("test"),email, PartType.MOBIS, nickname);
        return user;
    }

    private User createUserEntity(UserRequest.CreateUserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .personalId(request.getPersonalId())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .partType(request.getPartType())
                .nickname(request.getNickname())
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private User createUserWithPassword(String personalId, String password) {
        User user = User.builder()
                .personalId(personalId)
                .password(passwordEncoder.encode(password))
                        .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private List<User> createSampleUserList(int count) {
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            User user = createUserEntity(createUserRequest());
            userList.add(user);
        }

        return userList;
    }
}
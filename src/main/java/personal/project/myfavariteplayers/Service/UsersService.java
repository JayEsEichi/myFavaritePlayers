package personal.project.myfavariteplayers.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import personal.project.myfavariteplayers.Controller.Response.TokenDto;
import personal.project.myfavariteplayers.Entity.User;
import personal.project.myfavariteplayers.Entity.UserRoleEnum;
import personal.project.myfavariteplayers.Repository.UsersRepository;
import personal.project.myfavariteplayers.jwt.TokenProvider;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    // 계정 생성 (post맨 검증용)
//    @Transactional
//    public ResponseDto<?> registeraccount(UsersRequestDto usersRequestDto) {
////        User user = usersRepository.findByUserid(usersRequestDto.getUserid());
//
//        if (usersRepository.findByUserid(usersRequestDto.getUserid()) != null) {
//            ResponseDto.fail("ALREADY_EXIST_USER", "중복된 사용자입니다.");
//
//        } else if (!usersRequestDto.getUserpwd().equals(usersRequestDto.getUserpwdcheck())) {
//            ResponseDto.fail("WROND_PASSWORD_CHECK", "비빌번호와 재확인 비밀번호가 같지 않습니다.");
//        }
//
//        System.out.println("enum 계정 등급 : " + UserRoleEnum.USER);
//
//        // 이전에는 entity 에 생성자를 만들어서 usersRequestDto 값을 직접 넣어주었다.
//        User user = User.builder()
//                .userid(usersRequestDto.getUserid())
//                .userpwd(passwordEncoder.encode(usersRequestDto.getUserpwd()))
//                .username(usersRequestDto.getUsername())
//                .email(usersRequestDto.getEmail())
//                .role(UserRoleEnum.USER)
//                .build();
//
//        usersRepository.save(user);
//
//        return ResponseDto.success(user);
//    }

    // 회원가입
    @Transactional
    public String registeraccount(String userid, String userpwd, String username, String email) {

        System.out.println("enum 계정 등급 : " + UserRoleEnum.USER);

        User user = User.builder()
                .userid(userid)
                .userpwd(passwordEncoder.encode(userpwd))
                .username(username)
                .email(email)
                .role(UserRoleEnum.USER)
                .build();

        usersRepository.save(user);

        return "index";
    }

    // 로그인
    @Transactional
    public void login(String userid, String userpwd, HttpServletResponse response){
        User user = usersRepository.findByUserid(userid);

        if(user == null || !passwordEncoder.matches(userpwd, user.getUserpwd())){
            return;
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(user);
        createheader(tokenDto, response);

    }

    public void createheader(TokenDto tokenDto, HttpServletResponse response){
        response.addHeader("Authorization",tokenDto.getAccessToken());
        response.addHeader("Refresh-Token",tokenDto.getRefreshToken());
        response.addHeader("Expire-Time",(String.valueOf(tokenDto.getAccessTokenExpiresIn())));
    }
}

package personal.project.myfavariteplayers.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import personal.project.myfavariteplayers.Service.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UsersService usersService;

    // 최초 로그인 페이지 이동
    @GetMapping("/public/login")
    public String moveLoginPage(){
        return "login";
    }

    // 로그인
    // 로그인 성공 후 유저명 정보를 mainpage 에 모델로 저장시켜 넘긴다.
    // post 맨으로 확인하고 싶으면 @Responsebody를 붙인다.
    @GetMapping("/public/login/confirm")
    public String moveMainPage(Model model,
                               HttpServletRequest request,
                               HttpServletResponse response
    ){

        String userid = request.getParameter("userid");
        String userpwd = request.getParameter("userpwd");

        usersService.login(userid, userpwd, response);

        model.addAttribute("AccessToken",  response.getHeader("Authorization"));

        System.out.println("리스폰스 : " + response.getHeader("Authorization"));
        System.out.println("리퀘스트 : " + request);

        return "home";

    }

    // 계정 생성 페이지 이동
    @GetMapping("/public/register")
    public String moveRegisterPage(){
        return "register";
    }


    // 회원가입
    // POST 방싟 (postMapping 으로 하려고 했더니 되지 않는다.)
    @RequestMapping(value = "/public/register/confirm")
    public String registerAccount(HttpServletRequest request){
        String userid = request.getParameter("userid");
        String userpwd = request.getParameter("userpwd");
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        log.info("넘어왔나 ? {} {} {} {} " ,userid, userpwd, username, email);
        return usersService.registeraccount(userid, userpwd, username, email);
    }

    // 계정 생성 (postman을 이용한 테스트를 위한 메소드)
//    @PostMapping("/public/register")
//    public ResponseDto<?> registeraccount(@RequestBody UsersRequestDto usersRequestDto){
//        return usersService.registeraccount(usersRequestDto);
//    }

    // 로그인
//    @PostMapping("/public/login")
//    public String login(@RequestParam(required = false) String userid, @RequestParam(required = false) String userpwd){
//        System.out.println(userid + " " + userpwd);
//        return usersService.login(userid, userpwd);
////        return "index";
//    }



}

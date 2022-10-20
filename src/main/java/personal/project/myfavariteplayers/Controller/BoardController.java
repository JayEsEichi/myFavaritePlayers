package personal.project.myfavariteplayers.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import personal.project.myfavariteplayers.Controller.Response.ResponseDto;
import personal.project.myfavariteplayers.Controller.Request.BoardRequestDto;
import personal.project.myfavariteplayers.Security.UserDetailsImpl;
import personal.project.myfavariteplayers.Service.BoardService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;

    // 게시판 페이지 이동
    @GetMapping("/public/board")
    public String moveBoardPage(){
        return "board";
    }


    // 게시글 작성 : (프론트랑 연결해줘야함)
    @GetMapping("/auth/write")
    public String moveWriteBoardPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return "writeboard;";
    }


    // 게시글 작성 (ResponseBody 로 json 형식으로 구현 (post맨 필요) )
    @ResponseBody
    @PostMapping("/auth/write/confirm")
    public ResponseDto<?> writeBoard(@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request) {
        return boardService.writeBoard(boardRequestDto, request);
    }


    // 게시글 수정 (ResponseBody 로 json 형식으로 구현 (post맨 필요) )
    @ResponseBody
    @PutMapping("/auth/update/confirm/{boardid}")
    public ResponseDto<?> updateBoard(@PathVariable Long boardid, @RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request){
        return boardService.updateBoard(boardid, boardRequestDto, request);
    }


    // 게시글 삭제 (ResponseBody 로 json 형식으로 구현 (post맨 필요) )
    @ResponseBody
    @DeleteMapping("/auth/delete/confirm/{boardid}")
    public ResponseDto<?> deleteBoard(@PathVariable Long boardid, HttpServletRequest request){
        return boardService.deleteBoard(boardid, request);
    }


    // 게시글 전체 조회 (ResponseBody 로 json 형식으로 구현 (post맨 필요) )
    @ResponseBody
    @GetMapping("/public/boards")
    public ResponseDto<?> getAllBoards(){
        return boardService.getAllBoards();
    }


    // 작성한 게시글 조회
    @ResponseBody
    @GetMapping("/auth/select/confirm")
    public ResponseDto<?> getAllMyBoards(HttpServletRequest request){
        return boardService.getAllMyBoards(request);
    }



}

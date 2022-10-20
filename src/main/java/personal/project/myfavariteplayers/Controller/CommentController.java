package personal.project.myfavariteplayers.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import personal.project.myfavariteplayers.Controller.Request.CommentRequestDto;
import personal.project.myfavariteplayers.Controller.Response.ResponseDto;
import personal.project.myfavariteplayers.Service.CommentService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/comment")
@Controller
public class CommentController {

    private final CommentService commentService;


    // 댓글 작성 (ResponseBody 로 json 형식으로 구현 (post맨 필요) )
    @ResponseBody
    @PostMapping("/auth/write/confirm")
    public ResponseDto<?> writeComment(@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request){
        return commentService.writeComment(commentRequestDto, request);
    }
}

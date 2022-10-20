package personal.project.myfavariteplayers.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal.project.myfavariteplayers.Controller.Request.CommentRequestDto;
import personal.project.myfavariteplayers.Controller.Response.CommentResponseDto;
import personal.project.myfavariteplayers.Controller.Response.ResponseDto;
import personal.project.myfavariteplayers.Entity.Comment;
import personal.project.myfavariteplayers.Entity.User;
import personal.project.myfavariteplayers.Repository.CommentRepository;
import personal.project.myfavariteplayers.jwt.TokenProvider;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;

    // 댓글 작성
    public ResponseDto<?> writeComment(CommentRequestDto commentRequestDto, HttpServletRequest request){
        if(request == null){
            return ResponseDto.fail("NOT_EXIST_REQUEST", "요청이 존재하지 않습니다.");
        }

        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))){
            return ResponseDto.fail("NOT_EXIST_TOKEN", "토큰을 분석할 수 없습니다.");
        }

        User user = tokenProvider.getUserFromAuthentication();

        Comment comment = Comment.builder()
                .username(user.getUsername())
                .content(commentRequestDto.getContent())
                .build();

        commentRepository.save(comment);

        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .username(comment.getUsername())
                        .content(comment.getContent())
                        .CreatedAt(comment.getCreatedAt())
                        .ModifiedAt(comment.getModifiedAt())
                        .build()
        );
    }
}

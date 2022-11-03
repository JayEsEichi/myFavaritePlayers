package personal.project.myfavariteplayers.Service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import personal.project.myfavariteplayers.Controller.Request.CommentRequestDto;
import personal.project.myfavariteplayers.Controller.Response.CommentResponseDto;
import personal.project.myfavariteplayers.Controller.Response.ResponseDto;
import personal.project.myfavariteplayers.Entity.Board;
import personal.project.myfavariteplayers.Entity.Comment;
import personal.project.myfavariteplayers.Entity.User;
import personal.project.myfavariteplayers.Query.QuerydslConfig;
import personal.project.myfavariteplayers.Repository.BoardRepository;
import personal.project.myfavariteplayers.Repository.CommentRepository;
import personal.project.myfavariteplayers.jwt.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static personal.project.myfavariteplayers.Entity.QComment.comment;

//@Import(QuerydslConfig.class)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    // 댓글 작성
    public ResponseDto<?> writeComment(CommentRequestDto commentRequestDto, HttpServletRequest request){
        if(request == null){
            return ResponseDto.fail("NOT_EXIST_REQUEST", "요청이 존재하지 않습니다.");
        }

        if(!tokenProvider.validateToken(request.getHeader("Refresh-Token"))){
            return ResponseDto.fail("NOT_EXIST_TOKEN", "토큰을 분석할 수 없습니다.");
        }

        User user = tokenProvider.getUserFromAuthentication();
        Board board = boardRepository.findById(commentRequestDto.getBoardid()).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );

        Comment comment = Comment.builder()
                .username(user.getUsername())
                .content(commentRequestDto.getContent())
                .board(board)
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

    // 댓글 전체 조회 (queryDSL 활용 테스트) / 성공!!!!!!!!!!!!!!!!!
    public ResponseDto<?> getAllComments() {
        List<Comment> comments =
                jpaQueryFactory
                        .select(comment)
                        .from(comment)
                        .fetch();


        List<CommentResponseDto> commentlist = new ArrayList<>();

        for(Comment comment : comments){
            commentlist.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .username(comment.getUsername())
                            .content(comment.getContent())
                            .CreatedAt(comment.getCreatedAt())
                            .ModifiedAt(comment.getModifiedAt())
                            .build()
            );

            System.out.println("아이디:"+comment.getId()+"이름:"+comment.getUsername()+"내용"+comment.getContent());

            System.out.println("아이디 : " + comment.getId() + " 이름 : " + comment.getUsername() + " 내용 : " + comment.getContent());
        }

        return ResponseDto.success(commentlist);
    }
}

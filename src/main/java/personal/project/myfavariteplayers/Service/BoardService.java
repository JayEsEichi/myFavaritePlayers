package personal.project.myfavariteplayers.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import personal.project.myfavariteplayers.Controller.Response.BoardResponseDto;
import personal.project.myfavariteplayers.Controller.Response.ResponseDto;
import personal.project.myfavariteplayers.Controller.Request.BoardRequestDto;
import personal.project.myfavariteplayers.Entity.Board;
import personal.project.myfavariteplayers.Entity.User;
import personal.project.myfavariteplayers.Repository.BoardRepository;
import personal.project.myfavariteplayers.Security.UserDetailsImpl;
import personal.project.myfavariteplayers.jwt.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final TokenProvider tokenProvider;

    // 게시글 작성
    public ResponseDto<?> writeBoard(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        if (request == null) {
            return ResponseDto.fail("NOT_EXIST_REQUEST", "요청이 존재하지 않습니다. 다시 로그인해주시기 바랍니다.");
        }

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("NOT_EXIST_TOKEN", "토큰을 분석할 수 없습니다.");
        }

        User user = tokenProvider.getUserFromAuthentication();

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .username(user.getUsername())
                .user(user)
                .build();

        boardRepository.save(board);

        return ResponseDto.success(
                BoardResponseDto.builder()
                        .id(board.getId())
                        .username(board.getUsername())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .CreatedAt(board.getCreatedAt())
                        .ModifiedAt(board.getModifiedAt())
                        .build());
    }

    // 게시글 수정
    @Transactional
    public ResponseDto<?> updateBoard(Long boardid, BoardRequestDto boardRequestDto, HttpServletRequest request) {
        if (request == null) {
            return ResponseDto.fail("NOT_EXIST_REQUEST", "요청이 존재하지 않습니다. 다시 로그인해주시기 바랍니다.");
        }

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("NOT_EXIST_TOKEN", "토큰을 분석할 수 없습니다.");
        }

        User user = tokenProvider.getUserFromAuthentication();

        if (boardRepository.findByIdAndUser(boardid, user) == null) {
//            return ResponseDto.fail("NOT_EXIST_BOARD", "유저가 작성한 게시글이 존재하지 않습니다.");
            throw new NullPointerException("유저가 작성한 게시글이 존재하지 않습니다.");
        }
        ;

        Board board = boardRepository.findByIdAndUser(boardid, user);

        board.update(boardRequestDto);

        // 이 메소드 뿐만 아니라 반환값을 객체 자체로 해주면 안된다... 왜일까?
        // ResponseDto 에 객체만 넣어서 반환하는 것도 안됨...
        return ResponseDto.success(
                BoardResponseDto.builder()
                        .id(board.getId())
                        .username(board.getUsername())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .CreatedAt(board.getCreatedAt())
                        .ModifiedAt(board.getModifiedAt())
                        .build());
    }


    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deleteBoard(Long boardid, HttpServletRequest request) {
        if (request == null) {
            return ResponseDto.fail("NOT_EXIST_REQUEST", "요청이 존재하지 않습니다. 다시 로그인해주시기 바랍니다.");
        }

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("NOT_EXIST_TOKEN", "토큰을 분석할 수 없습니다.");
        }

        User user = tokenProvider.getUserFromAuthentication();

        if (boardRepository.findByIdAndUser(boardid, user) == null) {
//            return ResponseDto.fail("NOT_EXIST_BOARD", "유저가 작성한 게시글이 존재하지 않습니다.");
            throw new NullPointerException("유저가 작성한 게시글이 존재하지 않습니다.");
        }
        ;

        boardRepository.deleteById(boardid);

        return ResponseDto.success("Delete Complete!");

    }


    // 게시글 전체 조회
    public ResponseDto<?> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardResponseDto> boardlist = new ArrayList<>();

        for (Board board : boards) {
            boardlist.add(
                    BoardResponseDto.builder()
                            .id(board.getId())
                            .username(board.getUsername())
                            .title(board.getTitle())
                            .content(board.getContent())
                            .CreatedAt(board.getCreatedAt())
                            .ModifiedAt(board.getModifiedAt())
                            .build()
            );

        }

        // 객체를 바로 반환하는 것은 안되고 어떻게든 dto로 반환하든, dto 리스트를 만들어서 그 리스트를 반환해야한다.
        // 즉, 객체 직접 주입 반환 X, dto 경유 반환 O
        return ResponseDto.success(boardlist);
    }

    // 작성한 모든 게시글 조회
    public ResponseDto<?> getAllMyBoards(HttpServletRequest request){
        if (request == null) {
            return ResponseDto.fail("NOT_EXIST_REQUEST", "요청이 존재하지 않습니다. 다시 로그인해주시기 바랍니다.");
        }

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("NOT_EXIST_TOKEN", "토큰을 분석할 수 없습니다.");
        }

        User user = tokenProvider.getUserFromAuthentication();
        List<Board> boards = boardRepository.findAllByUser(user);
        List<BoardResponseDto> boardlist = new ArrayList<>();

        for(Board board : boards){
            boardlist.add(
                    BoardResponseDto.builder()
                            .id(board.getId())
                            .title(board.getTitle())
                            .content(board.getContent())
                            .username(board.getUsername())
                            .CreatedAt(board.getCreatedAt())
                            .ModifiedAt(board.getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(boardlist);
    }


}

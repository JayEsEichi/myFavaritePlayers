package personal.project.myfavariteplayers.Controller.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class CommentResponseDto {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime CreatedAt;
    private LocalDateTime ModifiedAt;
}

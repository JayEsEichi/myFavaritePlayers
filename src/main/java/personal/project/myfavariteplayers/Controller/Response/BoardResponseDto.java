package personal.project.myfavariteplayers.Controller.Response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BoardResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private LocalDateTime CreatedAt;
    private LocalDateTime ModifiedAt;
}

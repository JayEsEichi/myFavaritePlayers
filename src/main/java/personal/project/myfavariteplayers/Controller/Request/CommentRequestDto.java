package personal.project.myfavariteplayers.Controller.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequestDto {
    private Long boardid;
    private String content;

}

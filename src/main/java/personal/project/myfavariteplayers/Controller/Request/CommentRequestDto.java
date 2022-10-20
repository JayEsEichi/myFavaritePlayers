package personal.project.myfavariteplayers.Controller.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CommentRequestDto {

    private String content;

}

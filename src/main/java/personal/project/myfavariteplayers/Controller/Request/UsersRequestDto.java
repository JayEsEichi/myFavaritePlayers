package personal.project.myfavariteplayers.Controller.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import personal.project.myfavariteplayers.Entity.UserRoleEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
public class UsersRequestDto {

    @NotBlank(message = "{userid.notblank}")
    @Pattern(regexp ="^[a-zA-Z0-9]{4,20}$", message = "{userid.option}" )
    private String userid;

    @NotBlank(message = "{password.notblank}")
    @Pattern(regexp ="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$" , message = "{password.option}" )
    private String userpwd;

    @Pattern(regexp ="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$" , message = "{password.option}" )
    @NotBlank(message = "{password.notblank}")
    private String userpwdcheck;

    @NotBlank(message = "{username.notblank}")
    private String username;

    @Pattern(regexp ="^[a-zA-Z0-9]+@[a-zA-Z]+.[a-z]+${4,12}$", message = "{email.option}" )
    @NotBlank(message = "{email.notblank}")
    private String email;




}

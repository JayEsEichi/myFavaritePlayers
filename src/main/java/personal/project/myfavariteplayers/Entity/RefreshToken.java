package personal.project.myfavariteplayers.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Column
    @Id
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne()
    private User user;

    @Column
    private String value;


}

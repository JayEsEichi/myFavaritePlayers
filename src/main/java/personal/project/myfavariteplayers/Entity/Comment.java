package personal.project.myfavariteplayers.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String username;

    @Column
    private String content;

    @JoinColumn(name="boardid", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}

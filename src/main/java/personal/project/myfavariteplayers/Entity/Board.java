package personal.project.myfavariteplayers.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import personal.project.myfavariteplayers.Controller.Request.BoardRequestDto;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor
@Getter
@Entity
@Builder
@NoArgsConstructor
public class Board extends  Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @JoinColumn(name = "userid", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comment> comments;

    public void update(BoardRequestDto boardRequestDto){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
    }

}

package toyproject.MatnMut.domain.board;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Post {

    private Long id;

    private String writer;

    private Long views;

    private String postTime;

    @NotEmpty
    private String header;

    @NotEmpty
    private String title;

    @NotEmpty
    private String contents;
}

package toyproject.MatnMut.domain.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class Member {

    private Long id;

    private LocalDate registerDate;

//    private LocalDate recentLoginDate;

    @NotEmpty(message = "required")
    private String name;

    @NotEmpty(message = "required")
    private String loginId;

    @NotEmpty(message = "required")
//    @Length(min=8, max=20)
    private String password;

//    private String nickname;
}

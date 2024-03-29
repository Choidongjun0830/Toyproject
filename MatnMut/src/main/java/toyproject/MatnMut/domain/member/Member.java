package toyproject.MatnMut.domain.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class Member {

    @NotBlank(groups = UpdateCheck.class)
    private Long id;


    private LocalDate registerDate;

//    private LocalDate recentLoginDate;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String name;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String loginId;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
//    @Length(min=8, max=20)
    private String password;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String nickname;

    private int point;

    public Member() {
    }

    public Member(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    public Member(String name, String loginId, String password, String nickname, int point) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.point = point;
    }
}

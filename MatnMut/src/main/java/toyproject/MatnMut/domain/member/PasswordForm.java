package toyproject.MatnMut.domain.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordForm {
    @Length(min = 8, max = 20)
    @NotBlank
    private String newPassword;

    @Length(min = 8, max = 20)
    @NotBlank
    private String newPasswordConfirm;
}

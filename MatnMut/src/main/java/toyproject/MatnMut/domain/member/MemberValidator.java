package toyproject.MatnMut.domain.member;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class MemberValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member) target;

        if(!StringUtils.hasText(member.getLoginId())) {
            errors.rejectValue("name","required");
        }
        if(!StringUtils.hasText(member.getLoginId())) {
            errors.rejectValue("loginId", "required");
        }
        if(!StringUtils.hasText(member.getPassword())) {
            errors.rejectValue("password", "required");
        }
    }
}

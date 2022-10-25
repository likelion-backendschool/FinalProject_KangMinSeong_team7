package com.example.eBook.domain.member.validator;

import com.example.eBook.domain.member.dto.PwdModifyForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PwdModifyFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PwdModifyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PwdModifyForm pwdModifyForm = (PwdModifyForm) target;

        if (!pwdModifyForm.getPassword().equals(pwdModifyForm.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "notSame", "2개의 패스워드가 일치하지 않습니다.");
        }

        if (!pwdModifyForm.getPassword().isBlank() && pwdModifyForm.getOldPassword().equals(pwdModifyForm.getPassword())) {
            errors.rejectValue("password", "notModified", "기존 비밀번호와 일치합니다.");
        }
    }
}

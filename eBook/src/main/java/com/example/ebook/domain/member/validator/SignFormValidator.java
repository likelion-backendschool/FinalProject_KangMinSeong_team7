package com.example.ebook.domain.member.validator;

import com.example.ebook.domain.member.dto.SignupForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SignupForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupForm signupForm = (SignupForm) target;

        if (!signupForm.getPassword().equals(signupForm.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "notSame", "2개의 패스워드가 일치하지 않습니다.");
        }
    }
}

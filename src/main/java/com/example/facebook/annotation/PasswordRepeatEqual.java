package com.example.facebook.annotation;


import com.example.facebook.validator.PasswordRepeatEqualValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordRepeatEqualValidator.class)
public @interface PasswordRepeatEqual {
    String message() default "Password mismatch";
    String passwordFieldFirst();
    String passwordFieldSecond();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

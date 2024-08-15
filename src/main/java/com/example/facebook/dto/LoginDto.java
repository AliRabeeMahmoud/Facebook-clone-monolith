package com.example.facebook.dto;


import com.example.facebook.annotation.ValidEmail;
import com.example.facebook.annotation.ValidPassword;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}

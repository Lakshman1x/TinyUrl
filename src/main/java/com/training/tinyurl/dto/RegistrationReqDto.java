package com.training.tinyurl.dto;

import com.training.tinyurl.constants.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationReqDto {

    @NotBlank(message = "Email can not be blank")
    @Size(min = 4, max = 64, message = "email size must be between 4 and 64")
    @Email(message = "Invalid email pattern")
    private String email;

    @NotBlank(message = "Password can not be blank")
    @Size(min = 8, max = 64, message = "Password size must be between 8 and 64")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).+$",
            message = "Password must contain a number, a lowercase, and a uppercase")
    private String password;

    @NotNull(message = "Account type can not be empty")
    private AccountType accountType;
}

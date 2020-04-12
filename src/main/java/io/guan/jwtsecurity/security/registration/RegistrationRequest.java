package io.guan.jwtsecurity.security.registration;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email address should be valid")
    private String email;
    private String confirmEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password minimum is 8 characters")
    private String password;
    private String confirmPassword;

    private boolean emailMatching;

    @AssertTrue(message = "Emails must match")
    public boolean isEmailMatching() {
        if (email != null) {
            return email.equals(confirmEmail);
        }
        return true;
    }

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatching() {
        if (password != null) {
            return password.equals(confirmPassword);
        }
        return true;
    }
}

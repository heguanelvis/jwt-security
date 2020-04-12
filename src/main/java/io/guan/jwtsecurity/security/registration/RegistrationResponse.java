package io.guan.jwtsecurity.security.registration;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationResponse {
    private String message;
}

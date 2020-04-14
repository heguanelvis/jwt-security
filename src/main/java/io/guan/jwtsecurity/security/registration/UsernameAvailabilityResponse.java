package io.guan.jwtsecurity.security.registration;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsernameAvailabilityResponse {
    private boolean usernameAvailable;
}

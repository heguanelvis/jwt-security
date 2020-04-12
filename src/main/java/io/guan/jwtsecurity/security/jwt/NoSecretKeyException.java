package io.guan.jwtsecurity.security.jwt;

public class NoSecretKeyException extends Exception {
    public NoSecretKeyException(String errorMessage) {
        super(errorMessage);
    }
}

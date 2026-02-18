package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class MuralTokenService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private final Base64.Encoder b64UrlEncoder = Base64.getUrlEncoder().withoutPadding();
    private final Base64.Decoder b64UrlDecoder = Base64.getUrlDecoder();
    private final byte[] secretKey;
    private final long qrValiditySeconds;
    private final long sessionValiditySeconds;

    public MuralTokenService(
            @Value("${mural.token.secret:}") String configuredSecret,
            @Value("${mural.token.qr-validity-minutes:120}") long qrValidityMinutes,
            @Value("${mural.token.session-validity-minutes:15}") long sessionValidityMinutes
    ) {
        String finalSecret = (configuredSecret == null || configuredSecret.isBlank())
                ? Long.toHexString(RANDOM.nextLong()) + Long.toHexString(System.nanoTime())
                : configuredSecret;
        this.secretKey = finalSecret.getBytes(StandardCharsets.UTF_8);
        this.qrValiditySeconds = Math.max(60, qrValidityMinutes * 60);
        this.sessionValiditySeconds = Math.max(60, sessionValidityMinutes * 60);
    }

    public IssuedToken issueQrToken(String remoteIp) {
        return issueToken(remoteIp, "QR", qrValiditySeconds);
    }

    public IssuedToken issueSessionToken(String remoteIp) {
        return issueToken(remoteIp, "SESSION", sessionValiditySeconds);
    }

    private IssuedToken issueToken(String remoteIp, String tokenType, long durationSeconds) {
        long exp = Instant.now().getEpochSecond() + durationSeconds;
        String ipScope = buildIpScope(remoteIp);
        String nonce = Long.toHexString(RANDOM.nextLong());
        String payload = tokenType + "|" + ipScope + "|" + exp + "|" + nonce;
        String payloadEncoded = b64UrlEncoder.encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signatureEncoded = b64UrlEncoder.encodeToString(sign(payload));
        return new IssuedToken(payloadEncoded + "." + signatureEncoded, exp);
    }

    public ValidationResult validate(String token, String remoteIp, String expectedType) {
        if (token == null || token.isBlank()) {
            return ValidationResult.invalid();
        }

        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            return ValidationResult.invalid();
        }

        String payload;
        try {
            payload = new String(b64UrlDecoder.decode(parts[0]), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return ValidationResult.invalid();
        }

        byte[] expectedSig = sign(payload);
        byte[] receivedSig;
        try {
            receivedSig = b64UrlDecoder.decode(parts[1]);
        } catch (IllegalArgumentException e) {
            return ValidationResult.invalid();
        }

        if (!MessageDigest.isEqual(expectedSig, receivedSig)) {
            return ValidationResult.invalid();
        }

        String[] payloadParts = payload.split("\\|");
        if (payloadParts.length < 4) {
            return ValidationResult.invalid();
        }

        String tokenType = payloadParts[0];
        String tokenScope = payloadParts[1];

        if (!tokenType.equals(expectedType)) {
            return ValidationResult.wrongTypeResult();
        }

        long exp;
        try {
            exp = Long.parseLong(payloadParts[2]);
        } catch (NumberFormatException e) {
            return ValidationResult.invalid();
        }

        long now = Instant.now().getEpochSecond();
        if (exp < now) {
            return ValidationResult.expiredResult();
        }

        String expectedScope = buildIpScope(remoteIp);
        if (!tokenScope.equals(expectedScope)) {
            return ValidationResult.wrongNetworkResult();
        }

        return ValidationResult.valid(exp);
    }

    private byte[] sign(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            return mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao assinar token do mural", e);
        }
    }

    private String buildIpScope(String ip) {
        if (ip == null || ip.isBlank()) {
            return "unknown";
        }
        if (ip.contains(".")) {
            String[] parts = ip.split("\\.");
            if (parts.length >= 3) {
                return parts[0] + "." + parts[1] + "." + parts[2];
            }
            return ip;
        }
        if (ip.contains(":")) {
            String[] parts = ip.split(":");
            if (parts.length >= 4) {
                return parts[0] + ":" + parts[1] + ":" + parts[2] + ":" + parts[3];
            }
            return ip;
        }
        return ip;
    }

    public record IssuedToken(String token, long expiresAtEpochSeconds) {}

    public record ValidationResult(boolean valid, boolean expired, boolean wrongNetwork, boolean wrongType, long expiresAtEpochSeconds) {
        static ValidationResult valid(long exp) { return new ValidationResult(true, false, false, false, exp); }
        static ValidationResult expiredResult() { return new ValidationResult(false, true, false, false, 0); }
        static ValidationResult wrongNetworkResult() { return new ValidationResult(false, false, true, false, 0); }
        static ValidationResult wrongTypeResult() { return new ValidationResult(false, false, false, true, 0); }
        static ValidationResult invalid() { return new ValidationResult(false, false, false, false, 0); }
    }
}

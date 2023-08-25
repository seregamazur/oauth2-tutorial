package com.seregamazur.oauth2.tutorial.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.seregamazur.oauth2.tutorial.utils.SecurityUtils;

import de.taimos.totp.TOTP;
import io.lettuce.core.api.StatefulRedisConnection;

@Component
public class TwoFactorService {

    private StatefulRedisConnection<String, String> redisConnection;

    public TwoFactorService(StatefulRedisConnection<String, String> redisConnection) {
        this.redisConnection = redisConnection;
    }

    public void storeTempSecret(String sessionId, String secretKey) {
        Duration expirationDuration = Duration.ofMinutes(30);
        redisConnection.sync().setex(sessionId, expirationDuration.getSeconds(), secretKey);
    }

    public String getCurrentTempSecret() {
        return SecurityUtils.getCurrentUserLogin()
            .map(l -> redisConnection.sync().get(l))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username " + SecurityUtils.getCurrentUserLogin() + " doesn't have temp 2fa key"));
    }

    private static final String APP_NAME = "OAuth2 Tutorial";

    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public String getGoogleAuthenticatorBarCode(String secretKey, String email) {
        return "otpauth://totp/"
            + URLEncoder.encode(APP_NAME + ":" + email, StandardCharsets.UTF_8).replace("+", "%20")
            + "?secret=" + URLEncoder.encode(secretKey, StandardCharsets.UTF_8).replace("+", "%20")
            + "&issuer=" + URLEncoder.encode(APP_NAME, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public byte[] createQRCode(String barCodeData, int height, int width)
        throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
            width, height);
        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(matrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        return baos.toByteArray();
    }

    public void infinityGeneratingCodes(String secretKey) {
        String lastCode = null;
        while (true) {
            String code = getTOTPCode(secretKey);
            if (!code.equals(lastCode)) {
                System.out.println(code);
            }
            lastCode = code;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            ;
        }
    }
}

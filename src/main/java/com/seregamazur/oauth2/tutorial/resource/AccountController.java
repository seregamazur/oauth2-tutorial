package com.seregamazur.oauth2.tutorial.resource;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.google.zxing.WriterException;
import com.seregamazur.oauth2.tutorial.crud.ResponseUtil;
import com.seregamazur.oauth2.tutorial.crud.UserDTO;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.service.TwoFactorService;
import com.seregamazur.oauth2.tutorial.service.UserService;
import com.seregamazur.oauth2.tutorial.utils.SecurityUtils;

@Controller
public class AccountController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TwoFactorService twoFactorService;

    public AccountController(UserService userService, UserRepository userRepository, TwoFactorService twoFactorService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.twoFactorService = twoFactorService;
    }

    @GetMapping("/api/v1/account")
    public ResponseEntity<UserDTO> getUser() {
        Optional<UserDTO> user = userService.getUser();
        return ResponseUtil.wrapOrNotFound(user);
    }

    @Transactional
    @GetMapping(value = "/api/v1/enable-2fa")
    public ResponseEntity<byte[]> generateTwoFactorCode() throws IOException, WriterException {
        String secretKey = twoFactorService.generateSecretKey();
        String currentUserEmail = SecurityUtils.getCurrentUserLogin().get();
        twoFactorService.storeTempSecret(currentUserEmail, secretKey);
        String barCodeUrl = twoFactorService.getGoogleAuthenticatorBarCode(secretKey, currentUserEmail);
        byte[] qrCode = twoFactorService.createQRCode(barCodeUrl, 200, 200);
        return ResponseEntity.ok()
            .header("Content-Type", "image/png")
            .body(qrCode);
    }

    //    @Transactional
//    @PostMapping(value = "/api/v1/disable-2fa")
//    public ResponseEntity<UserDTO> generateTwoFactorCode(@RequestBody UserDTO userDTO) throws URISyntaxException, IOException, WriterException {
//        String secretKey = "QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK";
//        String companyName = "Awesome Company";
//        String barCodeUrl = TwoFactorUtils.getGoogleAuthenticatorBarCode(secretKey, userDTO.getEmail(), companyName);
//        System.out.println(barCodeUrl);
//        TwoFactorUtils.createQRCode(barCodeUrl, "QR.png", 400, 400);
//        return ResponseEntity.created(new URI("/api/v1/register/" + registeredUser.getId()))
//            .body(registeredUser);
//    }
//
    @Transactional
    @PostMapping(value = "/api/v1/verify-2fa")
    public ResponseEntity<UserDTO> verifyTwoFactorCode(@RequestParam("totpCode") String twoFactorCode) {
        String tempSecret = twoFactorService.getCurrentTempSecret();
        String totpCode = twoFactorService.getTOTPCode(tempSecret);
        if (twoFactorCode.equals(totpCode)) {
            return ResponseUtil.wrapOrNotFound(userService.enableTwoFactorCode(tempSecret));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid temporary one time password");
        }
    }
}

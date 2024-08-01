package com.demo.twoFA;

import com.demo.entity.User;
import com.demo.repository.UserRepository;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TwoFactorAuthenticationServiceImpl implements TwoFactorAuthenticationService{


    @Override
    public String generateSecret() {

        return new  DefaultSecretGenerator().generate() ;
    }

    @Override
    public String generateQrCodeImageUri(String secret) {
        QrData data = new QrData.Builder()
                .label("QR for 2FA")
                .secret(secret)
                .issuer("New Application")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30) // valid period in seconds
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0]; // array that will hold the generated Qr
        try{
            imageData = generator.generate(data);
        }catch (QrGenerationException e) {
            System.out.println("Error while generating code");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Utils.getDataUriForImage(imageData, generator.getImageMimeType() );
    }

    @Override
    public boolean isOtpValid(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }




}

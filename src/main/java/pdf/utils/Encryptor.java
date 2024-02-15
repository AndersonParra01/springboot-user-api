package pdf.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Encryptor implements PasswordEncoder {

    private SecretKey secretKey;

    public Encryptor(SecretKey secretKey) {

        this.secretKey = secretKey;

    }

    @Override
    public String encode(CharSequence rawPassword) {

        try {

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] bytes = cipher.doFinal(rawPassword.toString().getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(bytes);

        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        try {

            String decodePassword = this.decode(encodedPassword);

            return decodePassword.contentEquals(rawPassword);

        } catch (Exception ex) {
            ex.printStackTrace();

            return false;

        }

    }

    private String decode(String encryptedData) {

        try {

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)));

        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }
    }

}
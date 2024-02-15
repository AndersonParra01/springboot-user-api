package pdf.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyGenerator {

    public static SecretKey key(int keySize, String password)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        byte[] salt = new byte[100];
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, keySize);
        SecretKey pbeKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);

        return new SecretKeySpec(pbeKey.getEncoded(), "AES");
    }

    public static SecretKey randomKey(int keySize, String password) throws Exception {

        byte[] salt = new byte[100];

        SecureRandom secureRandom = new SecureRandom();

        secureRandom.nextBytes(salt);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, keySize);
        SecretKey pbeKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);

        return new SecretKeySpec(pbeKey.getEncoded(), "AES");

    }

    // JWT

    public static SecretKey jwtKey(int keySize, String secret) throws Exception {

        byte[] salt = new byte[100];

        PBEKeySpec pbeKeySpec = new PBEKeySpec(secret.toCharArray(), salt, 1000, 256);

        SecretKey pbKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);

        return new SecretKeySpec(pbKey.getEncoded(), "HmacSHA256");

    }

    public static SecretKey jwtRandomKey(int keySize, String secret) throws Exception {

        byte[] salt = new byte[100];

        SecureRandom secureRandom = new SecureRandom();

        secureRandom.nextBytes(salt);

        PBEKeySpec pbeKeySpec = new PBEKeySpec(secret.toCharArray(), salt, 1000, 256);

        SecretKey pbKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(pbeKeySpec);

        return new SecretKeySpec(pbKey.getEncoded(), "HmacSHA256");

    }

    public static SecretKey toJwtKey(String encodeKey) throws Exception {

        byte[] secretBytes = Base64.getDecoder().decode(encodeKey);

        return new SecretKeySpec(secretBytes, 0, secretBytes.length, "HmacSHA256");

    }

}
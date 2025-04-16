package org.mawulidev;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/*
 * Possible KEY_SIZE values are 128,192,256
 * Possible T_LEN values are 128,120,112,104,96
 * */
public class AES {
    SecretKey secretKey;
    private int KEY_SIZE = 128;
    private int T_LEN = 128;
    private byte[] IV;

    public void init() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        secretKey = generator.generateKey();
    }

    public String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] message = plainText.getBytes();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        IV = cipher.getIV();
        byte[] encryptedBytes = cipher.doFinal(message);
        return encode(encryptedBytes);
    }

    public String decrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] messageInByte = decode(message);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        byte[] decryptedBytes = cipher.doFinal(messageInByte);
        return new String(decryptedBytes);
    }

    private String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] decode(String encrypted) {
        return Base64.getDecoder().decode(encrypted);
    }

    public void exportKeys(String[] args) {
        System.out.println("SecretKey : " + encode(secretKey.getEncoded()));
        System.out.println("IV : " + encode(IV));
    }

    public static void main(String[] args) {
        try{
            AES aes = new AES();
            aes.init();
            String encrypt = aes.encrypt("Hello World");
            String decrypt = aes.decrypt(encrypt);
            System.err.println(encrypt);
            System.err.println(decrypt);
            System.out.println("Encrypted Message : " + encrypt);
            System.out.println("Decrypted Message : " + decrypt);
        }catch (Exception ignored) {

        }
    }
}

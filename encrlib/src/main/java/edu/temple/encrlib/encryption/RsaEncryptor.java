package edu.temple.encrlib.encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class RsaEncryptor {

    private static final String DEFAULT_ALGORITHM = "RSA";
    private static final String DEFAULT_PROVIDER = "SHA1PRNG";

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(DEFAULT_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance(DEFAULT_PROVIDER);
        keyGen.initialize(2048, random);
        return keyGen.generateKeyPair();
    }

}
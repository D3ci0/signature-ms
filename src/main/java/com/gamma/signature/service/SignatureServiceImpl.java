package com.gamma.signature.service;

import org.springframework.stereotype.Service;

import java.security.*;

@Service("SignatureServiceImpl")
public class SignatureServiceImpl implements SignatureService{
    @Override
    public byte[] sign(byte[] input) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //Creating KeyPair generator object
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        //Initializing the KeyPairGenerator
        keyPairGen.initialize(2048);
        //Generating the pair of keys
        KeyPair pair = keyPairGen.generateKeyPair();
        //Getting the private key from the key pair
        PrivateKey privKey = pair.getPrivate();
        //Sign
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privKey);
        signature.update(input);

        return signature.sign();
    }
}

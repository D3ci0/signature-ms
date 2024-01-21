package com.gamma.signature.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface SignatureService {
    byte[] sign(byte[] input) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException;
}

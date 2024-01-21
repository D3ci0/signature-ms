package com.gamma.signature.controller;

import com.gamma.signature.service.SignatureService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@RestController
@RequestMapping("/v1")
public class SignatureController {
    private static final Logger logger = LoggerFactory.getLogger(SignatureController.class);
    private SignatureService signatureService;

    @PostMapping("/sign")
    public StreamingResponseBody signFile(HttpServletResponse response, MultipartFile file){
        try{
            logger.debug("Requested signature for file {} of size {}", file.getOriginalFilename(), file.getSize());
            response.setContentType("application/octet-stream");
            response.setHeader(
                    HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getOriginalFilename()+".sign" + "\"");

            return outputStream -> {
                try {
                    StreamUtils.copy(signatureService.sign(file.getBytes()), outputStream);
                } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            };
        }catch (Exception e){
            return outputStream -> {
                StreamUtils.copy(InputStream.nullInputStream(), outputStream);
            };
        }
    }

    @Autowired
    @Qualifier("signatureServiceImpl")
    public void setSignatureService(SignatureService signatureService) {
        this.signatureService = signatureService;
    }
}

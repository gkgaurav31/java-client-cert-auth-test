package com.gauk.clientcertauthtest.controller;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String getHeaders(@RequestHeader(value="Accept") String acceptType,
			@RequestHeader(value="Host") String host,
			@RequestHeader(value="Cache-Control", required=false) String cacheControl,
			@RequestHeader(value="User-Agent") String userAgent,
			@RequestHeader(value="X-ARR-ClientCert") String certB64 ) throws CertificateException, NoSuchAlgorithmException
	{

		byte encodedCert[] = Base64.getDecoder().decode(certB64);
		ByteArrayInputStream inputStream  =  new ByteArrayInputStream(encodedCert);

		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate)certFactory.generateCertificate(inputStream);

		String thumpprint = DatatypeConverter.printHexBinary(
		        MessageDigest.getInstance("SHA-1").digest(
		                cert.getEncoded())).toLowerCase();
		
		if(thumpprint.equals("CERTIFICATE_THUMBPRINT")) {
			return "Welcome!";
		}
		return "Sorry, authorization failure.";
	}

}

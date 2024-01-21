package com.gamma.signature;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignatureApplicationTests {
	private static final String REST_SERVICE_PORT_PATTERN = "########";
	private static final String REST_SERVICE_URI = "http://localhost:" + REST_SERVICE_PORT_PATTERN + "/v1/sign";
	private static final Logger logger = LoggerFactory.getLogger(SignatureApplicationTests.class);
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;
	@Test
	void signTest() {
		StringBuilder url = new StringBuilder(REST_SERVICE_URI.replaceAll(REST_SERVICE_PORT_PATTERN, Integer.toString(port)));
		logger.info("URL: {}", url);

		Resource resource = new ClassPathResource("prova.pdf");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file_name", "testFile");
		body.add("file", resource);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

		ResponseEntity<Resource> response = restTemplate.postForEntity(url.toString(), request, Resource.class);


		Assert.isTrue(response != null, "Response is null");
		Assert.isTrue(response.getBody() != null, "Response body is null");
		Assert.isTrue(response.getBody().getFilename() != null, "Response file name is null");
		Assert.isTrue(response.getBody().getFilename().equals("prova.pdf.sign"), "Expected name differs from actual name");
	}

}

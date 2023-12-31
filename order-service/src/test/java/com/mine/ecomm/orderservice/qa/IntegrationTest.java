package com.mine.ecomm.orderservice.qa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Tag("it")
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSuccessAddNewUser() {
        String url = "/order/rate/pthirumalayadav";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("rate", 4);
        ResponseEntity<String> responseEntityResponseEntity = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, null, String.class);
        Assertions.assertEquals(responseEntityResponseEntity.getStatusCode(), HttpStatus.OK, "Status code is mismatched");
    }

}

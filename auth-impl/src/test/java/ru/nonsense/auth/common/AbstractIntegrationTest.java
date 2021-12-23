package ru.nonsense.auth.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

@ActiveProfiles("test")
@SpringBootTest(
        classes = TestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(locations = "/application.properties")
public abstract class AbstractIntegrationTest extends AbstractTestNGSpringContextTests {
    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeMethod
    public void setUp() {
    }

    protected static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        return headers;
    }
}

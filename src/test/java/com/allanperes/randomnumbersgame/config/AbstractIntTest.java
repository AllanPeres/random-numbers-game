package com.allanperes.randomnumbersgame.config;

import com.allanperes.randomnumbersgame.RandomNumbersGameApplication;
import com.allanperes.randomnumbersgame.client.GameWebSockedClient;
import com.allanperes.randomnumbersgame.utils.GameTimer;
import com.allanperes.randomnumbersgame.utils.RandomProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {RandomNumbersGameApplication.class})
@ActiveProfiles("integration")
@AutoConfigureWebTestClient(timeout = "60000")
@ContextConfiguration(classes = IntegrationConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntTest {

    @Autowired
    protected GameWebSockedClient gameWebSockedClient;

    @Autowired
    protected GameTimer gameTimer;

    @Autowired
    protected RandomProvider randomProvider;

    @Autowired
    protected ObjectMapper objectMapper;
}

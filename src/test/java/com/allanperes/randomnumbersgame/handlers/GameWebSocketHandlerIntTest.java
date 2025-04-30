package com.allanperes.randomnumbersgame.handlers;

import com.allanperes.randomnumbersgame.config.AbstractIntTest;
import com.allanperes.randomnumbersgame.models.dto.GuessDto;
import com.allanperes.randomnumbersgame.testutils.TestGameTimer;
import com.allanperes.randomnumbersgame.testutils.TestRandomProvider;
import com.allanperes.randomnumbersgame.utils.GameData;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(OutputCaptureExtension.class)
public class GameWebSocketHandlerIntTest extends AbstractIntTest {

    @Autowired
    private GameData gameData;

    @Test
    void when_ConnectingToWebsocked_should_ReceiveWelcomingMessage(CapturedOutput capturedOutput) throws Exception {
        ((TestRandomProvider) randomProvider).setForcedValue(2);
        ((TestGameTimer) gameTimer).setTickingTimes(1);
        ((TestGameTimer) gameTimer).setTimeBeforeTicking(100);
        WebSocketSession session = gameWebSockedClient.connect();
        GuessDto guessDto = new GuessDto(2, BigDecimal.ONE);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(guessDto)));

        String log = "GameWebSockedClient         : received message - Game started! You have 10 seconds to give your guess!";

        assertThat(capturedOutput.getOut()).contains(log);

        session.close();
    }

    @Test
    void when_ConnectedToWebSocketAndDoACorrectBet_should_ReceiveYouWonMessages(CapturedOutput capturedOutput) throws Exception {
        ((TestRandomProvider) randomProvider).setForcedValue(2);
        ((TestGameTimer) gameTimer).setTickingTimes(1);
        ((TestGameTimer) gameTimer).setTimeBeforeTicking(100);
        WebSocketSession session = gameWebSockedClient.connect();
        GuessDto guessDto = new GuessDto(2, BigDecimal.valueOf(3));
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(guessDto)));

        var username = gameData.getUsers().stream().findFirst().orElseThrow().username();
        await().untilAsserted(() -> {

            String logConnection = "GameWebSockedClient         : received message - Game started! You have 10 seconds to give your guess!";
            String logWinning = "GameWebSockedClient         : received message - You won 29.7";
            String logWinners = "GameWebSockedClient         : received message - The winners are User: " + username + " Won: 29.7";

            assertThat(capturedOutput.getOut()).contains(logConnection);
            assertThat(capturedOutput.getOut()).contains(logWinning);
            assertThat(capturedOutput.getOut()).contains(logWinners);

        });

        session.close();
    }

    @Test
    void when_ConnectedToWebSocketAndDoAWrongBet_should_ReceiveYouLostMessages(CapturedOutput capturedOutput) throws Exception {
        ((TestRandomProvider) randomProvider).setForcedValue(2);
        ((TestGameTimer) gameTimer).setTickingTimes(1);
        ((TestGameTimer) gameTimer).setTimeBeforeTicking(100);
        WebSocketSession session = gameWebSockedClient.connect();
        GuessDto guessDto = new GuessDto(4, BigDecimal.ONE);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(guessDto)));

        await().untilAsserted(() -> {
            String logConnection = "GameWebSockedClient         : received message - Game started! You have 10 seconds to give your guess!";
            String logLost = "GameWebSockedClient         : received message - You lost, don't be sad try one more time!";
            String logNoWinners = "GameWebSockedClient         : received message - No winners this round! Better luck next time.";

            assertThat(capturedOutput.getOut()).contains(logConnection);
            assertThat(capturedOutput.getOut()).contains(logLost);
            assertThat(capturedOutput.getOut()).contains(logNoWinners);
        });

        session.close();
    }
}

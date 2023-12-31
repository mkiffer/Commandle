package edu.monash.commandle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class designed to test the correct output for win percentages
 */
public class WinPercentageTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private List<String> wordList;
    private ByteArrayOutputStream testOut;

    // Methods to capture and provide input/output streams
    private String getOutput() {
        return testOut.toString();
    }

    private void provideInput(String guess) {
        System.setIn(new ByteArrayInputStream(guess.getBytes()));
    }


    @BeforeEach
    void setUp() {
        wordList = new ArrayList<>();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

    }

    @AfterEach
    void tearDown() {
        // Restore the original input and output streams, clear data, and reset game statistics
        System.setIn(systemIn);
        System.setOut(systemOut);
        wordList.clear();
        Commandle.resetWinsAndLosses();
        Commandle.resetGamesPlayed();
    }
    /**
     *3.1A
     * Test the correct percentage is shown winning 1 game out of 1
     */
    @Test
    @DisplayName("Correct percentage shown if one game is won")
    void winningOneGame() {
        wordList.add("train");
        provideInput("train\nN\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "train", 1);
        String output = getOutput();
        assertTrue(output.contains(" 100%"));

    }
    /**
     *3.1B
     * Test the correct percentage is shown losing 1 game out of 1
     */
    @Test
    @DisplayName("Correct percentage shown if one game is lost")
    void losingOneGame() {
        wordList.add("hello");
        wordList.add("train");
        provideInput("train\nN\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello", 1);
        String output = getOutput();
        assertTrue(output.contains(" 0%"));

    }

    /**
     *3.3
     * Test the correct percentage is shown winning all game out of 4
     */
    @Test
    @DisplayName("Correct percentage shown if all games are won out of ten")
    void winningAllGamesOutOfTen() {
        for(int i = 0; i<10; i++){
            wordList.add("hello");
        }
        provideInput("""
                hello
                Y
                hello
                Y
                hello
                Y
                hello
                Y
                hello
                Y
                hello
                Y
                hello
                Y
                hello
                Y
                hello
                Y
                hello
                Y""");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello",6);
        String output = getOutput();
        String expectedOutput = """
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                you have reached the maximum number of games played today\r
                See you next time!\r
                """;
        assertEquals(output, expectedOutput);

    }


    /**
     *3.3
     * Test the correct percentage is shown winning one game out of ten
     */
    @Test
    @DisplayName("Correct percentage shown if one game is won out of ten")
    void winningOneGamesOutOfTen() {
        wordList.add("hello");
        wordList.add("train");
        provideInput("hello\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello", 1);
        String output = getOutput();
        String expectedOutput = """
                Please enter your guess: 1: hello  1: hello\r
                Congratulations, you won!\r
                Your daily win percentage is: 100%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 50%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 33%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 25%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 20%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 16%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 14%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 12%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 11%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 10%.\r
                Play again? (Y/N)\r
                you have reached the maximum number of games played today\r
                See you next time!\r
                """;
        assertEquals(output, expectedOutput);

    }

    /**
     *3.4
     * Test the correct percentage is shown winning no game out of 10
     */
    @Test
    @DisplayName("Correct percentage shown if no games are won out of ten")
    void winningNoGamesOutOfTen() {
        wordList.add("hello");
        wordList.add("train");
        provideInput("train\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello", 1);
        String output = getOutput();
        String expectedOutput = """
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                Please enter your guess: 1: train  1: #####\r
                Sorry, you lost!\r
                correct word was hello\r
                Your daily win percentage is: 0%.\r
                Play again? (Y/N)\r
                you have reached the maximum number of games played today\r
                See you next time!\r
                """;
        assertEquals(output, expectedOutput);

    }


}

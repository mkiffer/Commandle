package edu.monash.commandle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class WinPercentageTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private List<String> wordList;
    private ByteArrayOutputStream testOut;
    private ByteArrayOutputStream errOut;

    private String getOutput() {
        return testOut.toString();
    }

    private String getError() {
        return errOut.toString();
    }

    private void provideInput(String guess) {
        System.setIn(new ByteArrayInputStream(guess.getBytes()));
    }


    @BeforeEach
    void setUp() {
        wordList = new ArrayList<String>();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

    }

    @AfterEach
    void tearDown() {
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
    void losingOneGame() {
        wordList.add("hello");
        wordList.add("train");
        provideInput("train\nN\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello", 1);
        String output = getOutput();
        assertTrue(output.contains(" 0%"));

    }
    /**
     *3.2
     * Test the correct percentage is shown winning all game out of 4
     */
    @Test
    void winningAllGamesOutOfFour() {
        for(int i = 0; i<4; i++){
            wordList.add("hello");
        }
        provideInput("hello\nY\nhello\nY\nhello\nY\nhello\nN");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello",6);
        String output = getOutput();
        String expectedOutput = "Please enter your guess: 1: hello  1: hello\r\nCongratulations, you won!" +
                "\r\nYour daily win percentage is: 100%.\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: " +
                "hello  1: hello\r\nCongratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: hello  1: hello\r\nCongratulations, you won!\r\nYour daily win " +
                "percentage is: 100%.\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: hello  1: hello\r\n" +
                "Congratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? (Y/N)\r\n" +
                "See you next time!\r\n";
        assertTrue(output.equals(expectedOutput));

    }
    /**
     *3.3
     * Test the correct percentage is shown winning all game out of 4
     */
    @Test
    void winningAllGamesOutOfTen() {
        for(int i = 0; i<10; i++){
            wordList.add("hello");
        }
        provideInput("hello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY" +
                "\nhello\nY\nhello\nY\nhello\nY\nhello\nY");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello",6);
        String output = getOutput();
        String expectedOutput = "Please enter your guess: 1: hello  1: hello\r\nCongratulations, you won!" +
                "\r\nYour daily win percentage is: 100%.\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: " +
                "hello  1: hello\r\nCongratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: hello  1: hello\r\nCongratulations, you won!\r\nYour daily win " +
                "percentage is: 100%.\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: hello  1: hello\r\n" +
                "Congratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: " +
                "hello  1: hello\r\nCongratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: " +
                "hello  1: hello\r\nCongratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: " +
                "hello  1: hello\r\nCongratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: " +
                "hello  1: hello\r\nCongratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: " +
                "hello  1: hello\r\nCongratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: " +
                "hello  1: hello\r\nCongratulations, you won!\r\nYour daily win percentage is: 100%.\r\nPlay again? " +
                "(Y/N)" +
                "\r\nyou have reached the maximum number of games played today\r\nSee you next time!\r\n";
        assertTrue(output.equals(expectedOutput));

    }

    /**
     *3.4
     * Test the correct percentage is shown winning one game out of 4
     */
    @Test
    void winningOneGamesOutOfFour() {
        wordList.add("hello");
        wordList.add("train");
        provideInput("hello\nY\ntrain\nY\ntrain\nY\ntrain\nN");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello", 1);
        String output = getOutput();
        String expectedOutput = "Please enter your guess: 1: hello  1: hello\r\nCongratulations, you won!" +
                "\r\nYour daily win percentage is: 100%.\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: " +
                "train  1: #####\r\nSorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 50%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: train  1: #####\r\nSorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 33%." +
                "\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 25%.\r\nPlay again? (Y/N)\r\n" +
                "See you next time!\r\n";
        assertTrue(output.equals(expectedOutput));

    }
    /**
     *3.5
     * Test the correct percentage is shown winning one game out of ten
     */
    @Test
    void winningOneGamesOutOfTen() {
        wordList.add("hello");
        wordList.add("train");
        provideInput("hello\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello", 1);
        String output = getOutput();
        String expectedOutput = "Please enter your guess: 1: hello  1: hello\r\nCongratulations, you won!" +
                "\r\nYour daily win percentage is: 100%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 50%.\r\nPlay again? " +
                "(Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\n" +
                "correct word was hello\r\nYour daily win percentage is: 33%." +
                "\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 25%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 20%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 16%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 14%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 12%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 11%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 10%.\r\nPlay again? (Y/N)\r\n" +
                "you have reached the maximum number of games played today\r\n" +
                "See you next time!\r\n";
        assertTrue(output.equals(expectedOutput));

    }
    /**
     *3.6
     * Test the correct percentage is shown winning no game out of 4
     */
    @Test
    void winningNoGamesOutOfFour(){
        wordList.add("hello");
        wordList.add("train");
        provideInput("train\nY\ntrain\nY\ntrain\nY\ntrain\nN");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello", 1);
        String output = getOutput();
        String expectedOutput = "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: " +
                "train  1: #####\r\nSorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? " +
                "(Y/N)\r\nPlease enter your guess: 1: train  1: #####\r\nSorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%." +
                "\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "See you next time!\r\n";
        assertTrue(output.equals(expectedOutput));

    }
    /**
     *3.7
     * Test the correct percentage is shown winning no game out of 10
     */
    @Test
    void winningNoGamesOutOfTen() {
        wordList.add("hello");
        wordList.add("train");
        provideInput("train\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY\ntrain\nY");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello", 1);
        String output = getOutput();
        String expectedOutput = "Please enter your guess: 1: train  1: #####\r\nSorry, you lost!\r\ncorrect word was hello" +
                "\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? " +
                "(Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%." +
                "\r\nPlay again? (Y/N)\r\nPlease enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "Please enter your guess: 1: train  1: #####\r\n" +
                "Sorry, you lost!\r\ncorrect word was hello\r\nYour daily win percentage is: 0%.\r\nPlay again? (Y/N)\r\n" +
                "you have reached the maximum number of games played today\r\nSee you next time!\r\n";
        assertTrue(output.equals(expectedOutput));

    }


}

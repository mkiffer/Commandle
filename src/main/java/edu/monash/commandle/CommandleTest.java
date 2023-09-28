package edu.monash.commandle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandleTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream errOut;


    private List<String> wordList;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() {
        wordList = new ArrayList<String>();
        testOut = new ByteArrayOutputStream();
        errOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
        Commandle.resetWinsAndLosses();
        Commandle.resetGamesPlayed();
    }

    private String getOutput() {
        return testOut.toString();
    }

    //game expires at the correct number of attempts
    @Test
    /**
     * 1C.1
     * Test to ensure game expires at the correct round
     */
    void gameExpiresAtCorrectGuess(){
        wordList.add("pouty");
        provideInput("pouty\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard", 1);
        String output = getOutput();
        assertTrue(output.contains("you lost"), "Game expires after correct number of guesses.");

    }

    /**
     * 6.2
     * Test to ensure game can be won on middle guess
     */
    @Test
    void gameWonOnMiddleGuess(){
        wordList.add("shark");
        wordList.add("shard");
        provideInput("shark\nshard\nN\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard", 3);
        String output = getOutput();
        assertTrue(output.contains("you won"), "Game can be one on the middle guess.");
    }
    /**
     * 6.3
     * Test to ensure game can be won on last guess
     */
    @Test
    void gameWonOnLastGuess(){
        wordList.add("shark");
        wordList.add("shard");
        wordList.add("shift");
        wordList.add("shaft");
        wordList.add("short");
        wordList.add("shirt");
        provideInput("shark\nshirt\nshaft\nshift\nshort\nshard\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard", 6);
        String output = getOutput();
        assertTrue(output.contains("you won"), "Game can be won on last guess.");
    }
    /**
     * 2.1
     * Test to ensure game can only be played 10 times before midnight
     */
    @Test
    void maxGamesPlayed(){
        for(int i = 0; i<10; i++){
            wordList.add("hello");
        }
        wordList.add("toomy");
        provideInput("hello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY" +
                "\nhello\nY\nhello\nY\nhello\nY\nhello\nY\ntoomy\nY");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello",1);
        String output = getOutput();
        assertTrue(!output.contains("toomy"), "Game does not allow 11 rounds.");

    }

    /**
     * 2.2
     * Test to ensure gamecount resets after midnight
     */

    @Test
    void midnightGamePlayed(){
        wordList.add("hello");
        wordList.add("hello");
        provideInput("hello\nY\nhello\nN");
        LocalDate currentDate = LocalDate.now();
        LocalDateTime endOfDay = LocalTime.MAX.atDate(currentDate);
        GameBoard gameBoard = new GameBoard(wordList);
        Scanner scanner = new Scanner(System.in);
        LocalDateTime justBeforeEndOfDay = endOfDay.minus(5, ChronoField.MILLI_OF_DAY.getBaseUnit());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm:ss");
        Commandle.gameLoopWithTarget(currentDate, justBeforeEndOfDay,endOfDay,scanner,gameBoard,System.out, "hello",6);


        String output = getOutput();
        assertTrue(Commandle.getGameCount() == 1, "Game counter resets after midnight.");
    }
    /**
     * 4.1a
     * Test to ensure target word is removed after winning
     */
    @Test
    void usedWordsAreRemovedWhenWon(){
        wordList.add("hello");
        wordList.add("train");
        provideInput("hello\nN");
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endOfDay = LocalTime.MAX.atDate(currentDate);
        GameBoard gameBoard = new GameBoard(wordList);
        Scanner scanner = new Scanner(System.in);
        Commandle.gameLoopWithTarget(currentDate, currentTime,endOfDay,scanner,gameBoard,System.out, "hello",6);
        String output = getOutput();
        //final guess should not go through
        assertTrue(!wordList.contains("hello"), "Used words are removed from wordlist when game is won.");

    }
    /**
     * 4.1b
     * Test to ensure target word is removed after losing
     */
    @Test
    void usedWordsAreRemovedWhenLost(){
        wordList.add("hello");
        wordList.add("train");
        provideInput("hello\nN");
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endOfDay = LocalTime.MAX.atDate(currentDate);
        GameBoard gameBoard = new GameBoard(wordList);
        Scanner scanner = new Scanner(System.in);

        Commandle.gameLoopWithTarget(currentDate, currentTime,endOfDay,scanner,gameBoard,System.out, "train",1);

        assertTrue(!wordList.contains("train"),"Used words are removed from wordlist when game is lost.");

    }

    @Test
    void gameCanBeWon(){

        // set up a one-word list for easy testing
        String target = "brave";
        wordList.add(target);

        // provide the correct guess, and then followed by "N" to signal not wanting to play again
        provideInput(target + "\nN");

        // simulate the gameplay start
        Commandle.start(System.in, System.out, wordList);

        // get output
        String result = getOutput();

        // verify that the output contains the word "won"
        assertTrue(result.contains("won"), "Game can be won");
    }
    private String getError() {
        return errOut.toString();
    }



    @Test
    void fileNotFoundErrorMessage(){
        String[] args = new String[1];
        args[0] = "noFile.txt";
        System.setErr(new PrintStream(errOut));
        Commandle.start(systemIn, systemOut, args);
        assertEquals("This file cannot be found", getError());

    }

    @Test
    void fileIsEmptyErrorMessage() {
        String[] args = new String[1];
        args[0] = "blank.txt";
        System.setErr(new PrintStream(errOut));
        Commandle.start(systemIn, systemOut, args);
        String output = getError();
        assertEquals("This file is empty", output);
    }

    @Test
    void needMoreInputErrorMessage(){
        System.setErr(new PrintStream(errOut));
        provideInput("train\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getError();
        assertTrue(output.equals("Need more input\r\n"));
    }

    @Test
    void timeDelayInterruptedExceptionHandled(){

        assertThrowsExactly(AssertionError.class, ()->{
            Thread.currentThread().interrupt();
            Commandle.timeDelay();
        });
    }

    @Test
    void assertionErrorCaught(){
        wordList.add("hello");
        wordList.add("train");
        provideInput("hello\nY\ntrain\n");
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime endOfDay = LocalTime.MAX.atDate(currentDate);
        GameBoard gameBoard = new GameBoard(wordList);
        Scanner scanner = new Scanner(System.in);
        System.setErr(new PrintStream(errOut));
        String output = errOut.toString();

        Thread.currentThread().interrupt();
        Commandle.gameLoop(currentDate,currentTime,endOfDay,scanner,gameBoard,System.out,6);
        assertTrue(output.contains("Time delay thread was interrupted, which is unexpected. Closing game..."));
    }




    private void provideInput(String guess) {
        System.setIn(new ByteArrayInputStream(guess.getBytes()));
    }
}
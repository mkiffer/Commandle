package edu.monash.commandle;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CommandleTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private final ByteArrayOutputStream errOut = new ByteArrayOutputStream();


    private final List<String> wordList = new ArrayList<>();
    private final ByteArrayOutputStream testOut = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(testOut));
        System.setErr(new PrintStream(errOut));

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
    /**
     * 1C.1
     * Test to ensure game expires at the correct round
     */
    @Test
    @DisplayName("Game expires at correct guess")
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
    @DisplayName("Game can be won on middle guess")

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
    @DisplayName("Game can be won on last guess")
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
    @DisplayName("Game can be only be played 10 times in one day")
    void maxGamesPlayed(){
        for(int i = 0; i<10; i++){
            wordList.add("hello");
        }
        wordList.add("toomy");
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
                Y
                toomy
                Y""");
        Commandle.startWithTarget(System.in, System.out, wordList, "hello",1);
        String output = getOutput();
        assertFalse(output.contains("toomy"), "Game does not allow 11 rounds.");

    }

    /**
     * 2.2
     * Test to ensure gamecount resets after midnight
     */

    @RepeatedTest(10)
    @DisplayName("Game count will reset after midnight")
    void midnightGamePlayed(){
        for(int i = 0; i<11; i++){
            wordList.add("hello");

        }
        provideInput("hello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nY\nhello\nN");
        LocalDate currentDate = LocalDate.now();
        LocalDateTime endOfDay = LocalTime.MAX.atDate(currentDate);
        GameBoard gameBoard = new GameBoard(wordList);
        Scanner scanner = new Scanner(System.in);
        LocalDateTime justBeforeEndOfDay = endOfDay.minus(5, ChronoField.MILLI_OF_DAY.getBaseUnit());
        Commandle.gameLoopWithTarget(currentDate, justBeforeEndOfDay,endOfDay,scanner,gameBoard,System.out, "hello",6);
        int count = Commandle.getGameCount();

        assertTrue( count<11 , "Game counter resets after midnight.");
    }








            /**
             * 4.1a
             * Test to ensure target word is removed after winning
             */
    @Test
    @DisplayName("Words are removed from word list when game is won")
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
        //final guess should not go through
        assertFalse(wordList.contains("hello"), "Used words are removed from wordlist when game is won.");

    }
    /**
     * 4.1b
     * Test to ensure target word is removed after losing
     */
    @Test
    @DisplayName("Words are removed from word list when game is lost")

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

        assertFalse(wordList.contains("train"), "Used words are removed from wordlist when game is lost.");

    }

    @RepeatedTest(2)
    @DisplayName("Word list is refreshed when a new game is started")
    void wordListRefreshesOnNewSession(){
        provideInput("hello\nN\n");
        String[] args = new String[1];
        args[0] = "wordsRemovedTest.txt";
        Commandle.start(System.in, System.out, args);
        String output = getOutput();
        assertTrue(output.contains("See you next time!"));
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
    @DisplayName("Game prints error message if non-existent file is passed")

    void fileNotFoundErrorMessage(){
        String[] args = new String[1];
        args[0] = "noFile.txt";
        Commandle.start(systemIn, systemOut, args);
        assertEquals("Input file cannot be found", getError());

    }

    @Test
    @DisplayName("Game prints error message if blank file is passed")

    void fileIsEmptyErrorMessage() {
        String[] args = new String[1];
        args[0] = "blank.txt";
        Commandle.start(systemIn, systemOut, args);
        assertEquals("Input file is empty", getError());
    }

    @Test
    @DisplayName("Game prints error message if scanner gets no input")

    void needMoreInputErrorMessage(){
        wordList.add("train");
        provideInput("train\n");
        Commandle.start(System.in, System.out, wordList);
        String output = getError();
        assertEquals("Need more input\r\n", output);
    }


    private void provideInput(String guess) {
        System.setIn(new ByteArrayInputStream(guess.getBytes()));
    }
}
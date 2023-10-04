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

import static org.junit.jupiter.api.Assertions.assertTrue;


public class InputValidationTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private List<String> wordList;
    private ByteArrayOutputStream testOut;
    private ByteArrayOutputStream errOut;



    @BeforeEach
    void setUp() {
        wordList = new ArrayList<>();
        wordList.add("shard");
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

    private String getOutput() {
        return testOut.toString();
    }
    private String getError() {
        return errOut.toString();
    }

    private void provideInput(String guess) {
        System.setIn(new ByteArrayInputStream(guess.getBytes()));
    }

    /**
     * 1B.1
     * Test to ensure game will accept all capital letters where none are correct.
     * */
    @Test
    @DisplayName("All capital letters with incorrect position")
    void allCapitalLetterInputWrongPosition(){
        wordList.add("pouty");
        provideInput("POUTY\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("#####"));
    }
    /**
     * 1B.2
     * Test to ensure game will accept all capital letters where one is correct.
     * */
    @Test
    @DisplayName("All capital letters with one correct position")
    void allCapitalLetterInputOneCorrect(){
        wordList.add("slope");
        provideInput("SLOPE\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("s####"));
    }
    /**
     * 1B.3
     * Test to ensure game will accept all capital letters where one is partially correct.
     * */
    @Test
    @DisplayName("All capital letters with one partial correct position")
    void allCapitalLetterInputOnePartial(){
        wordList.add("crone");
        provideInput("CRONE\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("#?###"));
    }
    /**
     * 1A.9
     * Test to ensure game ignores trailing whitespace
     * */
    @Test
    @DisplayName("Trailing whitespace ignored")
    void trailingWhitespace(){
        wordList.add("pouty");
        provideInput("pouty    \n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("#####"));
    }
    /**
     * 1A.10
     * Test to ensure game ignores leading whitespace
     * */
    @Test
    @DisplayName("Leading whitespace ignored")
    void leadingWhitespace(){
        wordList.add("pouty");
        provideInput("    pouty\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("#####"));
    }
    /**
     * 1A.11
     * Test to ensure game will not accept numeric characters
     * */
    @Test
    @DisplayName("Numeric character input rejected")
    void numericCharacter(){
        errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));
        wordList.add("pouty");
        provideInput("pout1\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getError();
        assertTrue(output.contains("Please enter a valid word: "));
    }
    /**
     * 1A.12
     * Test to ensure game will not accept special characters
     * */
    @Test
    @DisplayName("Non-alphanumeric character input rejected")
    void nonAlphaNumericCharacter(){
        errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));
        wordList.add("pouty");
        provideInput("pout!\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getError();
        assertTrue(output.contains("Please enter a valid word: "));
    }
    /**
     * 5.1
     * Test to ensure game will not accept four letter words
     * */
    @Test
    @DisplayName("Four letter word rejected")
    void fourLetterWord(){
        errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));
        provideInput("shar\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getError();
        assertTrue(output.contains("Please enter a word of 5 letters: "));
    }

    /**
     * 5.2
     * Test to ensure game will not accept words not in the wordlist
     * */
    @Test
    @DisplayName("Word not on list rejected")
    void fiveLetterInvalidWord(){
        errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));
        provideInput("champ\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getError();
        assertTrue(output.contains("Please enter a valid word: "));
    }

    /**
     * 5.3
     * Test to ensure game will not accept six letter words
     * */
    @Test
    @DisplayName("Six letter word rejected")
    void sixLetterWord(){
        errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));
        provideInput("shardy\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getError();
        assertTrue(output.contains("Please enter a word of 5 letters: "));
    }

    /**
     * 5.4
     * Test to ensure game will not accept words that have already been used in a round
     * */
    @Test
    @DisplayName("Repeated word rejected")
    void repeatedWord(){
        errOut = new ByteArrayOutputStream();
        wordList.add("train");
        System.setErr(new PrintStream(errOut));
        provideInput("train\ntrain\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getError();
        assertTrue(output.contains("Please enter a new word: "));
    }
    /**
     * 5.5.b
     * Test to ensure game will end when "N" is provided.
     * */
    @Test
    @DisplayName("N char will end game")
    void noWillEndGame(){
        wordList.add("train");
        provideInput("train\nN\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "train",1);
        String output = getOutput();
        assertTrue(output.contains("See you next time"));

    }
    /**
     * 5.5.a
     * Test to ensure game will not accept anything other than Y or N at end of game.
     * */
    @Test
    @DisplayName("Char other than Y or N will not be accepted")
    void charOtherThanYesOrNo(){
        errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));

        wordList.add("train");
        provideInput("train\nX\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "train",6);
        String output = getError();
        assertTrue(output.contains("Please enter Y or N\r\n"));

    }

}

package edu.monash.commandle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
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
        wordList = new ArrayList<String>();
        wordList.add("shard");
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
    void allCapitalLetterInputOnePartial(){
        wordList.add("crone");
        provideInput("CRONE\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("#?###"));
    }
    /**
     * 1B.4
     * Test to ensure game will accept capital letters that are incorrect.
     * */
    @Test
    void oneCapitalLetterInputWrongPosition(){
        wordList.add("pouty");
        provideInput("Pouty\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("#####"));
    }
    /**
     * 1B.5
     * Test to ensure game will accept capital letters that are correct.
     * */
    @Test
    void oneCapitalLetterInputOneCorrect(){
        wordList.add("slope");
        provideInput("Slope\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("s####"));
    }
    /**
     * 1B.6
     * Test to ensure game will accept capital letters that are partially correct.
     * */
    @Test
    void oneCapitalLetterInputOnePartial(){
        wordList.add("crone");
        provideInput("cRone\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getOutput();
        assertTrue(output.contains("#?###"));
    }
    /**
     * 1A.9
     * Test to ensure game ignores trailing whitespace
     * */
    @Test
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
     * Test to ensure game will not accept words with spaces
     * */
    @Test
    void fiveLetterWordWithSpace(){
        errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));
        provideInput("sha rd\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "shard",6);
        String output = getError();
        assertTrue(output.contains("Please enter a word of 5 letters: "));
    }
    /**
     * 5.5
     * Test to ensure game will not accept words that have already been used in a round
     * */
    @Test
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
     * 8D.1
     * Test to ensure game will end when "N" is provided.
     * */
    @Test
    void noWillEndGame(){
        wordList.add("train");
        provideInput("train\nN\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "train",1);
        String output = getOutput();
        assertTrue(output.contains("See you next time"));

    }
    /**
     * 8D.2
     * Test to ensure game will not accept anything other than Y or N at end of game.
     * */
    @Test
    void charOtherThanYesOrNoAtEndOfGame(){
        errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));

        wordList.add("train");
        provideInput("train\nX\n");
        Commandle.startWithTarget(System.in, System.out, wordList, "train",6);
        String output = getError();
        assertTrue(output.contains("Please enter Y or N\r\n"));

    }

}

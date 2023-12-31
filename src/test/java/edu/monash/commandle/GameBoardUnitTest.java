package edu.monash.commandle;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static edu.monash.commandle.GameBoard.Status.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for the gameboard class. Testing the ability to correctly identify letters.
 */
class GameBoardUnitTest {
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        Commandle.resetWinsAndLosses();
        Commandle.resetGamesPlayed();
    }



    /**
     * 1A.1
     * Test case to verify that all letters are in the wrong position.
     */
    @Test
    @DisplayName("All letters wrong")
    void allLetterWrongPosition() {
        gameBoard = new GameBoard("shard");
        char[] guess = "pouty".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                wrong, wrong, wrong, wrong, wrong};

        assertArrayEquals(expected, result);
    }

    /**
     * 1A.2
     * Test case to verify that one letter is correct in the guess but in the wrong position.
     */
    @Test
    @DisplayName("One letter partially correct")
    void oneCorrectLetterWrongPostion() {
        gameBoard = new GameBoard("shard");
        char[] guess = "crone".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                wrong, partial, wrong, wrong, wrong};

        assertArrayEquals(expected, result);
    }

    /**
     * 1A.3
     * Test case to verify that one letter is correct in the guess and in the correct position.
     */
    @Test
    @DisplayName("One letter correct")
    void OneCorrectLetterCorrectPosition() {
        gameBoard = new GameBoard("shard");
        char[] guess = "slope".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                correct, wrong, wrong, wrong, wrong};

        assertArrayEquals(expected, result);
    }

    /**
     * 1A.4
     * Test case to verify that all letters in the guess are correct but in the wrong positions.
     */
    @Test
    @DisplayName("All letter correct but in incorrect position")
    void allLettersCorrectButIncorrectPosition() {
        gameBoard = new GameBoard("angle");
        char[] guess = "glean".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                partial, partial, partial, partial, partial};

        assertArrayEquals(expected, result);
    }

    /**
     * 1A.5
     * Test case to verify that there is a double letter in the guess,
     * one being correct and one being wrong.
     */
    @Test
    @DisplayName("Double Letter: one not in word, one partially correct")
    void doubleLetterOnePartialOneWrong() {
        gameBoard = new GameBoard("chant");
        char[] guess = "dotty".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                wrong, wrong, partial, wrong, wrong};

        assertArrayEquals(expected, result);
    }

    /**
     * 1A.6
     * Test case to verify that there is a double letter in the guess, one being correct and one being partial.
     */
    @Test
    @DisplayName("Double Letter: one correct, one partially correct")
    void doubleLetterOneCorrectOnePartial() {
        gameBoard = new GameBoard("drama");
        char[] guess = "alpha".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                partial, wrong, wrong, wrong, correct};

        assertArrayEquals(expected, result);
    }

    /**
     * 1A.7
     * Test case to verify that there is a double letter in the guess, both being partial.
     */
    @Test
    @DisplayName("Double Letter: both partially correct")
    void doubleLetterBothPartial() {
        gameBoard = new GameBoard("start");
        char[] guess = "dotty".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                wrong, wrong, partial, partial, wrong};

        assertArrayEquals(expected, result);
    }

    /**
     * 1A.8
     * Test case to verify that there is a double letter in the guess, one being correct and one being wrong.
     */
    @Test
    @DisplayName("Double Letter: one correct, one not in word")
    void doubleLetterOneCorrectOneWrong() {
        gameBoard = new GameBoard("chant");
        char[] guess = "drama".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                wrong, wrong, correct, wrong, wrong};

        assertArrayEquals(expected, result);
    }

    /**
     * 1A.13
     * Test case to verify that all letters in the guess are correct
     */
    @Test
    @DisplayName("All letters are correct")
    void allLettersAreCorrect() {
        gameBoard = new GameBoard("shard");
        char[] guess = "shard".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                correct,correct,correct,correct,correct};
        assertArrayEquals(expected, result);
    }

    /**
     * 7B.2
     * Test case to ensure an exception is thrown when a blank file is passed to the game.
     */
    @Test
    @DisplayName("Exception thrown with file is empty")
    void fileIsEmptyException() throws IllegalArgumentException {
        gameBoard = new GameBoard(new ArrayList<>());
        assertThrowsExactly(IllegalArgumentException.class, ()-> gameBoard.startGame());
    }

    /**
     * 6.5
     * Test to verify the correct functioning of the hasWon method
     */
    @Test
    @DisplayName("Game board can detect win")
    void hasWon() {
        gameBoard = new GameBoard(new ArrayList<>());

        GameBoard.Status[] result = new GameBoard.Status[]{
                correct, correct, correct, correct, correct};
        assertTrue(gameBoard.hasWon(result), "game is won");
    }

    /**
     *1A.14
     * Test the function of the contains word method
     */
    @Test
    @DisplayName("Game board can check if word is valid")
    void containsValidWord() {
        ArrayList<String> list = new ArrayList<>();
        String word = "prone";
        list.add(word);
        gameBoard = new GameBoard(list);

        assertTrue(gameBoard.containsWord(word), "contains word");
    }
    /**
     *1A.15
     * Test the negative function of the contains word method
     */
    @Test
    @DisplayName("Game board can check if word is invalid")
    void doesNotContainInvalidWord() {
        ArrayList<String> list = new ArrayList<>();
        String word = "prone";

        gameBoard = new GameBoard(list);

        assertFalse(gameBoard.containsWord(word), "does not contain word");
    }
}
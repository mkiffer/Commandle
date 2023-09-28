package edu.monash.commandle;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static edu.monash.commandle.GameBoard.Status.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void isInTarget() {
    }

    /**
     * 1A.1
     * Test case to verify that all letters are in the wrong position.
     */
    @Test
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
    void allLettersAreCorrect() {
        gameBoard = new GameBoard("shard");
        char[] guess = "shard".toCharArray();
        GameBoard.Status[] result = gameBoard.isInTarget(guess);
        GameBoard.Status[] expected = new GameBoard.Status[]{
                correct,correct,correct,correct,correct};
        assertArrayEquals(expected, result);
    }

    @Test
    void fileIsEmptyException() throws IllegalArgumentException {
        gameBoard = new GameBoard(new ArrayList<>());
        assertThrowsExactly(IllegalArgumentException.class, ()->{
            gameBoard.startGame();
        });
    }

    @Test
    void hasWon() {
        gameBoard = new GameBoard(new ArrayList<>());

        GameBoard.Status[] result = new GameBoard.Status[]{
                correct, correct, correct, correct, correct};
        assertTrue(gameBoard.hasWon(result), "game is won");
    }

    @Test
    void containsCorrectWord() {
        ArrayList<String> list = new ArrayList<>();
        String word = "prone";
        list.add(word);
        gameBoard = new GameBoard(list);

        assertTrue(gameBoard.containsWord(word), "contains word");
    }
}
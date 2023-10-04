package edu.monash.commandle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * Unit tests for the {@link WordListFileReader} class.
 */
public class WordListFileReaderTest {

    /**
     * 7A.1
     * Test case to verify that a {@code NullPointerException} is thrown
     * when attempting to read a non-existent file.
     *
     * @throws NullPointerException When the specified file is not found.
     */
    @Test
    @DisplayName("File reader throws exception if non-existent file is passed")
    void fileNotFoundException() throws NullPointerException {
        // Arrange: Prepare the test by specifying a non-existent file name.
        String fileName = "noFile.txt";
        WordListFileReader wlfr = new WordListFileReader(fileName);

        // Act and Assert: Verify that the expected exception is thrown when calling getWordList.
        assertThrowsExactly(NullPointerException.class, () -> wlfr.getWordList(fileName));
    }

    /**
     * 7B.1
     * Test case to verify that an {@code IllegalArgumentException} is thrown
     * when attempting to read a blank file.
     *
     * @throws IllegalArgumentException When the input file is empty.
     */
    @Test
    @DisplayName("File reader throws exception if blank file is passed")
    void blankFileException() throws IllegalArgumentException {
        // Arrange: Prepare the test by specifying a file with no content.
        String fileName = "blank.txt";
        WordListFileReader wlfr = new WordListFileReader(fileName);

        // Act and Assert: Verify that the expected exception is thrown when calling getWordList.
        assertThrows(IllegalArgumentException.class, () -> wlfr.getWordList(fileName));
    }
}




















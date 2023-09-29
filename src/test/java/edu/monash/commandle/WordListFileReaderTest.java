package edu.monash.commandle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
@ExtendWith(MockitoExtension.class)
public class WordListFileReaderTest {

    @Test
    @DisplayName("File reader throws exception if non-existent file is passed")
    void fileNotFoundException() throws NullPointerException {
        String fileName = "noFile.txt";
        WordListFileReader wlfr = new WordListFileReader(fileName);

        assertThrowsExactly(NullPointerException.class,()-> {
            wlfr.getWordList(fileName);
                });
    }

    @Test
    @DisplayName("File reader throws exception if blank file is passed")
    void blankFileException() throws IllegalArgumentException {
        String fileName = "blank.txt";
        WordListFileReader wlfr = new WordListFileReader(fileName);

        assertThrowsExactly(IllegalArgumentException.class,()-> {
            wlfr.getWordList(fileName);
        });
    }




}











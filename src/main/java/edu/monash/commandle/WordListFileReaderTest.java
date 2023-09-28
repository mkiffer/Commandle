package edu.monash.commandle;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;
public class WordListFileReaderTest {

    @Test
    void fileNotFoundException() throws NullPointerException {
        String fileName = "noFile.txt";
        WordListFileReader wlfr = new WordListFileReader(fileName);

        assertThrowsExactly(NullPointerException.class,()-> {
            wlfr.getWordList(fileName);
                });
    }

    @Test
    void blankFileException() throws IllegalArgumentException {
        String fileName = "blank.txt";
        WordListFileReader wlfr = new WordListFileReader(fileName);

        assertThrowsExactly(IllegalArgumentException.class,()-> {
            wlfr.getWordList(fileName);
        });
    }




}











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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class WordListFileReaderTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream errOut;


    private List<String> wordList;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    @Test
    void fileNotFoundException() throws NullPointerException {
        String fileName = "noFile.txt";
        WordListFileReader wlfr = new WordListFileReader(fileName);

        assertThrowsExactly(NullPointerException.class,()-> {
            wlfr.getWordList(fileName);
                });
    }


}

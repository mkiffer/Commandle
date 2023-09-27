package edu.monash.commandle;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.time.*;

public class Commandle{
    public static final int MAX_TRIES = 6;
    private static int wins;
    private static int losses;
    private static double winPercentage;

    private static int gameCount;
    private static LocalDateTime currentTime;
    private static LocalDateTime endOfDay;

    /**
     * Requirements:
     * 1. Different words in consecutive games in the same session
     * 2. Default number of tries is 6, but can be changed if specified by the user
     * 3. Only valid words in the list are accepted
     * 4. Words are case-insensitive
     * 5. The same word cannot be played again in a game
     * <p>
     * Hints:
     * * A "?" flags that the input letter is in the word but not in the right position.
     * * A "#" flags the input letter is not in the word.
     * * The actual letter shows that it's in the right position.
     *
     * @param args Optional argument that points to a dictionary file of allowed words.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        wins = 0;
        losses = 0;
        winPercentage = 0;
        gameCount = 0;
        start(System.in, System.out, args);
    }

    public static int getGameCount(){
        return gameCount;
    }

    static void start(InputStream in, PrintStream out, String[] args) {
        String dictionaryFileName = "dictionary.txt";
        if (null != args && args.length > 0) {
            dictionaryFileName = args[0];
        }
        try{
            WordListFileReader gameFileReader = new WordListFileReader(dictionaryFileName);
            List<String> wordList = gameFileReader.getWordList(dictionaryFileName);
            start(in, out, wordList);
        }
        catch (IllegalArgumentException e){
            System.err.print(e.getMessage());
        }
        catch (NullPointerException e){
            System.err.print(e.getMessage());
        }

    }

    static void start(InputStream in, PrintStream out, List<String> wordList){
        GameBoard gameBoard = new GameBoard(wordList);

        Scanner scanner = new Scanner(in);
        LocalDate currentDate = LocalDate.now();
        endOfDay = LocalTime.MAX.atDate(currentDate);
        currentTime = LocalDateTime.now();
        try{
            gameLoop(currentDate, currentTime,endOfDay,scanner, gameBoard, out, MAX_TRIES);
        }
        catch (NoSuchElementException e){
            scanner.close();
        }
        out.println("See you next time!");
        scanner.close();
    }
    //Start function with a specific target
    static void startWithTarget(InputStream in, PrintStream out, List<String> wordList, String target, int maxTries){
        GameBoard gameBoard = new GameBoard(wordList);
        Scanner scanner = new Scanner(in);
        LocalDate currentDate = LocalDate.now();
        endOfDay = LocalTime.MAX.atDate(currentDate);
        currentTime = LocalDateTime.now();
        try{
            gameLoopWithTarget(currentDate, currentTime,endOfDay,scanner, gameBoard, out,target, maxTries);
        }
        catch (NoSuchElementException e){
            System.err.println("Need more input");
            scanner.close();
        }
        out.println("See you next time!");

        scanner.close();
    }


    static void gameLoopWithTarget(LocalDate currentDate, LocalDateTime currentTime, LocalDateTime endOfDay,Scanner scanner,
                         GameBoard gameBoard, PrintStream out, String target, int maxTries){

        boolean keepPlaying = true;

        do {
            LocalDateTime startTime = LocalDateTime.now();


            if (currentTime.isAfter(endOfDay)){
                restartDay(currentDate);
                gameBoard.startGame();
                gameBoard.setTarget(target);
                gameDisplayLogic(out, maxTries, gameBoard, scanner);

            }
            if((currentTime.isBefore(endOfDay))&&(gameCount<10)){
                gameBoard.startGame();
                gameBoard.setTarget(target);
                gameDisplayLogic(out, maxTries, gameBoard, scanner);

            }
            else if(gameCount >= 10){
                out.println("you have reached the maximum number of games played today");
                break;
            }

            timeDelay();

            Duration elapsedTime = Duration.between(startTime, LocalDateTime.now());
            currentTime = currentTime.plus(elapsedTime);
            String playAgain = scanner.nextLine().trim();
            if(!playAnotherGame(playAgain,scanner)){
                keepPlaying = false;
            }
        } while (keepPlaying);
    }

    static void gameLoop(LocalDate currentDate, LocalDateTime currentTime, LocalDateTime endOfDay,Scanner scanner,
                         GameBoard gameBoard, PrintStream out, int maxTries){
        boolean keepPlaying = true;
        do {
            LocalDateTime startTime = LocalDateTime.now();


            if (currentTime.isAfter(endOfDay)){
                restartDay(currentDate);
                gameBoard.startGame();
                gameDisplayLogic(out, maxTries, gameBoard, scanner);

            }
            if((currentTime.isBefore(endOfDay))&&(gameCount<10)){
                gameBoard.startGame();
                gameDisplayLogic(out, maxTries, gameBoard, scanner);

            }
            else if(gameCount >= 10){
                out.println("you have reached the maximum number of games played today");
                break;
            }
            timeDelay(); //pause the game for 5 milliseconds

            Duration elapsedTime = Duration.between(startTime, LocalDateTime.now());
            currentTime = currentTime.plus(elapsedTime);
            String playAgain = scanner.nextLine().trim();
            if(!playAnotherGame(playAgain,scanner)){
                keepPlaying = false;
            }
        } while (keepPlaying);
    }

    private static void restartDay(LocalDate currentDate){
        resetGamesPlayed();
        resetWinsAndLosses();
        endOfDay = LocalTime.MAX.atDate(currentDate);
    }

    private static void gameDisplayLogic(PrintStream out, int maxTries, GameBoard gameBoard, Scanner scanner){
        boolean result = playOneGame(out, maxTries, gameBoard, scanner);
        if (result) {
            out.println("Congratulations, you won!");
            wins += 1;
            calculateWinPercentage(wins, losses);
            out.println("Your daily win percentage is: " + (int)winPercentage + "%.");
        } else {
            out.println("Sorry, you lost!");
            losses += 1;
            out.println("correct word was " + gameBoard.getTarget());
            calculateWinPercentage(wins, losses);
            out.println("Your daily win percentage is: " + (int)winPercentage + "%.");
        }
        gameCount += 1;
        out.println("Play again? (Y/N)");
    }

    private static String getNextValidGuess(Scanner scanner, Set<String> guesses, GameBoard gameBoard) {
        String guess = scanner.nextLine().trim().toLowerCase();

        if (guess.length() != 5) {
            System.err.print("Please enter a word of 5 letters: ");
        } else if (guesses.contains(guess)) {
            System.err.print("Please enter a new word: ");
        } else if (!gameBoard.containsWord(guess)) {
            System.err.print("Please enter a valid word: ");
        } else {
            guesses.add(guess);
            return guess;
        }
        return getNextValidGuess(scanner, guesses, gameBoard);
    }

    private static void calculateWinPercentage(int wins, int losses){
        int gamesPlayed = wins + losses;
        winPercentage = ((double) wins/(gamesPlayed))*100;
    }

    private static boolean playOneGame(PrintStream out, int rounds, GameBoard gameBoard, Scanner scanner) {
        Set<String> guesses = new HashSet<>();

        for (int i = 0; i < rounds; i++) {
            out.print("Please enter your guess: ");
            String guess = getNextValidGuess(scanner, guesses, gameBoard);

            // Check for correctness here
            GameBoard.Status[] result = gameBoard.isInTarget(guess.toLowerCase().toCharArray());
            String hint = "";
            for (int j = 0; j < result.length; j++) {
                switch (result[j]) {
                    case correct -> hint += guess.charAt(j);
                    case wrong -> hint += "#";
                    case partial -> hint += "?";
                }
            }
            int round = i + 1;
            out.println(round + ": " + guess + "  " + round + ": " + hint);
            if (gameBoard.hasWon(result)) {
                //remove target from wordlist
                gameBoard.getWordList().remove(gameBoard.getTarget());
                return true;
            }
        }
        gameBoard.getWordList().remove(gameBoard.getTarget());
        return false;
    }

    public static void resetWinsAndLosses(){
        wins = 0;
        losses = 0;
    }

    public static void resetGamesPlayed(){
        gameCount = 0;
    }

    private static void timeDelay(){
        try {
            Thread.sleep(5); // Sleep for 5 milliseconds
        } catch (InterruptedException e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
        }

    }
    private static boolean playAnotherGame(String playAgain, Scanner scanner){
        boolean incorrectInput = true;
        boolean output = true;
        while(incorrectInput){
            if("N".equalsIgnoreCase(playAgain)){
                output = false;
                break;
            }
            if("Y".equalsIgnoreCase(playAgain)){
                output = true;
                break;
            }
            System.err.println("Please enter Y or N");
            playAgain = scanner.nextLine().trim();
        }
        return output;
    }


}


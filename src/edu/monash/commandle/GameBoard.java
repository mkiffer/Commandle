package edu.monash.commandle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
    public enum Status {
        correct, wrong, partial;
    }

    private String target;
    private final List<String> wordList;
    public GameBoard(String target) {
        wordList = new ArrayList<>();
        this.target = target;
    }


    public GameBoard(List<String> wordList) {
        this.wordList = wordList;
    }

    public void startGame() throws IllegalArgumentException{
        int size = wordList.size();
        if(size < 1){
            throw new IllegalArgumentException("This file is empty");
        }
        target = wordList.get(new Random().nextInt(size)).toLowerCase();
//        System.err.println("\ntarget w = " + target);
    }

   /** public Status[] isInTarget(char[] word) {
        Status[] result = new Status[target.length()];

        for (int i = 0; i < target.length(); i++) {
            if (word[i] == target.charAt(i)) {
                result[i] = Status.correct;
            } else if (target.indexOf(word[i]) >= 0) {
                result[i] = Status.partial;
            } else {
                result[i] = Status.wrong;
            }
        }
        return result;
    }
*/
   public Status[] isInTarget(char[] word) {
       Status[] result = new Status[target.length()];
       boolean[] usedIndices = new boolean[target.length()];

       // First pass: Mark correct letters and their positions
       for (int i = 0; i < target.length(); i++) {
           if (word[i] == target.charAt(i)) {
               result[i] = Status.correct;
               usedIndices[i] = true; // Mark this index as used
           }
       }

       // Second pass: Handle letters that are in the word but not in correct positions
       for (int i = 0; i < target.length(); i++) {
           if (result[i] != Status.correct) { // Skip correct letters
               int index = target.indexOf(word[i]);
               while (index >= 0 && usedIndices[index]) {
                   index = target.indexOf(word[i], index + 1); // Find the next occurrence
               }
               if (index >= 0) {
                   result[i] = Status.partial;
                   usedIndices[index] = true; // Mark the corresponding index as used
               } else {
                   result[i] = Status.wrong;
               }
           }
       }

       return result;
   }

    public boolean hasWon(Status[] current) {
        boolean result = true;
        for (Status st : current) {
            result &= (st == Status.correct);
        }
        return result;
    }


    public boolean containsWord(String word) {
        return wordList.contains(word);
    }

    public void setTarget(String target){
        this.target = target;
    }

    public String getTarget(){
        return target;
    }

    public List<String> getWordList(){
        return wordList;
    }
}

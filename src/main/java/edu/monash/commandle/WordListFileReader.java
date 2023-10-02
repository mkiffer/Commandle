package edu.monash.commandle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class WordListFileReader {
    private String dictionaryFileName;

    public WordListFileReader(String dictionaryFileName) {
        this.dictionaryFileName = dictionaryFileName;
    }
    public List<String> getWordList(String dictionaryFileName) throws NullPointerException, IllegalArgumentException, IOException {
        final List<String> wordList = new ArrayList<>();
        try{
            URL path = getPath(dictionaryFileName);
            File file = new File(path.getFile());
            try (FileReader fr = new FileReader(file)) {
                BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
                if(!br.ready()){
                    throw new IllegalArgumentException("Input file is empty");
                }
                String line;
                while ((line = br.readLine()) != null) {
                    wordList.add(line.trim().toLowerCase());
                }
            }
            catch(IOException e){
                throw new IOException("There was an error reading the file");
            }
        }
        catch (NullPointerException fileCannotBeFound){
            throw fileCannotBeFound;
        }
        return wordList;
    }

    private URL getPath(String dictionaryFileName) throws NullPointerException{

        URL path = Commandle.class.getClassLoader().getResource(dictionaryFileName);
        if(path == null) {
            throw new NullPointerException("Input file cannot be found");
        }
        else{
            return path;
        }

    }
}

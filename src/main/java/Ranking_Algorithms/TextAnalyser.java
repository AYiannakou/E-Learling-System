package Ranking_Algorithms;

import Database.Database;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author up707470
 */
public class TextAnalyser {

    public String input;

    public TextAnalyser(String input) {
        this.input = input;
    }

    public String[] Tokeniser() {  // tokenise the input
        return input.split("\\W");
    }

    public ArrayList<String> StopWords(ArrayList<String> temp) { // remove stop words from the user input
        ArrayList<String> stopWords = new ArrayList<>(Arrays.asList("java", "what", "how", "we", "that", "to", "for", "with", "when", "i", "a",
                "and", "are", "be", "if", "in", "is", "it", "of", "on", "or", "the", "was", "an", "by", "as", "within",
                "my", "will", "have", "so", "www", "com", "http", "https", "they", "there", "this", "which", "why"));
        for (int i = 0; i < temp.size(); i++) {
            for (int j = 0; j < stopWords.size(); j++) {

                if (temp.contains(stopWords.get(j))) { // checks if the word is the same as the stopWords list
                    temp.remove(stopWords.get(j));//remove it
                }
            }
        }
        return (temp);
    }

    public ArrayList<String> Lowercase(String[] storage) { // gets the user input and converted to lower case
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(storage));
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).toLowerCase());
        }
        list.removeAll(Arrays.asList(null, ""));
        return (list);
    }

    public ArrayList<String> TextAnalyser() {  // returns the result of the above methods into an ArrayList

        ArrayList<String> FinalList = new ArrayList<>();
        FinalList = StopWords(Lowercase(Tokeniser()));

        return (FinalList);
    }
}

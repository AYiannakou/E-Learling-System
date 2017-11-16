/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ranking_Algorithms;

import Database.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author A.Y
 */
public class Ranking implements Runnable {

    public List<String> words = new ArrayList<>(); // All single words from database
    public List<List<String>> docs = new ArrayList<>(); //All docs from database

    public ArrayList<Integer> sortedtIndex = new ArrayList<>();
    public ArrayList<String> queryWords = new ArrayList<>(); //Query single words
    public ArrayList<Double> queryFreq = new ArrayList<>(); //Query freaquencies

    public String query;

    public Ranking() {

    }

    public ArrayList<Integer> getSortedtIndex() {

        return this.sortedtIndex;
    }

    public void setQuery(String userQuery) {

        this.query = userQuery;

    }

    public double tf(List<String> doc, String term) { // code for TF
        double counter = 0; //counter for counting the same words
        for (String word : doc) {
            if (term.equalsIgnoreCase(word)) { //checks if the given term equals in the document
                counter++; //increment counter

            }
        }

        return counter;
    }

    public double idf(List<List<String>> docs, String term) { //code for IDF
        double n = 0; //to count if the term is inside each document
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) { //checks if the given term equals in each document
                    n++; //increment n 
                    break; //break is used to stop when the term appears in the document and go to the next document
                }
            }
        }
       
        return Math.log10(docs.size() / n);

    }

    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);
    }

    public void getDocs() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {

        Database dm = new Database(); //calling the Class to retrieve information from database

        dm.getAllDocuments();
         
        ArrayList<String> p = dm.getAllDescriptions();

        for (String p1 : p) {

            TextAnalyser ta = new TextAnalyser(p1); // calling Class to remove stopwords for each individual title

            List<String> result = ta.TextAnalyser(); // result without stopwords

            docs.add(result); //adding all documents to ArrayList for later use

        }

    }

    public HashMap<Integer, Double> cosSin() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        HashMap<Integer, Double> results = new HashMap<>(); //for adding index and frequency

        /*To split each doc into words and add them to ArrayList have all individual words*/
        for (List<String> singleDoc : docs) {

            for (String doc1 : singleDoc) {

                String result = doc1.replaceAll("[^a-zA-Z0-9\\s+]", "").replaceAll("\\d", ""); // removes symbols and numbers from string

                String[] split = result.split(" "); //splits the the doc into a single word

                for (String singleWord : split) {

                    if (!words.contains(singleWord)) {

                        //if word does not exist in the List, add it.
                        words.add(singleWord);

                    }

                }

            }

        }

        int docNumber = 1; //document number

        for (List<String> singleDoc : docs) {

            /*Variables for calculating CosSin similarity*/
            double total = 0.0;
            double root;
            double sum1 = 0.0;
            double sum;
            double cosSin;
            double q;
            double finale = 0.0;
            double qTotal;
            double tf;
            double idf;

            for (String singleWord : words) { //for each word in words

                if (singleDoc.contains(singleWord)) { //to check if the current word exist in the doc

                    tf = tf(singleDoc, singleWord); //if true, count how frequent the word is in the doc

                    idf = idf(docs, singleWord); //to calculate the given word from all documents

                    sum = tf * idf;

                    double sqrt = Math.pow(sum, 2); //to square the result to use it in CosSin equation

                    total += sqrt; // adding all squared results for later use

                    /*To ckeck if a word in the query equals with the current word in the doc*/
                    for (int i = 0; i < queryWords.size(); i++) {

                        String string = queryWords.get(i);

                        if (string.equals(singleWord)) {

                            double value = queryFreq.get(i); //if true, get the frequency of the given word

                            double y = value * sum; // multiplying (value) with the freq of the current word from query

                            finale += y * sum; //to have the whole result of the calculated words

                            q = Math.pow(y, 2);

                            sum1 += q; // for overall score in the doc 

                            //System.out.println("Doc: " + docNumber + " Term: " + singleWord + " Freq: " + y);
                        }

                    }

                }

            }
            qTotal = Math.sqrt(sum1); //using sqrt to find square root of sum1
            root = Math.sqrt(total); //(checked)

            cosSin = finale / root * qTotal; //cosSin(document(number),query)

            //To check if cosSin is not 0  and bigger than 0.02
            if (!Double.isNaN(cosSin) && cosSin > 0.02) {

                results.put(docNumber, cosSin); //add the number of the doc and result to HashMap

            }
            docNumber += 1; //increments doc number

        }

        return results;

    }

    public void UserQuery() {

        List<String> doc = new ArrayList<>();

        String[] split = query.toLowerCase().split(" "); //splits the query into words

        /*To hold each word and add it to (queryWords) only if not exist*/
        for (String a : split) {

            doc.add(a);

            if (!queryWords.contains(a)) { //if word doesn't exist in the ArrayList

                queryWords.add(a); //add it
            }
        }
        /*Calculates how frequent a word is in the query */
        for (String i : queryWords) {

            double tf = tf(doc, i);
            double idf = idf(docs, i);

            double sum = tf * idf / queryWords.size();

            queryFreq.add(sum);
        }

    }

    @Override
    public void run() {

        try {

            getDocs();

            UserQuery();

            Map b = cosSin();

            Map c = sort(b);

            Iterator iterator = c.entrySet().iterator();
           
            while (iterator.hasNext()) {

                Map.Entry entry = (Map.Entry) iterator.next();

                System.out.println("Index: " + entry.getKey() + " Freq: " + entry.getValue());
                Integer i = (Integer) entry.getKey();

                sortedtIndex.add(i);

            }
             
            System.out.println("Total documents returned: " + sortedtIndex.size());
            
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | IOException ex) {
            Logger.getLogger(Ranking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

    }

    /*Used for variation of videos in ascending order based on CosSin resutls  */
    public static <K, V extends Comparable<? super V>> Map<K, V> sort(Map<K, V> map) {
        List<Map.Entry<K, V>> list
                = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}

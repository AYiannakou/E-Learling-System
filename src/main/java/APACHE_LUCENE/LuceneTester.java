package APACHE_LUCENE;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

    String indexDirTitles = "/Users/A.Y/Desktop/index_titles";
    String dataDirTitles = "/Users/A.Y/Desktop/docs_titles";

    String indexDirDesc = "/Users/A.Y/Desktop/index_descs";
    String dataDirDesc = "/Users/A.Y/Desktop/docs_descs";

    Indexer indexer;
    Searcher searcher;

    String query;

    ArrayList<Float> titles_results;
    ArrayList<Float> descs_results;

    ArrayList<Integer> titles_index;
    ArrayList<Integer> descs_index;

    public LuceneTester() {

        this.titles_results = new ArrayList<>();
        this.descs_results = new ArrayList<>();

        this.titles_index = new ArrayList<>();
        this.descs_index = new ArrayList<>();

    }

    public static void main(String[] args) throws IOException, ParseException {

        LuceneTester tester;

        tester = new LuceneTester();

        tester.setQuery("how to iterate through hashmap in java");

        tester.searchDescs();
        tester.searchTitles();

        tester.finalResutls();

    }

    public void setQuery(String q) {

        query = q;
    }

    public void createIndex() throws IOException {

        indexer = new Indexer(indexDirDesc);

        indexer.createIndex(dataDirDesc, new TextFileFilter());

        indexer.close();

    }
    /*Using the directory of the indexed video descriptions to find videos based on the given query*/
    public void searchDescs() throws IOException, ParseException {

        searcher = new Searcher(indexDirDesc);

        TopDocs hits = searcher.search(query);

        System.out.println("---------");
        System.out.println("Descriprion Results:");
        System.out.println("---------");

        for (ScoreDoc scoreDoc : hits.scoreDocs) {

            Document doc = searcher.getDocument(scoreDoc);

            String docInfo = doc.get(Constants.FILE_PATH);

            String split = StringUtils.substringAfter(docInfo, "file").replace(".txt", "");

            int index = Integer.valueOf(split);
            descs_results.add(scoreDoc.score);
            descs_index.add(index);
            System.out.println("File index: " + index + " Score: " + scoreDoc.score);
        }
        searcher.close();

    }
    /*Using the directory of the indexed video titles to find videos based on the given query*/
    public void searchTitles() throws IOException, ParseException {

        searcher = new Searcher(indexDirTitles);

        TopDocs hits = searcher.search(query);

        System.out.println("---------");
        System.out.println("Titles Results:");
        System.out.println("---------");

        for (ScoreDoc scoreDoc : hits.scoreDocs) {

            Document doc = searcher.getDocument(scoreDoc);

            String docInfo = doc.get(Constants.FILE_PATH);

            String split = StringUtils.substringAfter(docInfo, "file").replace(".txt", "");

            int index = Integer.valueOf(split);
            titles_results.add(scoreDoc.score);
            titles_index.add(index);

            System.out.println("File index: " + index + " Score: " + scoreDoc.score);
        }
        searcher.close();

    }

    public ArrayList<Integer> finalResutls() throws IOException, ParseException {

        ArrayList<Integer> result = new ArrayList<>();

        System.out.println("-----------------");
        System.out.println("Most rated:");
        System.out.println("-----------------");
        System.out.println("Total documents returned: " + titles_index.size());

        for (int i = 0; i < titles_index.size(); i++) {

            if (descs_index.contains(titles_index.get(i))) {

                for (int a = 0; a < descs_index.size(); a++) {
                    if (titles_index.get(i).equals(descs_index.get(a))) {
                        float r = descs_results.get(a) + titles_results.get(i);
                        System.out.println("Index: " + titles_index.get(i) + " Result: " + r);

                        result.add(titles_index.get(i));

                    }
                }
            }

        }
        return result;

    }

}

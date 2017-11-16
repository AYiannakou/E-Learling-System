package APACHE_LUCENE;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {

    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;

    public Searcher(String indexDirectoryPath) throws IOException {

        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));// to open the indexed files folder.
        indexSearcher = new IndexSearcher(indexDirectory); // using the class IndexSearcher to add the indexed files.
        queryParser = new QueryParser(Version.LUCENE_36, Constants.CONTENTS, new StandardAnalyzer(Version.LUCENE_36)); // creates an new parser query.
    }

    public TopDocs search(String searchQuery) throws IOException, ParseException {

        query = queryParser.parse(searchQuery); //parsing QuerParser with the given query to Query

        return indexSearcher.search(query, Constants.MAX_SEARCH); //set query and max results

    }

    public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {

        return indexSearcher.doc(scoreDoc.doc); // to receive each document score
    }

    public void close() throws IOException {

        indexSearcher.close(); //close the statement
    }
}

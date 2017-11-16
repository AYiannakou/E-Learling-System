package APACHE_LUCENE;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

    private final IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {

        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath)); //given the directory to write  indexed documents 

        //creates a new indexer
        writer = new IndexWriter(indexDirectory, new StandardAnalyzer(Version.LUCENE_36), true, IndexWriter.MaxFieldLength.UNLIMITED);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    private Document getDocument(File file) throws IOException {

        Document document = new Document();

        //to index all files in the folder containing video information.
        Field contentField = new Field(Constants.CONTENTS, new FileReader(file));

        //to index words inside the document.
        Field fileNameField = new Field(Constants.FILE_NAME, file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        //to index file path
        Field filePathField = new Field(Constants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED);

        
        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);

        return document;
    }

    public void createIndex(String dataDirPath, FileFilter filter) throws IOException {
       
        File[] files = new File(dataDirPath).listFiles(); // Receives all files based on direcotry 

        for (File file : files) {
            if (!file.isDirectory() && filter.accept(file)) { //checks if the file is valid 

                Document document = getDocument(file); // use the file direcotry
                writer.addDocument(document);  // writes the document
            }
        }

    }
}

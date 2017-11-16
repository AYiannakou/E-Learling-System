/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Ranking_Algorithms.Ranking;
import static Ranking_Algorithms.Ranking.sort;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author A.Y
 */
public class DatabaseThread implements Runnable {

    String query;

    public DatabaseThread() {

    }

    public void setQuery(String q) {

        query = q;
    }

    @Override
    public void run() {

        try {

            Ranking tf_idf = new Ranking();
            tf_idf.setQuery(query);
            tf_idf.getDocs();

            tf_idf.UserQuery();

            Map b = tf_idf.cosSin();

            Map c = sort(b);

            Iterator iterator = c.entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry entry = (Map.Entry) iterator.next();

                System.out.println("Intex: " + entry.getKey() + " Freq: " + entry.getValue());
                Integer i = (Integer) entry.getKey();

                tf_idf.sortedtIndex.add(i);

            }

        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Ranking.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

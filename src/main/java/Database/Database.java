package Database;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author A.Y
 */
public class Database {

    private static final String Driver = "com.mysql.jdbc.Driver";// Driver MySQL (Connector/J driver)
    private static Connection conn = null;
    private static Statement stmt = null;

    /*Attributes for adding users questions*/
    public ArrayList<String> questions;
    public ArrayList<String> user;
    public ArrayList<String> date;
    public ArrayList<Integer> question_id;

    /*Attributes for adding users answers*/
    public ArrayList<String> answer;
    public ArrayList<String> user_answer;
    public ArrayList<String> date_answer;

    /*Attributes for adding (BASIC CONCEPTS OF JAVA) video information */
    public ArrayList<String> allIds;
    public ArrayList<Integer> allViews;
    public ArrayList<Integer> allLikes;
    public ArrayList<Integer> allDislikes;
    public ArrayList<String> allDurations;
    public ArrayList<String> allYears;
    public ArrayList<String> allPng;
    public ArrayList<String> allDescriptions;
    public ArrayList<String> allTitles;

    /*Attributes for receiving quize information*/
    public ArrayList<String> quizeQuestions;
    public ArrayList<String> quizeAnswers;

    /*Attributes for showing notification messages to user*/
    public ArrayList<String> sender;
    public ArrayList<Integer> messagePosition;
    public ArrayList<Integer> messageId;
    public ArrayList<Boolean> messageCondition;

    int newVideos = 0;

    public Database() {

        this.questions = new ArrayList<>();
        this.user = new ArrayList<>();
        this.date = new ArrayList<>();
        this.question_id = new ArrayList<>();

        this.answer = new ArrayList<>();
        this.user_answer = new ArrayList<>();
        this.date_answer = new ArrayList<>();

        this.allDislikes = new ArrayList<>();
        this.allDurations = new ArrayList<>();
        this.allIds = new ArrayList<>();
        this.allPng = new ArrayList<>();
        this.allViews = new ArrayList<>();
        this.allYears = new ArrayList<>();
        this.allLikes = new ArrayList<>();
        this.allDescriptions = new ArrayList<>();
        this.allTitles = new ArrayList<>();

        this.quizeQuestions = new ArrayList<>();
        this.quizeAnswers = new ArrayList<>();

        this.sender = new ArrayList<>();
        this.messagePosition = new ArrayList<>();
        this.messageId = new ArrayList<>();
        this.messageCondition = new ArrayList<>();

    }

    public ArrayList<String> getQuestions() {

        return this.questions;
    }

    public ArrayList<String> getDate() {

        return this.date;
    }

    public ArrayList<String> getUser() {

        return this.user;
    }

    public ArrayList<Integer> getQuestionId() {

        return this.question_id;

    }

    public ArrayList<String> getAnswer() {

        return this.answer;

    }

    public ArrayList<String> getAnswerDate() {

        return this.date_answer;
    }

    public ArrayList<String> getUserAnswer() {

        return this.user_answer;
    }

    public ArrayList<String> getAllDescriptions() {

        return this.allDescriptions;
    }

    public ArrayList<String> getAllTitles() {

        return this.allTitles;
    }

    public ArrayList<String> getAllIds() {

        return this.allIds;
    }

    public ArrayList<Integer> getAllLikes() {

        return this.allLikes;
    }

    public ArrayList<Integer> getAllDislikes() {

        return this.allDislikes;
    }

    public ArrayList<Integer> getAllViews() {

        return this.allViews;
    }

    public ArrayList<String> getAllYears() {

        return this.allYears;
    }

    public ArrayList<String> getAllDurations() {

        return this.allDurations;
    }

    public ArrayList<String> getAllPng() {

        return this.allPng;
    }

    public ArrayList<String> getQuizeQuestions() {

        return this.quizeQuestions;
    }

    public ArrayList<String> getQuizeAnswers() {

        return this.quizeAnswers;
    }

    public ArrayList<String> getMessageSender() {

        return this.sender;
    }

    public ArrayList<Integer> getMessagePosition() {

        return this.messagePosition;
    }

    public ArrayList<Integer> getMessageId() {

        return this.messageId;
    }

    public ArrayList<Boolean> getMessageCondition() {

        return this.messageCondition;
    }

    private void connect() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        Class.forName(Driver).newInstance();  //Creates a new instance of the class represented by this Class object.

        conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/E-Learning-Database", "root", "root"); //Attempts to establish a connection using the database URL,password,and username.
        stmt = conn.createStatement();

    }

    public String createAccount(String name, String surname, String username, String password) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        connect();

        String check = "SELECT * FROM USERS WHERE USERNAME = '" + username + "'";

        ResultSet rs = stmt.executeQuery(check);

        if (rs.next()) {

            return "Username already exist,please use a different username";

        } else {

            String query = "INSERT INTO USERS (NAME,SURNAME,USERNAME,PASSWORD)" + "values (?,?,?,?)"; //Query to insert infomation in the database

            PreparedStatement preparedStmt = conn.prepareStatement(query); //Creates a statement object for sending SQL statements to the database.

            //Inserting user info to database respectively//
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, surname);
            preparedStmt.setString(3, username);
            preparedStmt.setString(4, password);

            preparedStmt.execute(); // Executes the SQL statement

            return "Successfully created an account";

        }
    }

    public boolean logIn(String username, String password) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        connect();

        String query = "SELECT * FROM USERS WHERE USERNAME = '" + username + "' and PASSWORD = '" + password + "'"; //query for searching to USERS table

        ResultSet rs = stmt.executeQuery(query); // execute the query

        if (rs.next()) {

            return true;

        } else {

            return false;

        }

    }

    public int addVideoInfoToTable(ArrayList<String> titles, ArrayList<BigInteger> views, ArrayList<BigInteger> likes, ArrayList<BigInteger> dislikes, ArrayList<String> durations, ArrayList<String> years, ArrayList<String> pngs, ArrayList<String> ids, ArrayList<String> desc) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        connect();

        int counter = 0;

        for (int i = 0; i < ids.size(); i++) {

            /*Receiving given video infos respectively from Search Object*/
            String id = ids.get(i);
            String title = titles.get(i);
            String duration = durations.get(i);
            String year = years.get(i);
            String png = pngs.get(i);
            String desc1 = desc.get(i);
            int videoView = views.get(i).intValue();
            int videoLike = likes.get(i).intValue();
            int videoDislike = dislikes.get(i).intValue();

            String check = "SELECT * FROM VIDEOS WHERE ID = '" + id + "'"; // to interate through topic table and check if the id exist

            ResultSet resultSet = stmt.executeQuery(check); //executes database query 

            if (!resultSet.next()) { //adds the videos if the id does not exist 

                counter++; // inrementation of counter if video id is not exist

                String add = "INSERT INTO VIDEOS (TITLE,VIEWS,LIKES,DISLIKES,YEAR,DURATION,PNG,ID,DESCRIPTION) " + " values (?,?,?,?,?,?,?,?,?)";

                PreparedStatement preparedStmt = conn.prepareStatement(add);

                /*Add video information respectivelly to database table*/
                preparedStmt.setString(1, title);
                preparedStmt.setInt(2, videoView);
                preparedStmt.setInt(3, videoLike);
                preparedStmt.setInt(4, videoDislike);
                preparedStmt.setString(5, year);
                preparedStmt.setString(6, duration);
                preparedStmt.setString(7, png);
                preparedStmt.setString(8, id);
                preparedStmt.setString(9, desc1);

                preparedStmt.execute(); // Executes the SQL statement

            }
        }

        newVideos += newVideos + counter; // used to add counter values to summurise it.

        conn.close();
        stmt.close();

        return newVideos;

    }

    public void addQuestion(String username, String question, String category) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        connect();

        DateFormat current_date = new SimpleDateFormat("dd/MM/yyyy"); //gets the current date
        Date dateobj = new Date();

        String date_time = current_date.format(dateobj); //convert it into string

        String query = "INSERT INTO QUESTIONS (USERNAME,QUESTION,DATE,CATEGORY) VALUES (?,?,?,?)"; //query for adding question information

        PreparedStatement preparedStmt = conn.prepareStatement(query);

        preparedStmt.setString(1, username); // position of username in the table
        preparedStmt.setString(2, question); // position of question in the table
        preparedStmt.setString(3, date_time); // position of dateAdded in the table
        preparedStmt.setString(4, category); // position of dateAdded in the table

        preparedStmt.execute(); //excecutes the statement

        conn.close();
        stmt.close();

    }

    public void loadQuestions(String category) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        connect();

        String query = "SELECT * FROM QUESTIONS WHERE CATEGORY = '" + category + "'"; // query for receiving QUESTIONS table data

        ResultSet resultSet = stmt.executeQuery(query); // excecutes the query

        while (resultSet.next()) { //while true

            //get the following: id,dateAdded,username and user question
            int id = resultSet.getInt("ID");
            String date1 = resultSet.getString("DATE");
            String user1 = resultSet.getString("USERNAME");
            String question = resultSet.getString("QUESTION");

            //adds the data into multiple ArrayLists
            questions.add(question);
            user.add(user1);
            date.add(date1);
            question_id.add(id);
        }

        conn.close();
        stmt.close();
    }

    public void Discussions(int id) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        connect();

        String query = "SELECT * FROM ANSWERS WHERE ANSWERS.ID = " + id; // query for receiving answers from table based on the index that the user has selected

        ResultSet resultSet = stmt.executeQuery(query);

        while (resultSet.next()) { //while true

            /*Data from ANSWERS table */
            String table_date = resultSet.getString("DATE");
            String table_answer = resultSet.getString("ANSWER");
            String table_user = resultSet.getString("USERNAME");

            answer.add(table_answer);
            user_answer.add(table_user);
            date_answer.add(table_date);

        }
        conn.close();
        stmt.close();

    }

    public void addAnswer(int id, String username, String answer) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        connect();

        DateFormat current_date = new SimpleDateFormat("dd/MM/yyyy"); //gets the current date
        Date dateobj = new Date();

        String date_time = current_date.format(dateobj); //convert it into string

        String query = "INSERT INTO ANSWERS (ID,DATE,USERNAME,ANSWER) VALUES (?,?,?,?)"; //query for adding an answer from user

        PreparedStatement preparedStmt = conn.prepareStatement(query);

        preparedStmt.setInt(1, id); //position of id in the table
        preparedStmt.setString(2, date_time); //position of date in the table
        preparedStmt.setString(3, username); //position of usernmae in the table
        preparedStmt.setString(4, answer); //position of answer in the table

        preparedStmt.execute();

        conn.close();
        stmt.close();
    }

    public void getAllDocuments() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {

        connect();

        String query = "SELECT * FROM Videos"; //to retrieve video information from Vidoes table

        ResultSet resultSet = stmt.executeQuery(query); //execute query

        while (resultSet.next()) {

            /*Selecting all variables from the table*/
            String description = resultSet.getString("description");
            String title = resultSet.getString("title");
            int like = resultSet.getInt("likes");
            int dislike = resultSet.getInt("dislikes");
            int view = resultSet.getInt("views");
            String duration = resultSet.getString("duration");
            String year = resultSet.getString("year");
            String png = resultSet.getString("png");
            String id = resultSet.getString("id");

            
            //Insert information to attributes
            allDescriptions.add(description);
            allTitles.add(title);
            allLikes.add(like);
            allDislikes.add(dislike);
            allViews.add(view);
            allDurations.add(duration);
            allYears.add(year);
            allPng.add(png);
            allIds.add(id);

        }
        conn.close();
        stmt.close();

    }

    public void Quize(String quize) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        connect();

        String topic = quize.replace(" - ", "_");

        String st = "SELECT * FROM " + topic; //sets the topic and use the database query to retieve videos 

        ResultSet resultSet = stmt.executeQuery(st); // Executes the query

        while (resultSet.next()) {

            /*Receiving video information from database*/
            String singleQuestion = resultSet.getString("question");
            String singleAnswer = resultSet.getString("answer");

            quizeQuestions.add(singleQuestion);
            quizeAnswers.add(singleAnswer);

        }

        conn.close();
        stmt.close();
    }

    public void addNotificationUser(String sender, String user, int position, boolean seen) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        connect();

        String query = "INSERT INTO USERS_MESSAGES (SENDER,USER,POSITION,SEEN) VALUES (?,?,?,?)"; //query for inserting values to USERS_MESSAGES

        try (PreparedStatement preparedStmt = conn.prepareStatement(query)) {

            //Adds information to the table
            preparedStmt.setString(1, sender);
            preparedStmt.setString(2, user);
            preparedStmt.setInt(3, position);
            preparedStmt.setBoolean(4, seen);

            preparedStmt.execute();
        }

        conn.close();
        stmt.close();

    }

    public void getUserNotification(String user) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        connect();

        String query = "SELECT * FROM USERS_MESSAGES WHERE USER = " + "'" + user + "'"; //receives information from USERS_MESSAGES given the username
 
        ResultSet resultSet = stmt.executeQuery(query);

        while (resultSet.next()) {

            //selecting all varibles from table 
            int id = resultSet.getInt("ID");
            String sender1 = resultSet.getString("SENDER");
            int position1 = resultSet.getInt("POSITION");
            boolean condition = resultSet.getBoolean("SEEN");

            if (!sender1.equals(user)) {

                sender.add(sender1);
                messagePosition.add(position1);
                messageCondition.add(condition);
                messageId.add(id);
            }

        }

        conn.close();
        stmt.close();

    }

    public void updatedNotificationStatus(int id) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        connect();

        String query = "UPDATE USERS_MESSAGES SET SEEN = TRUE WHERE ID = " + id; // query for updating SEEN boolean to true given the id of the message

        PreparedStatement preparedStmt = conn.prepareStatement(query); 

        preparedStmt.execute(); //execute the statement

        conn.close();
        stmt.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException {

        Database a = new Database();

        a.getAllDocuments();

        File file;
        FileWriter writer;
        ArrayList<String> docs = a.getAllDescriptions();
        int docNumber = 1;

        System.out.println(docs.size());
        for (String doc : docs) {

            file = new File("/Users/A.Y/Desktop/docs_descs/file" + docNumber + ".txt");
            file.createNewFile();

            writer = new FileWriter(file);
            writer.write(doc);
            writer.flush();
            writer.close();

            docNumber = docNumber + 1;
        }
    }
}

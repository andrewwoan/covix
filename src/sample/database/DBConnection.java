package sample.database;

import sample.model.User;

import java.sql.*;
import java.util.ArrayList;

public class DBConnection
{

    private static Connection connection;
    private static final String user = "root";
    private static final String password = "qwertt123";
    private static final String database = "jdbc:mysql://localhost:3306/user";

    public static Connection getConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (connection == null)
        {
            try
            {
                connection = DriverManager.getConnection(database, user, password);
            } catch (SQLException e)
            {
                e.printStackTrace();
                System.out.println("Could not open database.");
                System.exit(1);

            }
        }
        return connection;
    }
    public void createUser(String fullName, String email, String password){

        String insertUser = "INSERT INTO " + Const.USER_TABLE +
                "(" + Const.USER_FULLNAME
                + "," + Const.USER_EMAIL + "," + Const.USER_PASSWORD
                + ")" + "VALUES(?,?,?)";
        try {

            PreparedStatement preparedStatement = getConnection().prepareStatement(insertUser);
            preparedStatement.setString(1,fullName);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createUserConnections(String userID){

        String createUserConnections = "CREATE TABLE `user`."+"`"+"connections"+userID+"`"+"("+
                            "`fullName` VARCHAR(45) NULL,"+
                            "`email` VARCHAR(45) NOT NULL,"+
                            "`covixScore` INT NULL,"+
                            "PRIMARY KEY (`email`));";

        try {

            PreparedStatement preparedStatement = getConnection().prepareStatement(createUserConnections);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Function returns user row which contains all information about user
    public ResultSet checkForUser(User user){
        ResultSet resultSet = null;

        if (!user.getEmail().equals("") || !user.getPassword().equals("")){
            String searchForUser = "SELECT * FROM " + Const.USER_TABLE
                                                    + " WHERE "
                                                    + Const.USER_EMAIL
                                                    + "=?" + " AND "
                                                    + Const.USER_PASSWORD
                                                    + "=?";
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(searchForUser);

                preparedStatement.setString(1,user.getEmail());
                preparedStatement.setString(2,user.getPassword());

                resultSet = preparedStatement.executeQuery();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
                System.out.println("Error Logging in");
        }

        return resultSet;
    }

    public User getUser(String email){
        ResultSet resultSet = null;
        User user = new User();

        if (!email.equals("")){
            String searchForUser = "SELECT fullName,email,password FROM " + Const.USER_TABLE
                    + " WHERE "
                    + Const.USER_EMAIL
                    + "=?";
            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(searchForUser);

                preparedStatement.setString(1,email);

                resultSet = preparedStatement.executeQuery();

                while(resultSet.next()) {
                    user.setFullName(resultSet.getString("fullName"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return user;
    }
    public ResultSet getUserList(String email){
        ResultSet resultSet = null;
        String userID = getUserID(email);

        if (!email.equals("")){
            String searchForUser = "SELECT fullName,email,covixScore FROM " + "connections" +userID ;

            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(searchForUser);

                resultSet = preparedStatement.executeQuery();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return resultSet;
    }

    public ResultSet getGroupList(String whichTable){
        ResultSet resultSet = null;

            String searchForUser = "SELECT fullName,email,covixScore FROM " + whichTable;

            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(searchForUser);

                resultSet = preparedStatement.executeQuery();


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        return resultSet;
    }

    public ArrayList<String>  getAllTables(){
    ResultSet resultSet = null;
    ArrayList<String> tableNames = new ArrayList<String>();

        String getTableNames = "SELECT TABLE_NAME"+
        " FROM INFORMATION_SCHEMA.TABLES"+
        " WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='user'";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getTableNames);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                tableNames.add(resultSet.getString("TABLE_NAME"));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    return tableNames;
}

    public boolean checkConnection(String userEmail, String email){
        ResultSet resultSet = null;
        String userID = getUserID(userEmail);
        boolean nullOrNot = false;

        if (!email.equals("")){
            String searchForUser = "SELECT fullName,email,covixScore FROM " + "connections" +userID + " WHERE email=?" ;

            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(searchForUser);
                preparedStatement.setString(1,email);

                resultSet = preparedStatement.executeQuery();

                if (!resultSet.isBeforeFirst() ) {
                    nullOrNot=true;
                }

//                System.out.println(nullOrNot);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return nullOrNot;
    }

    public boolean checkGroup(String whichTable, String email){
        ResultSet resultSet = null;
        boolean nullOrNot = false;

        if (!email.equals("")){
            String searchForUser = "SELECT fullName,email,covixScore FROM " + whichTable + " WHERE email=?" ;

            try {
                PreparedStatement preparedStatement = getConnection().prepareStatement(searchForUser);
                preparedStatement.setString(1,email);

                resultSet = preparedStatement.executeQuery();

                if (!resultSet.isBeforeFirst() ) {
                    nullOrNot=true;
                }

//                System.out.println(nullOrNot);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return nullOrNot;
    }


    public void updateCovixScore(String email, int CovixScore){
        String insertCovixScore = "UPDATE " + Const.USER_TABLE +
                " SET " + Const.USER_COVIXSCORE
                + "=?" + " WHERE email=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertCovixScore);
            preparedStatement.setString(1,String.valueOf(CovixScore));
            preparedStatement.setString(2,email);

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updateGroupAdd(String whichTable, String email){
        String insertCovixScore = "UPDATE " + Const.USER_TABLE +
                " SET " + whichTable
                + "=?" + " WHERE email=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertCovixScore);
            preparedStatement.setInt(1,1);
            preparedStatement.setString(2,email);

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updateGroupRemove(String whichTable, String email){
        String insertCovixScore = "UPDATE " + Const.USER_TABLE +
                " SET " + whichTable
                + "=?" + " WHERE email=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertCovixScore);
            preparedStatement.setInt(1,0);
            preparedStatement.setString(2,email);

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updatePreviousStatus(String email, int CovixScore){
        String insertCovixScore = "UPDATE " + Const.USER_TABLE +
                " SET " + "previousScore"
                + "=?" + " WHERE email=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertCovixScore);
            preparedStatement.setString(1,String.valueOf(CovixScore));
            preparedStatement.setString(2,email);

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updateRiskScore(String email, int riskScore){
        String insertCovixScore = "UPDATE " + Const.USER_TABLE +
                " SET " + "riskScore"
                + "=?" + " WHERE email=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertCovixScore);
            preparedStatement.setString(1,String.valueOf(riskScore));
            preparedStatement.setString(2,email);

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updateUserScores(String table, String email, int CovixScore){
        String insertCovixScore = "UPDATE " + table +
                " SET " + Const.USER_COVIXSCORE
                + "=?" + " WHERE email=?";

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(insertCovixScore);
            preparedStatement.setString(1,String.valueOf(CovixScore));
            preparedStatement.setString(2,email);

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public int getPreviousStatus(String email){
        ResultSet covixScore;
        int returnScore = 0;

        String getCovixScore = "SELECT previousScore FROM " + Const.USER_TABLE
                + " WHERE "
                + Const.USER_EMAIL
                + "=?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getCovixScore);
            preparedStatement.setString(1,email);

            covixScore = preparedStatement.executeQuery();

            while(covixScore.next()){
                returnScore = covixScore.getInt(1);
//                System.out.println("return score: " + returnScore);
                return returnScore;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnScore;
    }

    public int calcGroupScore(String whichTable, String whichCol){
        ResultSet covixScore;
        int returnScore = 0;

        String getCovixScore = "SELECT "+"sum(" +whichCol + ")"+ " FROM " + whichTable;

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getCovixScore);
            covixScore = preparedStatement.executeQuery();

            while(covixScore.next()){
                returnScore = covixScore.getInt(1);
//                System.out.println("return score: " + returnScore);
                return returnScore;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnScore;
    }

    public int getCovixScore(String email){
//        System.out.println(email);
        ResultSet covixScore;
        int returnScore = 0;

        String getCovixScore = "SELECT covixScore FROM " + Const.USER_TABLE
                + " WHERE "
                + Const.USER_EMAIL
                + "=?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getCovixScore);
            preparedStatement.setString(1,email);

            covixScore = preparedStatement.executeQuery();

            while(covixScore.next()){
                returnScore = covixScore.getInt(1);
//                System.out.println("return score: " + returnScore);
                return returnScore;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnScore;
    }

    public int userConnectionScores(String userID){
        ResultSet covixScore;
        int returnScore = 0;

        String getCovixScore = "SELECT SUM(covixScore) as CovixScore FROM " + "connections" + userID;

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getCovixScore);

            covixScore = preparedStatement.executeQuery();

            while(covixScore.next()){
                returnScore += covixScore.getInt(1);
                return returnScore;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnScore;
    }

    public ArrayList<String> getEmails(String email){
        ResultSet emails;
        ArrayList<String> returnEmails = new ArrayList<String>();

        String getEmail = "SELECT email FROM user WHERE email NOT IN (SELECT email FROM user WHERE email='"+email+"')";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getEmail);

            emails = preparedStatement.executeQuery();

            while(emails.next()){
                returnEmails.add(emails.getString("email"));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnEmails;
    }

    public String getUserID(String email){
//        System.out.println(email);
        ResultSet userid;
        String returnID = "";

        String getUserId = "SELECT userid FROM " + Const.USER_TABLE
                + " WHERE "
                + Const.USER_EMAIL
                + "=?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getUserId);
            preparedStatement.setString(1,email);


            userid = preparedStatement.executeQuery();

            while(userid.next()){
                returnID = userid.getString(1);
                return returnID;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnID;
    }

    public String getFullName(String email){
//        System.out.println(email);
        ResultSet getFullName;
        String fullName = "";

        String getUserId = "SELECT fullName FROM " + Const.USER_TABLE
                + " WHERE "
                + Const.USER_EMAIL
                + "=?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getUserId);
            preparedStatement.setString(1,email);


            getFullName = preparedStatement.executeQuery();

            while(getFullName.next()){
                fullName = getFullName.getString(1);
                return fullName;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return fullName;
    }

    public void connectUser(String userID, String userEmail, String otherUserEmail){

        int covixScore = getCovixScore(otherUserEmail);


        String connectUser = "INSERT INTO " + "connections" + userID +
                "(" + Const.USER_FULLNAME
                + "," + Const.USER_EMAIL + "," + Const.USER_COVIXSCORE
                + ")" + " VALUES(?,?,?)";

        try {

            PreparedStatement preparedStatement = getConnection().prepareStatement(connectUser);

            preparedStatement.setString(1,getFullName(otherUserEmail));
            preparedStatement.setString(2,otherUserEmail);
            preparedStatement.setString(3,String.valueOf(covixScore));


            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void insertIntoGroup(String whichTable, String fullName, String userEmail){

        int covixScore = getCovixScore(userEmail);


        String connectUser = "INSERT INTO " + whichTable +
                "(" + Const.USER_FULLNAME
                + "," + Const.USER_EMAIL + "," + Const.USER_COVIXSCORE
                + ")" + " VALUES(?,?,?)";

        try {

            PreparedStatement preparedStatement = getConnection().prepareStatement(connectUser);

            preparedStatement.setString(1,getFullName(fullName));
            preparedStatement.setString(2,userEmail);
            preparedStatement.setString(3,String.valueOf(covixScore));


            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void disconnectUser(String userID, String otherUserEmail){

        String disconnectUser = "DELETE FROM " + "connections" + userID + " WHERE email=?";

        try {

            PreparedStatement preparedStatement = getConnection().prepareStatement(disconnectUser);

            preparedStatement.setString(1,otherUserEmail);

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteFromGroup(String whichTable, String userEmail){

        String disconnectUser = "DELETE FROM " + whichTable + " WHERE email=?";

        try {

            PreparedStatement preparedStatement = getConnection().prepareStatement(disconnectUser);

            preparedStatement.setString(1,userEmail);

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getIfInGroup(String whichTable, String email){
        ResultSet groupOrNot;
        int returnScore = 0;

        String getCovixScore = "SELECT "+ whichTable + " FROM " + Const.USER_TABLE
                + " WHERE "
                + Const.USER_EMAIL
                + "=?";
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getCovixScore);
            preparedStatement.setString(1,email);

            groupOrNot = preparedStatement.executeQuery();

            while(groupOrNot.next()){
                returnScore = groupOrNot.getInt(1);
                return returnScore;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnScore;
    }

    public int countHowMany(String whichTable){
        ResultSet groupOrNot;
        int returnScore = 0;

        String getCovixScore = "SELECT COUNT(*) FROM " + whichTable;

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(getCovixScore);

            groupOrNot = preparedStatement.executeQuery();

            while(groupOrNot.next()){
                returnScore = groupOrNot.getInt(1);
                return returnScore;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnScore;
    }

}

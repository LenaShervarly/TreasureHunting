/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHelper;
import java.sql.*;
/**
 *
 * @author elena
 */
public class SqlDb {
     public static void main(String[] args) throws SQLException {

       Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Know_hunt?useSSL=false","root","Qq123¤%&");
            // 1. Get a connection to database
            //myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", user, pass);

            // 2. Create a statement
            myStmt = myConn.createStatement();

            // 3. Execute SQL query
            myRs = myStmt.executeQuery("SELECT * FROM know_hunt.qa;");

            // 4. Process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("QUESTION") + " and answer: " + myRs.getString("RIGHT_ANSWER"));
            } 
           

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            } 
        }
    }    
}

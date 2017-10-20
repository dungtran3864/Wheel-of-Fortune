/**
 * Created by Zung on 10/5/17.
 */
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        String answer = null;
        String question = null;

        try {

            // Get a connection to MySQL database
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wheel_of_fortune?useSSL=false",
                    "root", "Tandung1998");

            // Create a statement
            Statement myStmt = myConn.createStatement();

            // Execute SQL Query
            ResultSet myRs = myStmt.executeQuery("select * from Puzzle order by RAND() limit 1;");

            // Process the result set
            while (myRs.next()) {
                answer = myRs.getString("answer");
                question = myRs.getString("question");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        WheelOfFortune wheelOfFortune = new WheelOfFortune();
        wheelOfFortune.gameSimulation(answer, question);
    }

}

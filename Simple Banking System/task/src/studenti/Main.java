package studenti;

import banking.Account;

import java.sql.*;
import java.util.ArrayList;

public class Main {
    static private Connection conn;

    public static void main(String[] args) {
        makeDBConnection("test_karlo.db");
        loadStudents();
    }

    public static void makeDBConnection(String fileName) {

        String createTableSql = "CREATE TABLE IF NOT EXISTS studenti (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	first_name text ,\n"
                + "	last_name text \n"
                + ");";

        try{
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            Statement stmt = conn.createStatement();
            // create a new table if it doesn't exist
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }




    public static void loadStudents() {


        String sql = "SELECT * FROM studenti";

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                String fn = rs.getString("first_name");
                String ln = rs.getString("last_name");

                System.out.println("Student loaded: " + fn + " " + ln);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}

package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    List<Account> accountList = new ArrayList<Account>();
    Connection conn = null;

    public void makeDBConnection(String fileName) {

        String createTableSql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	number text NOT NULL UNIQUE,\n"
                + "	pin text NOT NULL,\n"
                + "	balance integer\n"
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




    public void loadAccounts() {
        accountList = new ArrayList<Account>();


        String sql = "SELECT * FROM card";

        try (Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                Account a = new Account();
                a.setCardNumber(rs.getString("number"));
                a.setPin(rs.getString("pin"));
                a.setBalance(rs.getInt("balance"));
                accountList.add(a);
                System.out.println("Account loaded: " + a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void addAccount(Account a) {
        String sql = "INSERT INTO card(number, pin, balance) VALUES(?, ?, ?)";
        RuntimeException exception = new RuntimeException();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getCardNumber());
            pstmt.setString(2, a.getPin());
            pstmt.setInt(3, a.getBalance());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        accountList.add(a);
    }

    public void updateAccount(Account a) {
        String sql = "UPDATE card SET " +
                "pin = ?," +
                "balance = ?" +
                "WHERE number = ?";
        RuntimeException exception = new RuntimeException();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getPin());
            pstmt.setInt(2, a.getBalance());
            pstmt.setString(3, a.getCardNumber());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void deleteAccount(Account a) {
        String sql = "DELETE FROM card where number = ?";
        RuntimeException exception = new RuntimeException();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getCardNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // cache functions

    public Account findAccount(String cardNumber, String pin) {
        for(Account a : accountList) {
            if(a.getCardNumber().equals(cardNumber) && a.getPin().equals(pin))
                return a;
        }

        return null;
    }

    public Account findAccount(String cardNumber) {
        for(Account a : accountList) {
            if(a.getCardNumber().equals(cardNumber))
                return a;
        }

        return null;
    }

    public List<Account> getAccountList() {
        return accountList;
    }


}

package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    Connection conn = null;

    public AccountDao(Connection conn) {
        this.conn = conn;

        String createTableSql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	number text NOT NULL UNIQUE,\n"
                + "	pin text NOT NULL,\n"
                + "	balance integer, \n"
                + "	user_id integer\n"
                + ");";

        try {

            Statement stmt = conn.createStatement();
            // create a new table if it doesn't exist
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public List<Account> findAllAccounts(User currUser) {
        List<Account> accountList = new ArrayList<Account>();


        String sql = "SELECT * FROM card where user_id=" + currUser.getId();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                Account a = new Account();
                a.setCardNumber(rs.getString("number"));
                a.setPin(rs.getString("pin"));
                a.setBalance(rs.getInt("balance"));
                accountList.add(a);
                //System.out.println("Account loaded: " + a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return accountList;
    }

    public void addAccount(Account a) {
        String sql = "INSERT INTO card(number, pin, balance, user_id) VALUES(?, ?, ?, ?)";
        RuntimeException exception = new RuntimeException();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getCardNumber());
            pstmt.setString(2, a.getPin());
            pstmt.setInt(3, a.getBalance());
            pstmt.setInt(4, a.getUser().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


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

    public Account findAccount(String cardNumber) {

        String sql = "SELECT * FROM card where number=?";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Account a = new Account();
                a.setCardNumber(rs.getString("number"));
                a.setPin(rs.getString("pin"));
                a.setBalance(rs.getInt("balance"));
                return a;
            } else {
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
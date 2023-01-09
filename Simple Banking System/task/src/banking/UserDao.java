package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    Connection conn = null;

    public UserDao(Connection conn) {
        this.conn = conn;

        String createTableSql = "CREATE TABLE IF NOT EXISTS user (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	first_name text NOT NULL,\n"
                + "	last_name text NOT NULL,\n"
                + "	email text NOT NULL,\n"
                + " password text NOT NULL"
                + ")";

        try {

            Statement stmt = conn.createStatement();
            // create a new table if it doesn't exist
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public List<User> findAllUsers() {
        List<User> userList = new ArrayList<User>();


        String sql = "SELECT * FROM user";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));

                userList.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userList;
    }

    public void addUser(User a) {
        String sql = "INSERT INTO user(first_name, last_name, email, password) VALUES(?, ?, ?, ?)";
        RuntimeException exception = new RuntimeException();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getFirstName());
            pstmt.setString(2, a.getLastName());
            pstmt.setString(3, a.getEmail());
            pstmt.setString(4, a.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUser(User u) {
        String sql = "UPDATE user SET " +
                "first_name = ?," +
                "last_name = ?, " +
                "email = ?, " +
                "password = ?, " +
                "WHERE id = ?";
        RuntimeException exception = new RuntimeException();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.getFirstName());
            pstmt.setString(2, u.getLastName());
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getPassword());
            pstmt.setInt(5, u.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void deleteUser(User a) {
        String sql = "DELETE FROM user where number = ?";
        RuntimeException exception = new RuntimeException();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, a.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public User findUser(String email) {

        String sql = "SELECT * FROM user where email=?";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                return u;
            } else {
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
package org.company.TestOfTables;

import org.company.Connection.JDBC;
import java.sql.*;

public class GetAllRows {

    public static void main(String[] args) {
        Statement stmt = null;
        try{
            JDBC.connect();
            stmt = JDBC.connection.createStatement();

            // isbns out
            getAuthorISBN(stmt);
            System.out.println();
            // authors out
            getAuthors(stmt);
            System.out.println();
            // publishers out
            getPublishers(stmt);
            System.out.println();
            // titles out
            getTitles(stmt);

        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } finally {
            // Finally block, used to close resources
            JDBC.close();
        }
    }

    private static void getAuthorISBN(Statement stmt) throws SQLException {
        String query = "SELECT * FROM authorisbn";
        System.out.println("Author's ids:");
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("authorID");
            String isbn = rs.getString("isbn");
            System.out.println(id + "\t" + isbn);
        }
    }

    private static void getAuthors(Statement stmt) throws SQLException {
        String exampleQuery1 = "SELECT * FROM authors";
        System.out.println("Authors:");
        ResultSet rs1 = stmt.executeQuery(exampleQuery1);
        while (rs1.next()) {
            int id = rs1.getInt("authorID");
            String firstName = rs1.getString("firstName");
            String lastName = rs1.getString("lastName");
            System.out.println(id + "\t" + firstName + "\t" + lastName);
        }
    }

    private static void getPublishers(Statement stmt) throws SQLException {
        String query = "SELECT * FROM publishers";
        System.out.println("Publishers:");
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("publisherID");
            String publisherName = rs.getString("publisherName");
            System.out.println(id + "\t" + publisherName);
        }
    }

    private static void getTitles(Statement stmt) throws SQLException {
        String query = "SELECT * FROM titles";
        System.out.println("Titles:");
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("isbn");
            String title = rs.getString("title");
            int editionNumber = rs.getInt("editionNumber");
            int year = rs.getInt("year");
            int publisherID = rs.getInt("publisherID");
            double price = rs.getDouble("price");
            System.out.println(id + "\t" + title + "\t" + editionNumber +
                    "\t" + year + "\t" + publisherID + "\t" + price);
        }
    }
}


package org.company.Query;

import org.company.Connection.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainQuery {
    public static void main(String[] args) {
        Statement stmt = null;
        try {
            JDBC.connect();
            stmt = JDBC.connection.createStatement();
            showMenu(stmt);
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } finally {
            // Finally block, used to close resources
            JDBC.close();
        }
    }

    private static void showMenu(Statement stmt) throws SQLException {
        while (true) {
            System.out.println("1 - get sorted authors;\n" +
                    "2 - add new publisher;\n" +
                    "3 - change publisher name;\n" +
                    "4 - get sorted titles of selected publisher;\n" +
                    "5 - add new author;\n" +
                    "6 - update author's name by id;\n" +
                    "7 - add new publisher, title and authorISBN;\n" +
                    "0 - quit");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            switch (input) {
                case ("1") -> getSortedAuthors(stmt);
                case ("2") -> addNewPublisher(stmt);
                case ("3") -> updatePublishersName(stmt);
                case ("4") -> getTitlesOfPublisher(stmt);
                case ("5") -> addNewAuthor(stmt);
                case ("6") -> updateAuthorsName(stmt);
                case ("7") -> addNewTitle(stmt);

                default -> {
                    return;
                }
            }
        }
    }

    private static void addNewTitle(Statement stmt) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        String newPublisherName = addNewPublisher(stmt);
        String newAuthorFullName = addNewAuthor(stmt);

        System.out.print("Author's ISBN: ");
        String inputISBN = scanner.nextLine();
        String[] fullNameArr = newAuthorFullName.split(" ");
        String firstName = fullNameArr[0];
        String lastName = fullNameArr[1];

        System.out.print("Title: ");
        String inputTitle = scanner.nextLine();

        System.out.print("Edition number: ");
        String inputEditionNumber = scanner.nextLine();

        System.out.print("Year: ");
        String inputYear = scanner.nextLine();

        System.out.print("Price: ");
        String inputPrice = scanner.nextLine();

        String insertTitle = "INSERT INTO Titles (isbn, title, editionNumber, year, publisherID, price) "
                + "VALUES ('" + inputISBN + "', '" + inputTitle + "', " + inputEditionNumber + ", '" + inputYear + "', "
                + "(SELECT publisherID FROM publishers WHERE publisherName = '" + newPublisherName + "'), "
                + inputPrice + ")";
        stmt.executeUpdate(insertTitle);

        String insertAuthorISBN = "INSERT INTO authorisbn (authorID, isbn) VALUES ("
                + "(SELECT authorID FROM authors WHERE firstName = '" + firstName + "' and lastName = '"  + lastName + "'), '"
                + inputISBN + "')";

        stmt.executeUpdate(insertAuthorISBN);
    }

    private static void updateAuthorsName(Statement stmt) throws SQLException {
        System.out.print("Write a author's id: ");
        Scanner scanner = new Scanner(System.in);
        String inputID = scanner.nextLine();

        String getAuthors = "SELECT * FROM authors WHERE authorID = " + inputID;
        ResultSet rs = stmt.executeQuery(getAuthors);
        boolean idExists = rs.next();
        if (idExists) {
            String oldFirstName = rs.getString("firstName");
            String oldLastName = rs.getString("lastName");
            System.out.print("New first name: ");
            String inputFirstName = scanner.nextLine();
            System.out.print("New last name: ");
            String inputLastName = scanner.nextLine();

            String replaceAuthorName = "UPDATE Authors SET firstName = replace(firstName, '" +
                    oldFirstName + "', '" + inputFirstName + "');";
            stmt.executeUpdate(replaceAuthorName);
            replaceAuthorName = "UPDATE Authors SET lastName = replace(lastName, '" +
                    oldLastName + "', '" + inputLastName + "');";
            stmt.executeUpdate(replaceAuthorName);
            System.out.println("Success!");
            return;
        }
        System.out.println("Author with such ID does not exist!");

    }

    private static void getTitlesOfPublisher(Statement stmt) throws SQLException {
        System.out.print("Write a publisher name: ");
        Scanner scanner = new Scanner(System.in);
        String inputName = scanner.nextLine();

        String getTitles = "SELECT * FROM titles " +
                "INNER JOIN publishers ON titles.publisherID = publishers.publisherID " +
                "WHERE publisherName = '" + inputName + "' ORDER BY title";

        ResultSet rs = stmt.executeQuery(getTitles);
        int i = 0;
        while (rs.next()) {
            String title = rs.getString("title");
            System.out.println(++i + "\t" + title);
        }
        System.out.println();
    }

    private static void updatePublishersName(Statement stmt) throws SQLException {
        String getPublishers = "SELECT * FROM publishers";
        System.out.println("Publishers:");
        ResultSet rs1 = stmt.executeQuery(getPublishers);
        while (rs1.next()) {
            int id = rs1.getInt("publisherID");
            String publisherName = rs1.getString("publisherName");
            System.out.println(id + "\t" + publisherName);
        }
        System.out.println();

        System.out.print("Write a name you'd like to change: ");
        Scanner scanner = new Scanner(System.in);
        String inputName = scanner.nextLine();

        System.out.print("Enter a new name: ");
        String inputNewName = scanner.nextLine();

        String replacePublisher = "UPDATE Publishers SET publisherName = replace(publisherName, '" +
                inputName + "', '" + inputNewName + "');";
        stmt.executeUpdate(replacePublisher);
        System.out.println("Success!");
    }

    private static void getSortedAuthors(Statement stmt) throws SQLException {
        String getAuthors = "SELECT * FROM authors ORDER BY firstName, lastName";
        System.out.println("Authors:");
        ResultSet rs1 = stmt.executeQuery(getAuthors);
        while (rs1.next()) {
            int id = rs1.getInt("authorID");
            String firstName = rs1.getString("firstName");
            String lastName = rs1.getString("lastName");
            System.out.println(id + "\t" + firstName + "\t" + lastName);
        }
    }

    private static String addNewPublisher(Statement stmt) throws SQLException {
        System.out.print("Publisher's name: ");
        Scanner scanner = new Scanner(System.in);
        String inputName = scanner.nextLine();

        String addPublisher = "INSERT INTO Publishers (publisherName)" + "VALUES ('" + inputName
                + "');";

        stmt.executeUpdate(addPublisher);
        System.out.println("Success!");
        return inputName;
    }

    private static String addNewAuthor(Statement stmt) throws SQLException {
        System.out.print("Author's first name: ");
        Scanner scanner = new Scanner(System.in);
        String inputFirstName = scanner.nextLine();

        System.out.print("Author's last name: ");
        String inputLastName = scanner.nextLine();

        String addAuthor = "INSERT INTO Authors (firstName, lastName)" + "VALUES ('" + inputFirstName
                + "', '" + inputLastName + "');";

        stmt.executeUpdate(addAuthor);
        return inputFirstName + " " + inputLastName;
    }
}

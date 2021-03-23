package example.boundary;

import example.DatabaseConnectionProvider;
import example.DatabaseUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.*;

public class JdbcExerciseTest {

    @Before
    public void before() throws IOException, SQLException {
        DatabaseUtil.prepareDatabase();
    }

    /**
     * Test demonstruje wielokrotne tworzenie połączenia i zamykanie go za każdym razem za pomocą konstrukcji try-with-resources
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">try-with-resources</a>
     * DriverManager.getConnection korzysta z  Java SE Service Provider mechanism i dlatego nie jest potrzebne
     * ładowanie sterownika za pomocą kostrukcji Class.forName
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/sql/DriverManager.html">DriverManager</a>
     */

    @Test
    public void getConnection() throws SQLException, ClassNotFoundException {
        //Class.forName("com.mysql.jdbc.Driver");
        for (int i = 0; i < 500; i++) {
            // try () {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?characterEncoding=utf8",
                    "root", "sda")) {
                ;
                boolean valid = connection.isValid(1000);
                System.out.println(valid);
            }
        }
    }

    /**
     * Test demonstruje pobranie danych z bazy za pomocą klas Statement oraz nawigowanie po wynikach reprezentowanych przez instancje klasy ResultSet
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testSimpleSelect() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery("select * from example_users")) {

        }
    }

    /**
     * Test demonstruje możliwość aktualizowania danych za pomocą klasy ResultSet.
     * Aby było to możliwe konieczne jest podanie wartosci ResultSet.CONCUR_UPDATABLE podczas tworzenia instancji klasy Statement
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testResultSetUpdate() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
             ResultSet resultSet = statement.executeQuery("select * from example_users")) {

        }
    }

    /**
     * Test demonstruje usuwanie danych za pomocą SQL delete
     * Wykorzystywana metoda executeUpdate zwraca ilość usnietych wierszy
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testDelete() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {


        }
    }

    /**
     * Test demonstruje aktualizowanie danych.
     * We wszystkich sytuacjach gdy komenda sql zmienia dane w bazie (delete, update) zamiast executeQuery wywołujemy executeUpdate
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testUpdate() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {

        }
    }

    /**
     * Test demonstruje przykład SQL INJECTION poprzez błędne budowanie zapytania z parametrami
     * TAK NIE ROBIMY!!!
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testGetUserByNameAndPassword() throws IOException, SQLException {
        String userName = "Jan";
        String password = "'123' OR 1=1";
        String sql = "select * from example_users WHERE name='" + userName + "' AND password=" + password;

    }

    /**
     * Test demonstruje poprawne budowanie zapytania z parametrami poprzez wykorzystanie PreparedStatement
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testGetUserByNameAndPasswordWithPreparedStatement() throws IOException, SQLException {
        String userName = "Jan";
        String password = "123";
        String sql = "select * from example_users WHERE name=? AND password=?";

    }

    /**
     * Test demonstruje wykonanie wielkorotnego insereta za pomocą operacji executeBatch
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testInsert() throws IOException, SQLException {
        try (Connection connection = DatabaseConnectionProvider.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("insert into example_users (name,password,age)" +
                    "values (?,?,?)");

        //            System.out.println("updated count: " + ints[0]);
        }
    }

    /**
     * Test demonstruje wykonanie dwóch operacji w jednej transakcji
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testInsertWithTransaction() throws IOException, SQLException {
        Connection connection = DatabaseConnectionProvider.getConnection();
        //try {
          //  connection.setAutoCommit(false);

    }

    /**
     * Test demonstruje izolowanie transakcji.
     * Dopóki transakcja pierwsza nie zostanie skomitowana, zmiany w niej wprowadzone nie beda widoczne przez inne transakcje
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testInsertWithTransaction2() throws IOException, SQLException {
    //    Connection connection = DatabaseConnectionProvider.getConnection();
//        try {
  //          connection.setAutoCommit(false);


    }
}
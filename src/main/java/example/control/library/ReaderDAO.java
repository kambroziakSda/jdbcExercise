package example.control.library;

import example.entity.library.Reader;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReaderDAO implements AutoCloseable {

    private final Connection connection;

    public ReaderDAO(Connection connection) {
        this.connection = connection;
    }

    public Integer deleteReader(int readerId) throws SQLException {
        String sql = "delete FROM czytelnik  WHERE id_czytelnik = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, readerId);
            return preparedStatement.executeUpdate();
        }
    }

    public Optional<Reader> findReaderById(int readerId) throws SQLException {
        String sql = "SELECT id_czytelnik, login,haslo,imie,nazwisko,adres, miasto, wojewodztwo,telefon, kod_pocztowy, email FROM czytelnik  WHERE id_czytelnik = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, readerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            Reader reader = new Reader();
            reader.setId(resultSet.getInt(1));
            reader.setLogin(resultSet.getString(2));
            reader.setPassword(resultSet.getString(3));
            reader.setFirstName(resultSet.getString(4));
            reader.setLastName(resultSet.getString(5));
            reader.setAddress(resultSet.getString(6));
            reader.setCity(resultSet.getString(7));
            reader.setState(resultSet.getString(8));
            reader.setPhone(resultSet.getString(9));
            reader.setPostalCode(resultSet.getString(10));
            reader.setEmail(resultSet.getString(11));

            return Optional.of(reader);
        }

    }

    public List<Integer> insertReaders(List<Reader> readers) throws SQLException {
        String sql = "INSERT INTO czytelnik (login,haslo,imie,nazwisko,adres, miasto, wojewodztwo,telefon, kod_pocztowy, email) values (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Reader readerToInsert : readers) {
                populateWithReader(readerToInsert, preparedStatement);
                preparedStatement.addBatch();
                //preparedStatement.addBatch(); - dodaje duplikat
            }
            int[] inserted = preparedStatement.executeBatch();
            return Arrays.stream(inserted).boxed().collect(Collectors.toList());
        }
    }

    public List<Integer> updateReaders(List<Reader> readers) throws SQLException {
        return Collections.emptyList();
    }

    public boolean insertReader(Reader reader) throws SQLException {
        String sql = "INSERT INTO czytelnik (login,haslo,imie,nazwisko,adres, miasto, wojewodztwo,telefon, kod_pocztowy, email) values (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            populateWithReader(reader, preparedStatement);

            int insertedCount = preparedStatement.executeUpdate();

            return insertedCount > 0;
        }
    }

    public int updateReader(Reader newReaderData) throws SQLException {
        String sql = "UPDATE czytelnik set login = ? , haslo = ?, imie = ?, nazwisko = ?, adres = ?, miasto =?, wojewodztwo = ?,telefon = ?, kod_pocztowy = ?, email =? " +
                " WHERE id_czytelnik = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            populateWithReader(newReaderData, preparedStatement);
            preparedStatement.setInt(11, newReaderData.getId());
            return preparedStatement.executeUpdate();
        }
        //Integer -> int autounboxing
        //int -> Integer autoboxing
    }

    @Override
    public void close() throws Exception {
        connection.close();

    }

    private void populateWithReader(Reader reader, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, reader.getLogin());
        preparedStatement.setString(2, reader.getPassword());
        preparedStatement.setString(3, reader.getFirstName());
        preparedStatement.setString(4, reader.getLastName());
        preparedStatement.setString(5, reader.getAddress());
        preparedStatement.setString(6, reader.getCity());
        preparedStatement.setString(7, reader.getState());
        preparedStatement.setString(8, reader.getPhone());
        preparedStatement.setString(9, reader.getPostalCode());
        preparedStatement.setString(10, reader.getEmail());
    }
}

package example.control.library;

import example.entity.library.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReaderDAO implements AutoCloseable {

    private final Connection connection;

    public ReaderDAO(Connection connection) {
        this.connection = connection;
    }

    public Integer deleteReader(int readerId) {
        //todo
        return null;
    }

    public Reader findReaderById(int readerId) {
        return null;
    }

    public List<Integer> insertReaders(List<Reader> readers) {
        return Collections.emptyList();
    }

    public boolean insertReader(Reader reader) throws SQLException {
        String sql = "INSERT INTO czytelnik (login,haslo,imie,nazwisko,adres, miasto, wojewodztwo,telefon, kod_pocztowy, email) values (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

            int insertedCount = preparedStatement.executeUpdate();

            return insertedCount > 0;
        }
    }

    public Reader updateReader(Reader newReader) {
        return null;
    }

    @Override
    public void close() throws Exception {
        connection.close();

    }
}

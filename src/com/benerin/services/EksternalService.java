package com.benerin.services;

import com.benerin.DatabaseConnection.DatabaseConnection;
import com.benerin.interfaces.CRUDOperations;
import com.benerin.models.Eksternal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EksternalService implements CRUDOperations<Eksternal> {

    @Override
    public void add(Eksternal eksternal) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO eksternal (nama, jenis) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, eksternal.getNama());
            statement.setString(2, eksternal.getJenis());
            statement.executeUpdate();
        }
    }

    @Override
    public void update(Eksternal eksternal) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE eksternal SET nama = ?, jenis = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, eksternal.getNama());
            statement.setString(2, eksternal.getJenis());
            statement.setInt(3, eksternal.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM eksternal WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}

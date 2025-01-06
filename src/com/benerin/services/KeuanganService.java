package com.benerin.services;

import com.benerin.DatabaseConnection.DatabaseConnection;
import com.benerin.interfaces.CRUDOperations;
import com.benerin.models.Keuangan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KeuanganService implements CRUDOperations<Keuangan> {
    @Override
    public void add(Keuangan keuangan) throws SQLException {
        // try (Connection connection = DatabaseConnection.getConnection()) {
        //     String query = "INSERT INTO keuangan (nama, jenis) VALUES (?, ?)";
        //     PreparedStatement statement = connection.prepareStatement(query);
        //     statement.setString(1, eksternal.getNama());
        //     statement.setString(2, eksternal.getJenis());
        //     statement.executeUpdate();
        // }
    }

    @Override
    public void update(Keuangan keuangan) throws SQLException {
        // try (Connection connection = DatabaseConnection.getConnection()) {
        //     String query = "UPDATE keuangan SET nama = ?, jenis = ? WHERE id = ?";
        //     PreparedStatement statement = connection.prepareStatement(query);
        //     statement.setString(1, eksternal.getNama());
        //     statement.setString(2, eksternal.getJenis());
        //     statement.setInt(3, eksternal.getId());
        //     statement.executeUpdate();
        // }
    }

    @Override
    public void delete(int id) throws SQLException {
        // try (Connection connection = DatabaseConnection.getConnection()) {
        //     String query = "DELETE FROM keuangan WHERE id = ?";
        //     PreparedStatement statement = connection.prepareStatement(query);
        //     statement.setInt(1, id);
        //     statement.executeUpdate();
        // }
    }
}

package com.benerin.services;

import com.benerin.DatabaseConnection.DatabaseConnection;
import com.benerin.interfaces.CRUDOperations;
import com.benerin.models.Anggota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnggotaService implements CRUDOperations<Anggota> {

    @Override
    public void add(Anggota anggota) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO anggota (nama, jabatan) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, anggota.getNama());
            statement.setString(2, anggota.getJabatan());
            statement.executeUpdate();
        }
    }


    @Override
    public void update(Anggota anggota) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE anggota SET nama = ?, jabatan = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, anggota.getNama());
            statement.setString(2, anggota.getJabatan());
            statement.setInt(3, anggota.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM anggota WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}

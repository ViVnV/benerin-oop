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
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO keuangan (sumber_dana, keterangan, jumlah, jenis_transaksi, tanggal, anggota_id, eksternal_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, keuangan.getSumberDana());
                statement.setString(2, keuangan.getKeterangan());
                statement.setDouble(3, keuangan.getJumlah());
                statement.setString(4, keuangan.getJenisTransaksi());
                statement.setString(5, keuangan.getTanggal());

                if (keuangan.getAnggotaId() == 0) {
                    statement.setNull(6, java.sql.Types.INTEGER);
                } else {
                    statement.setInt(6, keuangan.getAnggotaId());
                }

                if (keuangan.getEksternalId() == 0) {
                    statement.setNull(7, java.sql.Types.INTEGER);
                } else {
                    statement.setInt(7, keuangan.getEksternalId());
                }

                statement.executeUpdate();
            }
        }

        @Override
        public void update(Keuangan keuangan) throws SQLException {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "UPDATE keuangan SET sumber_dana = ?, keterangan = ?, jumlah = ?, jenis_transaksi = ?, tanggal = ?, anggota_id = ?, eksternal_id = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, keuangan.getSumberDana());
                statement.setString(2, keuangan.getKeterangan());
                statement.setDouble(3, keuangan.getJumlah());
                statement.setString(4, keuangan.getJenisTransaksi());
                statement.setString(5, keuangan.getTanggal());

                if (keuangan.getAnggotaId() == 0) {
                    statement.setNull(6, java.sql.Types.INTEGER);
                } else {
                    statement.setInt(6, keuangan.getAnggotaId());
                }

                if (keuangan.getEksternalId() == 0) {
                    statement.setNull(7, java.sql.Types.INTEGER);
                } else {
                    statement.setInt(7, keuangan.getEksternalId());
                }

                statement.setInt(8, keuangan.getId());
                statement.executeUpdate();
            }
        }


        @Override
        public void delete(int id) throws SQLException {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM keuangan WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        }
}

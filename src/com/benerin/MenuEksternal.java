package com.benerin;

import com.benerin.DatabaseConnection.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MenuEksternal extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField namaField;
    private JTextField jenisField;

    public MenuEksternal(String username, String role) {
        setTitle("Menu Eksternal - Aplikasi Pencatatan Keuangan");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel untuk form input Nama
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Nama:"));
        namaField = new JTextField();
        inputPanel.add(namaField);

        // Panel untuk form input Nama
        inputPanel.add(new JLabel("Jenis:"));
        jenisField = new JTextField();
        inputPanel.add(jenisField);

        // Panel untuk tombol aksi
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Tambah");
        JButton updateButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");
        JButton backButton = new JButton("Kembali");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Table untuk menampilkan data eksternal
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Jenis"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Tambahkan panel ke frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data eksternal
        loadData();

        // Action listeners
        addButton.addActionListener(e -> addEksternal());
        updateButton.addActionListener(e -> updateEksternal());
        deleteButton.addActionListener(e -> deleteEksternal());
        backButton.addActionListener(e -> {
            dispose();
            new MainMenu(username, role);
        });

        setVisible(true);
    }

    // Method untuk memuat data eksternal dari database
    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM eksternal";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                String jenis = resultSet.getString("jenis");
                tableModel.addRow(new Object[]{id, nama, jenis});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!");
        }
    }

    // Method untuk menambahkan eksternal baru
    private void addEksternal() {
        String nama = namaField.getText();
        String jenis = jenisField.getText();

        if (nama.isEmpty() || jenis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO eksternal (nama, jenis) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nama);
            statement.setString(2, jenis);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Eksternal berhasil ditambahkan!");
            loadData();
            namaField.setText("");
            jenisField.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding eksternal!");
        }
    }

    // Method untuk mengedit data eksternal
    private void updateEksternal() {
        int selectedRow = table.getSelectedRow();

        // Jika tidak ada baris yang dipilih
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih eksternal yang ingin diedit!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nama = namaField.getText();
        String jenis = jenisField.getText();

        if (nama.isEmpty() || jenis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE eksternal SET nama = ?, jenis = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nama);
            statement.setString(2, jenis);
            statement.setInt(3, id);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Eksternal berhasil diperbarui!");
            loadData();
            namaField.setText("");
            jenisField.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating eksternal!");
        }
    }

    // Method untuk menghapus eksternal
    private void deleteEksternal() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih eksternal yang ingin dihapus!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus eksternal ini?");
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM eksternal WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Eksternal berhasil dihapus!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting eksternal!");
        }
    }
}
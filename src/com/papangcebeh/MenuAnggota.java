package com.papangcebeh;

import com.papangcebeh.DatabaseConnection.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class MenuAnggota extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField namaField;
    private JTextField jabatanField;

    public MenuAnggota() {
        setTitle("Menu Anggota - Aplikasi Pencatatan Keuangan");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel untuk form input
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Nama:"));
        namaField = new JTextField();
        inputPanel.add(namaField);

        inputPanel.add(new JLabel("Jabatan:"));
        jabatanField = new JTextField();
        inputPanel.add(jabatanField);

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

        // Table untuk menampilkan data anggota
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Jabatan"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Tambahkan panel ke frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data anggota
        loadData();

        // Action listeners
        addButton.addActionListener(e -> addAnggota());
        updateButton.addActionListener(e -> updateAnggota());
        deleteButton.addActionListener(e -> deleteAnggota());
        backButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Method untuk memuat data anggota dari database
    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM anggota";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                String jabatan = resultSet.getString("jabatan");
                tableModel.addRow(new Object[]{id, nama, jabatan});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!");
        }
    }

    // Method untuk menambahkan anggota baru
    private void addAnggota() {
        String nama = namaField.getText();
        String jabatan = jabatanField.getText();

        if (nama.isEmpty() || jabatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO anggota (nama, jabatan) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nama);
            statement.setString(2, jabatan);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Anggota berhasil ditambahkan!");
            loadData();
            namaField.setText("");
            jabatanField.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding anggota!");
        }
    }

    // Method untuk mengedit data anggota
    private void updateAnggota() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang ingin diedit!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nama = namaField.getText();
        String jabatan = jabatanField.getText();

        if (nama.isEmpty() || jabatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE anggota SET nama = ?, jabatan = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nama);
            statement.setString(2, jabatan);
            statement.setInt(3, id);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Anggota berhasil diperbarui!");
            loadData();
            namaField.setText("");
            jabatanField.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating anggota!");
        }
    }

    // Method untuk menghapus anggota
    private void deleteAnggota() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang ingin dihapus!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus anggota ini?");
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM anggota WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Anggota berhasil dihapus!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting anggota!");
        }
    }
}
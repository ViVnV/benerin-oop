package com.benerin;

import com.benerin.DatabaseConnection.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MenuBendahara extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;

    private String username;
    private String role;

    public MenuBendahara(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("Menu Bendahara - CRUD Bendahara");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel input
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        // Panel tombol
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Tambah");
        JButton updateButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");
        JButton backButton = new JButton("Kembali");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Tabel untuk menampilkan data user
        tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Email"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Tambahkan panel ke frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data user
        loadData();

        // Action listeners
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        backButton.addActionListener(e -> back());

        setVisible(true);
    }

    // Method untuk memuat data user dari database
    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id, username, email, role FROM users WHERE role = 'bendahara'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                tableModel.addRow(new Object[]{id, username, email});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!");
        }
    }

    // Method untuk menambahkan user baru
    private void addUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password, email, role) VALUES (?, SHA2(?, 256), ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, "bendahara");
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
            loadData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding user!");
        }
    }

    // Method untuk mengedit user
    private void updateUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user yang ingin diedit!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username, Email, dan Password tidak boleh kosong!");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE users SET username = ?, password = SHA2(?, 256), email = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setInt(4, id);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "User berhasil diperbarui!");
            loadData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user!");
        }
    }

    // Method untuk menghapus user
    private void deleteUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user yang ingin dihapus!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus user ini?");
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting user!");
        }
    }

    // Method untuk membersihkan field input
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
    }

    public void back() {
        dispose();
        new MainMenu(this.username, this.role);
    }
}

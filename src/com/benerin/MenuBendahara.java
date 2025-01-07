package com.benerin;

import com.benerin.DatabaseConnection.DatabaseConnection;
import com.benerin.interfaces.BasicForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MenuBendahara extends JFrame implements BasicForm {
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
        setSize(950, 600); // Adjusted window size to match other forms
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Panel input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(passwordField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(emailField, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Tabel untuk menampilkan data user
        tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Email"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addButton = new JButton("Tambah");
        addButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addButton.setPreferredSize(new Dimension(120, 40));
        JButton updateButton = new JButton("Edit");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 16));
        updateButton.setPreferredSize(new Dimension(120, 40));
        JButton deleteButton = new JButton("Hapus");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        deleteButton.setPreferredSize(new Dimension(120, 40));
        JButton backButton = new JButton("Kembali");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load data user
        loadData();

        // Action listeners
        addButton.addActionListener(e -> add());
        updateButton.addActionListener(e -> update());
        deleteButton.addActionListener(e -> delete());
        backButton.addActionListener(e -> back());

        setVisible(true);
    }

    // Method untuk memuat data user dari database
    @Override
    public void loadData() {
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
    @Override
    public void add() {
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

            // if username already exists
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!");
                return;
            }

            JOptionPane.showMessageDialog(this, "Error adding user!");
        }
    }

    // Method untuk mengedit user
    @Override
    public void update() {
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
    @Override
    public void delete() {
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

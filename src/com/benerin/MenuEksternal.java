package com.benerin;

import com.benerin.DatabaseConnection.DatabaseConnection;
import com.benerin.interfaces.BasicForm;
import com.benerin.models.Eksternal;
import com.benerin.services.EksternalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MenuEksternal extends JFrame implements BasicForm {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField namaField;
    private JTextField jenisField;
    private EksternalService eksternalService;

    private String username;
    private String role;

    public MenuEksternal(String username, String role) {
        this.username = username;
        this.role = role;

        eksternalService = new EksternalService();

        setTitle("Menu Eksternal - Aplikasi Pencatatan Keuangan");
        setSize(950, 500); // Adjusted window size to match other forms
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Panel untuk form input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nama
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel namaLabel = new JLabel("Nama:");
        namaLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        inputPanel.add(namaLabel, gbc);

        gbc.gridx = 1;
        namaField = new JTextField(20);
        namaField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(namaField, gbc);

        // Jenis
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel jenisLabel = new JLabel("Jenis:");
        jenisLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        inputPanel.add(jenisLabel, gbc);

        gbc.gridx = 1;
        jenisField = new JTextField(20);
        jenisField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(jenisField, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table untuk menampilkan data eksternal
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Jenis"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel untuk tombol aksi
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton addButton = new JButton("Tambah");
        addButton.setFont(new Font("Arial", Font.PLAIN, 18));
        addButton.setPreferredSize(new Dimension(150, 40));
        JButton updateButton = new JButton("Edit");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 18));
        updateButton.setPreferredSize(new Dimension(150, 40));
        JButton deleteButton = new JButton("Hapus");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteButton.setPreferredSize(new Dimension(150, 40));
        JButton backButton = new JButton("Kembali");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setPreferredSize(new Dimension(150, 40));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load data eksternal
        loadData();

        // Action listeners
        addButton.addActionListener(e -> add());
        updateButton.addActionListener(e -> update());
        deleteButton.addActionListener(e -> delete());
        backButton.addActionListener(e -> back());

        setVisible(true);
    }

    // Method untuk memuat data eksternal dari database
    @Override
    public void loadData() {
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
    @Override
    public void add() {
        String nama = namaField.getText();
        String jenis = jenisField.getText();

        if (nama.isEmpty() || jenis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try {
            eksternalService.add(new Eksternal(nama, jenis));
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
    @Override
    public void update() {
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

        try {
            eksternalService.update(new Eksternal(id, nama, jenis));
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
    @Override
    public void delete() {
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

        try {
            eksternalService.delete(id);
            JOptionPane.showMessageDialog(this, "Eksternal berhasil dihapus!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting eksternal!");
        }
    }

    @Override
    public void back() {
        dispose();
        new MainMenu(this.username, this.role);
    }
}

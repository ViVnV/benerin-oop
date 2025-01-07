package com.benerin;

import com.benerin.DatabaseConnection.DatabaseConnection;
import com.benerin.interfaces.BasicForm;
import com.benerin.models.Anggota;
import com.benerin.services.AnggotaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MenuAnggota extends JFrame implements BasicForm {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField namaField;
    private JTextField jabatanField;
    private AnggotaService anggotaService;

    private String username;
    private String role;

    public MenuAnggota(String username, String role) {
        this.username = username;
        this.role     = role;

        anggotaService = new AnggotaService();

        setTitle("Menu Anggota - Aplikasi Pencatatan Keuangan");
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

        // Jabatan
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel jabatanLabel = new JLabel("Jabatan:");
        jabatanLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        inputPanel.add(jabatanLabel, gbc);

        gbc.gridx = 1;
        jabatanField = new JTextField(20);
        jabatanField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(jabatanField, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table untuk menampilkan data anggota
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Jabatan"}, 0);
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

        // Load data anggota
        loadData();

        // Action listeners
        addButton.addActionListener(e -> add());
        updateButton.addActionListener(e -> update());
        deleteButton.addActionListener(e -> delete());
        backButton.addActionListener(e -> back());

        setVisible(true);
    }

    @Override
    public void loadData() {
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

    @Override
    public void add() {
        String nama = namaField.getText();
        String jabatan = jabatanField.getText();

        if (nama.isEmpty() || jabatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try {
            anggotaService.add(new Anggota(nama, jabatan));
            JOptionPane.showMessageDialog(this, "Anggota berhasil ditambahkan!");
            loadData();
            namaField.setText("");
            jabatanField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding anggota!");
        }
    }

    @Override
    public void update() {
        int selectedRow = table.getSelectedRow();

        // Cek apakah ada baris yang dipilih
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang ingin diedit!");
            return;
        }

        // Ambil nilai ID dari baris yang dipilih
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nama = namaField.getText();
        String jabatan = jabatanField.getText();

        // Validasi input
        if (nama.isEmpty() || jabatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        try {
            // Buat objek Anggota dengan data baru dan panggil update() dari AnggotaService
            Anggota anggota = new Anggota(id, nama, jabatan);
            anggotaService.update(anggota);

            JOptionPane.showMessageDialog(this, "Anggota berhasil diperbarui!");
            loadData();

            // Kosongkan field input
            namaField.setText("");
            jabatanField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating anggota!");
        }
    }

    @Override
    public void delete() {
        int selectedRow = table.getSelectedRow();

        // Cek apakah ada baris yang dipilih
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang ingin dihapus!");
            return;
        }

        // Ambil nilai ID dari baris yang dipilih
        int id = (int) tableModel.getValueAt(selectedRow, 0);

        // Konfirmasi penghapusan
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus anggota ini?");
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // Panggil delete() dari AnggotaService
            anggotaService.delete(id);

            JOptionPane.showMessageDialog(this, "Anggota berhasil dihapus!");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting anggota!");
        }
    }

    @Override
    public void back() {
        dispose();
        new MainMenu(this.username, this.role);
    }
}

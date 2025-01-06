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
        addButton.addActionListener(e -> add());
        updateButton.addActionListener(e -> update());
        deleteButton.addActionListener(e -> delete());
        backButton.addActionListener(e -> back());

        setVisible(true);
    }

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

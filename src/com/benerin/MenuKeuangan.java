package com.benerin;

import com.benerin.DatabaseConnection.DatabaseConnection;
import com.benerin.interfaces.BasicForm;
import com.benerin.models.Keuangan;
import com.benerin.services.KeuanganService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MenuKeuangan extends JFrame implements BasicForm {
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> sumberDanaComboBox;
    private JComboBox<String> anggotaComboBox;
    private JComboBox<String> eksternalComboBox;
    private JTextField keteranganField;
    private JTextField jumlahField;
    private JComboBox<String> jenisTransaksiComboBox;
    private JTextField tanggalField;

    private String username;
    private String role;

    private KeuanganService keuanganService;

    public MenuKeuangan(String username, String role) {
        this.username = username;
        this.role = role;

        keuanganService = new KeuanganService();

        setTitle("Menu Keuangan - CRUD Aktivitas Keuangan");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Panel input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Input Transaksi"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Sumber Dana
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Sumber Dana:"), gbc);
        gbc.gridx = 1;
        sumberDanaComboBox = new JComboBox<>(new String[]{"anggota", "eksternal"});
        sumberDanaComboBox.addActionListener(e -> toggleComboBox());
        inputPanel.add(sumberDanaComboBox, gbc);

        // Nama Sumber Dana
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Nama Sumber Dana:"), gbc);
        gbc.gridx = 1;
        anggotaComboBox = new JComboBox<>();
        eksternalComboBox = new JComboBox<>();
        eksternalComboBox.setVisible(false);
        JPanel comboPanel = new JPanel(new CardLayout());
        comboPanel.add(anggotaComboBox, "anggota");
        comboPanel.add(eksternalComboBox, "eksternal");
        inputPanel.add(comboPanel, gbc);

        // Keterangan
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Keterangan:"), gbc);
        gbc.gridx = 1;
        keteranganField = new JTextField();
        inputPanel.add(keteranganField, gbc);

        // Jumlah
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Jumlah:"), gbc);
        gbc.gridx = 1;
        jumlahField = new JTextField();
        inputPanel.add(jumlahField, gbc);

        // Jenis Transaksi
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Jenis Transaksi:"), gbc);
        gbc.gridx = 1;
        jenisTransaksiComboBox = new JComboBox<>(new String[]{"pemasukan", "pengeluaran"});
        inputPanel.add(jenisTransaksiComboBox, gbc);

        // Tanggal
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        tanggalField = new JTextField();
        inputPanel.add(tanggalField, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel tabel
        tableModel = new DefaultTableModel(new String[]{"ID", "Sumber Dana", "Nama", "Keterangan", "Jumlah", "Jenis", "Tanggal"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addButton = new JButton("Tambah");
        JButton updateButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");
        JButton backButton = new JButton("Kembali");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadData();
        loadAnggota();
        loadEksternal();

        // Action listeners
        addButton.addActionListener(e -> add());
        updateButton.addActionListener(e -> update());
        deleteButton.addActionListener(e -> delete());
        backButton.addActionListener(e -> back());

        setVisible(true);
    }

    private void toggleComboBox() {
        CardLayout cl = (CardLayout) ((JPanel) eksternalComboBox.getParent()).getLayout();
        cl.show(eksternalComboBox.getParent(), sumberDanaComboBox.getSelectedItem().toString());
    }

    @Override
    public void loadData() {
        tableModel.setRowCount(0);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT keuangan.id, keuangan.sumber_dana, COALESCE(eksternal.nama, anggota.nama) AS nama, COALESCE(eksternal.jenis, anggota.jabatan) AS jenis, keuangan.keterangan, keuangan.jumlah, keuangan.jenis_transaksi, keuangan.tanggal " +
                    "FROM keuangan " +
                    "LEFT JOIN anggota ON keuangan.anggota_id = anggota.id " +
                    "LEFT JOIN eksternal ON keuangan.eksternal_id = eksternal.id";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String sumberDana = resultSet.getString("sumber_dana");
                String nama = resultSet.getString("nama");
                String keterangan = resultSet.getString("keterangan");
                double jumlah = resultSet.getDouble("jumlah");
                String jenisTransaksi = resultSet.getString("jenis_transaksi");
                String tanggal = resultSet.getString("tanggal");

                tableModel.addRow(new Object[]{id, sumberDana, nama, keterangan, jumlah, jenisTransaksi, tanggal});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data!");
        }
    }

    private void loadAnggota() {
        anggotaComboBox.removeAllItems();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id, nama FROM anggota";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                anggotaComboBox.addItem(resultSet.getInt("id") + " - " + resultSet.getString("nama"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading anggota!");
        }
    }

    private void loadEksternal() {
        eksternalComboBox.removeAllItems();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id, nama FROM eksternal";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                eksternalComboBox.addItem(resultSet.getInt("id") + " - " + resultSet.getString("nama"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading eksternal!");
        }
    }

    @Override
    public void add() {
        String sumberDana = (String) sumberDanaComboBox.getSelectedItem();
        String selectedNama = sumberDana.equals("anggota") ? (String) anggotaComboBox.getSelectedItem() : (String) eksternalComboBox.getSelectedItem();
        int id = Integer.parseInt(selectedNama.split(" - ")[0]);

        String keterangan = keteranganField.getText();
        double jumlah = Double.parseDouble(jumlahField.getText());
        String jenisTransaksi = (String) jenisTransaksiComboBox.getSelectedItem();
        String tanggal = tanggalField.getText();

        int anggotaId = sumberDana.equals("anggota") ? id : 0;
        int eksternalId = sumberDana.equals("eksternal") ? id : 0;

        try {
            keuanganService.add(new Keuangan(sumberDana, keterangan, jumlah, jenisTransaksi, tanggal, anggotaId, eksternalId));

            JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!");
            loadData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding transaction!");
        }
    }

    @Override
    public void update() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin diperbarui!");
            return;
        }

        int keuanganId = (int) tableModel.getValueAt(selectedRow, 0);
        String sumberDana = (String) sumberDanaComboBox.getSelectedItem();
        String selectedNama = sumberDana.equals("anggota") ? (String) anggotaComboBox.getSelectedItem() : (String) eksternalComboBox.getSelectedItem();
        int id = Integer.parseInt(selectedNama.split(" - ")[0]);

        String keterangan = keteranganField.getText();
        double jumlah = Double.parseDouble(jumlahField.getText());
        String jenisTransaksi = (String) jenisTransaksiComboBox.getSelectedItem();
        String tanggal = tanggalField.getText();

        int anggotaId = sumberDana.equals("anggota") ? id : 0;
        int eksternalId = sumberDana.equals("eksternal") ? id : 0;

        try {
            keuanganService.update(new Keuangan(keuanganId, sumberDana, keterangan, jumlah, jenisTransaksi, tanggal, anggotaId, eksternalId));

            JOptionPane.showMessageDialog(this, "Transaksi berhasil diperbarui!");
            loadData();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating transaction!");
        }
    }

    // Method untuk menghapus transaksi yang dipilih
    @Override
    public void delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus transaksi ini?");
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM keuangan WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting transaction!");
        }
    }

    private void clearFields() {
        jumlahField.setText("");
        tanggalField.setText("");
        sumberDanaComboBox.setSelectedIndex(0);
        anggotaComboBox.setSelectedIndex(0);
        eksternalComboBox.setSelectedIndex(0);
        jenisTransaksiComboBox.setSelectedIndex(0);
    }

    @Override
    public void back() {
        dispose();
        new MainMenu(this.username, this.role);
    }
}
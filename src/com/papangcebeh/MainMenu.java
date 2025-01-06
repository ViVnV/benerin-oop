package com.papangcebeh;

import com.papangcebeh.DatabaseConnection.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainMenu extends JFrame {
    private String username;
    private String userRole;
    private JLabel totalDanaLabel;

    public MainMenu(String username, String role) {
        this.username = username;
        this.userRole = role;
        setTitle("Main Menu - Aplikasi Pencatatan Keuangan");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Panel atas untuk selamat datang dan total dana
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JLabel welcomeLabel = new JLabel("Selamat Datang, " + this.username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalDanaLabel = new JLabel("Total Dana Saat Ini: Rp 0", JLabel.CENTER);
        totalDanaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(welcomeLabel);
        topPanel.add(totalDanaLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Panel menu di tengah
        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        addMenuItems(menuPanel);
        mainPanel.add(menuPanel, BorderLayout.CENTER);

        // Tombol Logout di bawah
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        // Load total dana
        loadTotalDana();

        setVisible(true);
    }

    private void addMenuItems(JPanel menuPanel) {
        if ("admin".equals(userRole)) {
            addAdminMenu(menuPanel);
        } else if ("bendahara".equals(userRole)) {
            addBendaharaMenu(menuPanel);
        }
    }

    private void addAdminMenu(JPanel panel) {
        JButton anggotaButton = new JButton("Menu Anggota");
        anggotaButton.addActionListener(e -> {
            dispose();
            new MenuAnggota();
        });
        panel.add(anggotaButton);

        JButton eksternalButton = new JButton("Menu Eksternal");
        eksternalButton.addActionListener(e -> {
            dispose();
            new MenuEksternal(this.username, this.userRole);
        });
        panel.add(eksternalButton);

        JButton keuanganButton = new JButton("Menu Data Keuangan");
        // do dispose and then open new MenuKeuangan
        keuanganButton.addActionListener(e -> {
            dispose();
            new MenuKeuangan(this.username, this.userRole);
        });
        panel.add(keuanganButton);

        JButton registerUserButton = new JButton("Register Bendahara");
        registerUserButton.addActionListener(e -> {
            dispose();
            new RegisterForm();
        });
        panel.add(registerUserButton);
    }

    private void addBendaharaMenu(JPanel panel) {
        JButton anggotaButton = new JButton("Menu Anggota");
        anggotaButton.addActionListener(e -> {
            dispose();
            new MenuAnggota();
        });
        panel.add(anggotaButton);

        JButton eksternalButton = new JButton("Menu Eksternal");
        eksternalButton.addActionListener(e -> {
            dispose();
            new MenuEksternal(this.username, this.userRole);
        });
        panel.add(eksternalButton);

        JButton keuanganButton = new JButton("Menu Data Keuangan");
        keuanganButton.addActionListener(e -> {
            dispose();
            new MenuKeuangan(this.username, this.userRole);
        });
        panel.add(keuanganButton);
    }

    private void loadTotalDana() {
        double totalDana = 0;

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Hitung pemasukan
            String queryPemasukan = "SELECT SUM(jumlah) AS total FROM keuangan WHERE jenis_transaksi = 'pemasukan'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryPemasukan);
            if (resultSet.next()) {
                totalDana += resultSet.getDouble("total");
            }

            // Hitung pengeluaran
            String queryPengeluaran = "SELECT SUM(jumlah) AS total FROM keuangan WHERE jenis_transaksi = 'pengeluaran'";
            resultSet = statement.executeQuery(queryPengeluaran);
            if (resultSet.next()) {
                totalDana -= resultSet.getDouble("total");
            }

            // Tampilkan total dana
            totalDanaLabel.setText("Total Dana Saat Ini: Rp " + String.format("%,.2f", totalDana));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading total dana!");
        }
    }

    private void logout() {
        dispose();  // Tutup Main Menu
        new LoginForm();  // Kembali ke Login Form
    }
}

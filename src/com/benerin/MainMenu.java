package com.benerin;

import com.benerin.DatabaseConnection.DatabaseConnection;

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
        setSize(950, 500); // Adjusted window size to match LoginForm
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // Panel atas untuk selamat datang dan total dana
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JLabel welcomeLabel = new JLabel("Selamat Datang, " + this.username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
        totalDanaLabel = new JLabel("Total Dana Saat Ini: Rp 0", JLabel.CENTER);
        totalDanaLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        topPanel.add(welcomeLabel);
        topPanel.add(totalDanaLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Panel menu di tengah
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 20, 20)); // Adjusted grid layout for spacing
        addMenuItems(menuPanel);
        mainPanel.add(menuPanel, BorderLayout.CENTER);

        // Tombol Logout di bawah
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Increased font size
        logoutButton.setPreferredSize(new Dimension(150, 50)); // Increased button size
        logoutButton.addActionListener(e -> logout());
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.add(logoutButton);
        mainPanel.add(logoutPanel, BorderLayout.SOUTH);

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
        JButton anggotaButton = createMenuButton("Menu Anggota");
        anggotaButton.addActionListener(e -> {
            dispose();
            new MenuAnggota(this.username, this.userRole);
        });
        panel.add(anggotaButton);

        JButton eksternalButton = createMenuButton("Menu Eksternal");
        eksternalButton.addActionListener(e -> {
            dispose();
            new MenuEksternal(this.username, this.userRole);
        });
        panel.add(eksternalButton);

        JButton keuanganButton = createMenuButton("Menu Data Keuangan");
        keuanganButton.addActionListener(e -> {
            dispose();
            new MenuKeuangan(this.username, this.userRole);
        });
        panel.add(keuanganButton);

        JButton registerUserButton = createMenuButton("Register Bendahara");
        registerUserButton.addActionListener(e -> {
            dispose();
            new MenuBendahara(this.username, this.userRole);
        });
        panel.add(registerUserButton);
    }

    private void addBendaharaMenu(JPanel panel) {
        JButton anggotaButton = createMenuButton("Menu Anggota");
        anggotaButton.addActionListener(e -> {
            dispose();
            new MenuAnggota(this.username, this.userRole);
        });
        panel.add(anggotaButton);

        JButton eksternalButton = createMenuButton("Menu Eksternal");
        eksternalButton.addActionListener(e -> {
            dispose();
            new MenuEksternal(this.username, this.userRole);
        });
        panel.add(eksternalButton);

        JButton keuanganButton = createMenuButton("Menu Data Keuangan");
        keuanganButton.addActionListener(e -> {
            dispose();
            new MenuKeuangan(this.username, this.userRole);
        });
        panel.add(keuanganButton);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18)); // Increased font size
        button.setPreferredSize(new Dimension(200, 50)); // Increased button size
        return button;
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

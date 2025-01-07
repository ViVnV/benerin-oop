package com.benerin.models;

public class Keuangan {
    private int id;
    private String sumber_dana;
    private String keterangan;
    private double jumlah;
    private String jenis_transaksi;
    private String tanggal;
    private int anggota_id;
    private int eksternal_id;

    public Keuangan(String sumber_dana, String keterangan, double jumlah, String jenis_transaksi, String tanggal, int anggota_id, int eksternal_id) {
        this.sumber_dana = sumber_dana;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.jenis_transaksi = jenis_transaksi;
        this.tanggal = tanggal;
        this.anggota_id = anggota_id;
        this.eksternal_id = eksternal_id;
    }

    public Keuangan(int id, String sumber_dana, String keterangan, double jumlah, String jenis_transaksi, String tanggal, int anggota_id, int eksternal_id) {
        this.id = id;
        this.sumber_dana = sumber_dana;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.jenis_transaksi = jenis_transaksi;
        this.tanggal = tanggal;
        this.anggota_id = anggota_id;
        this.eksternal_id = eksternal_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSumberDana() {
        return sumber_dana;
    }

    public void setSumberDana(String sumber_dana) {
        this.sumber_dana = sumber_dana;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public String getJenisTransaksi() {
        return jenis_transaksi;
    }

    public void setJenisTransaksi(String jenis_transaksi) {
        this.jenis_transaksi = jenis_transaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getAnggotaId() {
        return anggota_id;
    }

    public void setAnggotaId(int anggota_id) {
        this.anggota_id = anggota_id;
    }

    public int getEksternalId() {
        return eksternal_id;
    }

    public void setEksternalId(int eksternal_id) {
        this.eksternal_id = eksternal_id;
    }
}

package com.benerin.models;

public class Anggota {
    private int id;
    private String nama;
    private String jabatan;

    // Konstruktor untuk data baru (tanpa ID)
    public Anggota(String nama, String jabatan) {
        this.nama = nama;
        this.jabatan = jabatan;
    }

    // Konstruktor dengan ID (misalnya saat mengupdate data)
    public Anggota(int id, String nama, String jabatan) {
        this.id = id;
        this.nama = nama;
        this.jabatan = jabatan;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }
}

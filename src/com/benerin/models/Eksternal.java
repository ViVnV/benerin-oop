package com.benerin.models;

public class Eksternal {
    private int id;
    private String nama;
    private String jenis;

    public Eksternal(String nama, String jenis) {
        this.nama = nama;
        this.jenis = jenis;
    }

    public Eksternal(int id, String nama, String jenis) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
    }

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

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}

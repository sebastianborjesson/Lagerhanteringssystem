package com.sample;


public class Produkt {

  private long artikelNummer;
  private String artikelNamn;
  private long antal;
  private String lagerPlats;
  private long kategoriId;


  public long getArtikelNummer() {
    return artikelNummer;
  }

  public void setArtikelNummer(long artikelNummer) {
    this.artikelNummer = artikelNummer;
  }


  public String getArtikelNamn() {
    return artikelNamn;
  }

  public void setArtikelNamn(String artikelNamn) {
    this.artikelNamn = artikelNamn;
  }


  public long getAntal() {
    return antal;
  }

  public void setAntal(long antal) {
    this.antal = antal;
  }


  public String getLagerPlats() {
    return lagerPlats;
  }

  public void setLagerPlats(String lagerPlats) {
    this.lagerPlats = lagerPlats;
  }


  public long getKategoriId() {
    return kategoriId;
  }

  public void setKategoriId(long kategoriId) {
    this.kategoriId = kategoriId;
  }

}


public class Produkt {

  private int artikelNummer;
  private String artikelNamn;
  private int antal;
  private String lagerPlats;
  private int kategoriId;

  public Produkt(int artikelNummer, String artikelNamn, int antal) {
    this.artikelNummer = artikelNummer;
    this.artikelNamn = artikelNamn;
    this.antal = antal;
  }
  public Produkt(int artikelNummer, String artikelNamn, int antal, String lagerPlats) {
    this.artikelNummer = artikelNummer;
    this.artikelNamn = artikelNamn;
    this.antal = antal;
    this.lagerPlats = lagerPlats;
  }
  public Produkt() {

  }

  public int getArtikelNummer() {
    return artikelNummer;
  }

  public void setArtikelNummer(int artikelNummer) {
    this.artikelNummer = artikelNummer;
  }


  public String getArtikelNamn() {
    return artikelNamn;
  }

  public void setArtikelNamn(String artikelNamn) {
    this.artikelNamn = artikelNamn;
  }


  public int getAntal() {
    return antal;
  }

  public void setAntal(int antal) {
    this.antal = antal;
  }


  public String getLagerPlats() {
    return lagerPlats;
  }

  public void setLagerPlats(String lagerPlats) {
    this.lagerPlats = lagerPlats;
  }


  public int getKategoriId() {
    return kategoriId;
  }

  public void setKategoriId(int kategoriId) {
    this.kategoriId = kategoriId;
  }

}

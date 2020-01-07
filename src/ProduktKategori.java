
public class ProduktKategori {

  private int artikelNummer;
  private String artikelNamn;
  private int antal;
  private String kategori;

  public ProduktKategori() {
  }

  public ProduktKategori(int artikelNummer, String artikelNamn, int antal, String kategori) {
    this.artikelNummer = artikelNummer;
    this.artikelNamn = artikelNamn;
    this.antal = antal;
    this.kategori = kategori;
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


  public String getKategori() {
    return kategori;
  }

  public void setKategori(String kategori) {
    this.kategori = kategori;
  }

}

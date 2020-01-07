
public class ProduktKategori {

  private long artikelNummer;
  private String artikelNamn;
  private long antal;
  private String kategori;

  public ProduktKategori() {
  }

  public ProduktKategori(long artikelNummer, String artikelNamn, long antal, String kategori) {
    this.artikelNummer = artikelNummer;
    this.artikelNamn = artikelNamn;
    this.antal = antal;
    this.kategori = kategori;
  }

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


  public String getKategori() {
    return kategori;
  }

  public void setKategori(String kategori) {
    this.kategori = kategori;
  }

}


public class Kategori extends Produkt {

  private int id;
  private String namn;

  public Kategori(String namn) {
    super();
    this.namn = namn;
  }

  public Kategori(int id, String namn) {
    this.id = id;
    this.namn = namn;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getNamn() {
    return namn;
  }

  public void setNamn(String namn) {
    this.namn = namn;
  }

}

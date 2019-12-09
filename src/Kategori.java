
public class Kategori extends Produkt {

  private long id;
  private String namn;

  public Kategori(String namn) {
    super();
    this.namn = namn;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getNamn() {
    return namn;
  }

  public void setNamn(String namn) {
    this.namn = namn;
  }

}

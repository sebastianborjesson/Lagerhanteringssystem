
public class LagerPlats {

  private String namn;
  private int tillganglighet;

  public LagerPlats(String namn, int tillganglighet) {
    this.namn = namn;
    this.tillganglighet = tillganglighet;
  }
  public String getNamn() {
    return namn;
  }

  public void setNamn(String namn) {
    this.namn = namn;
  }

  public int getTillganglighet() {
    return tillganglighet;
  }

  public void setTillganglighet(int tillganglighet) {
    this.tillganglighet = tillganglighet;
  }

}

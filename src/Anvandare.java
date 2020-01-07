
public class Anvandare {

  private int anstallningsId;
  private String namn;
  private String roll;
  private String losenord;


  public Anvandare() {
  }

  public Anvandare(String namn, String losenord) {
    this.namn = namn;
    this.losenord = losenord;
  }



  public int getAnstallningsId() {
    return anstallningsId;
  }

  public void setAnstallningsId(int anstallningsId) {
    this.anstallningsId = anstallningsId;
  }

  public String getNamn() {
    return namn;
  }

  public void setNamn(String namn) {
    this.namn = namn;
  }


  public String getRoll() {
    return roll;
  }

  public void setRoll(String roll) {
    this.roll = roll;
  }


  public String getLosenord() {
    return losenord;
  }

  public void setLosenord(String losenord) {
    this.losenord = losenord;
  }

}

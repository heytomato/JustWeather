package sz.heytomato.justweatherforecast;

/**
 * Created by Administrator on 2016/5/17.
 */
public class WeatherDaily {

    private String wDay="";
    private String wText="";
    private String wTempL ="";
    private String wTempH ="";
    private int wPidId=0;



    public WeatherDaily(String wDay,String wText,String wTempL,String wTempH,int wPidId){
        this.wDay=wDay;
        this.wText=wText;
        this.wTempL =wTempL;
        this.wTempH =wTempH;
        this.wPidId=wPidId;

    }

    public String getwDay() {
        return wDay;
    }

    public void setwDay(String wDay) {
        this.wDay = wDay;
    }

    public String getwTempL() {
        return wTempL;
    }

    public void setwTempL(String wTempL) {
        this.wTempL = wTempL;
    }

    public String getwTempH() {
        return wTempH;
    }

    public void setwTempH(String wTempH) {
        this.wTempH = wTempH;
    }

    public int getwPidId() {
        return wPidId;
    }

    public void setwPidId(int wPidId) {
        this.wPidId = wPidId;
    }


    public String getwText() {
        return wText;
    }

    public void setwText(String wText) {
        this.wText = wText;
    }
}

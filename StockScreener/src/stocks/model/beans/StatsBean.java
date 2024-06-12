package stocks.model.beans;

import java.util.Calendar;

public class StatsBean {
    private double ema26,ema16;
    private Calendar dateObj;

    public double getEma26() {
        return ema26;
    }

    public void setEma26(double ema26) {
        this.ema26 = ema26;
    }

    public Calendar getDateObj() {
        return dateObj;
    }

    public void setDateObj(Calendar dateObj) {
        this.dateObj = dateObj;
    }

    public double getEma16() {
        return ema16;
    }

    public void setEma16(double ema16) {
        this.ema16 = ema16;
    }
}

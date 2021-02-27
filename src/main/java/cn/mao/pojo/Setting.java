package cn.mao.pojo;

import java.util.Date;

public class Setting {
    private Integer id;

    private Integer temp;

    private Integer humi;

    private Integer light;

    private Date timeon;

    private Date timeoff;

    private String smart;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getHumi() {
        return humi;
    }

    public void setHumi(Integer humi) {
        this.humi = humi;
    }

    public Integer getLight() {
        return light;
    }

    public void setLight(Integer light) {
        this.light = light;
    }

    public Date getTimeon() {
        return timeon;
    }

    public void setTimeon(Date timeon) {
        this.timeon = timeon;
    }

    public Date getTimeoff() {
        return timeoff;
    }

    public void setTimeoff(Date timeoff) {
        this.timeoff = timeoff;
    }

    public String getSmart() {
        return smart;
    }

    public void setSmart(String smart) {
        this.smart = smart == null ? null : smart.trim();
    }
}
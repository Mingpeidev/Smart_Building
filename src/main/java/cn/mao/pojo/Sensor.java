package cn.mao.pojo;

import java.util.Date;

public class Sensor {
	private Integer id;

	private String temp;

	private String humi;

	private String light;

	private Date time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp == null ? null : temp.trim();
	}

	public String getHumi() {
		return humi;
	}

	public void setHumi(String humi) {
		this.humi = humi == null ? null : humi.trim();
	}

	public String getLight() {
		return light;
	}

	public void setLight(String light) {
		this.light = light == null ? null : light.trim();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
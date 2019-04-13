package cn.mao.pojo;

import java.util.Date;

public class Realtimedata {
	private Integer id;

	private String human;

	private String smoke;

	private String lamp;

	private String air;

	private String alarm;

	private String door;

	private Date time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHuman() {
		return human;
	}

	public void setHuman(String human) {
		this.human = human == null ? null : human.trim();
	}

	public String getSmoke() {
		return smoke;
	}

	public void setSmoke(String smoke) {
		this.smoke = smoke == null ? null : smoke.trim();
	}

	public String getLamp() {
		return lamp;
	}

	public void setLamp(String lamp) {
		this.lamp = lamp == null ? null : lamp.trim();
	}

	public String getAir() {
		return air;
	}

	public void setAir(String air) {
		this.air = air == null ? null : air.trim();
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm == null ? null : alarm.trim();
	}

	public String getDoor() {
		return door;
	}

	public void setDoor(String door) {
		this.door = door == null ? null : door.trim();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
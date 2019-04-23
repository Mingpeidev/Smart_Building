package cn.mao.pojo;

import java.util.Date;

public class Alarm {
	private Integer id;

	private String human;

	private String smoke;

	private String state;

	private Date date;

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
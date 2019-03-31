package cn.mao.service;

import java.util.List;

import cn.mao.pojo.Sensor;

public interface SensorService {
	public List<Sensor> getSensorAll();

	public List<Sensor> getSensorByPage(int page, int limit);
}

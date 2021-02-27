package cn.mao.service.impl;

import cn.mao.dao.SensorMapper;
import cn.mao.pojo.Sensor;
import cn.mao.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

    @Autowired
    private SensorMapper sensorMapper;

    @Override
    public List<Sensor> getSensorAll() {
        List<Sensor> sensor = sensorMapper.selectSensorAll();

        return sensor;
    }

    @Override
    public List<Sensor> getSensorByPage(int page, int limit) {
        List<Sensor> sensorpage = sensorMapper.selectSensorByPage(page, limit);

        return sensorpage;
    }

    @Override
    public void insertSensor(Sensor sensor) {
        sensorMapper.insert(sensor);
    }

}

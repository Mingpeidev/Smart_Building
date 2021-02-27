package cn.mao.service.impl;

import cn.mao.dao.TripRecordMapper;
import cn.mao.pojo.TripRecord;
import cn.mao.service.TripRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripRecordServiceImpl implements TripRecordService {

    @Autowired
    private TripRecordMapper tripRecordMapper;

    @Override
    public List<TripRecord> getTripRecordAll() {
        List<TripRecord> tripRecords = tripRecordMapper.selectTripRecordAll();
        return tripRecords;
    }

    @Override
    public List<TripRecord> getTripRecordByPage(int page, int limit) {
        List<TripRecord> tripRecords = tripRecordMapper.selectTripRecordByPage(page, limit);
        return tripRecords;
    }

    @Override
    public List<TripRecord> getTripRecordAllByName(String residentName) {
        List<TripRecord> tripRecords = tripRecordMapper.selectTripRecordAllByName(residentName);
        return tripRecords;
    }

    @Override
    public void addTripRecord(TripRecord tripRecord) {
        tripRecordMapper.insert(tripRecord);
    }

    @Override
    public TripRecord getTripRecordByDoorId(String doorId) {
        TripRecord tripRecord = tripRecordMapper.selectByDoorId(doorId);
        return tripRecord;
    }

}

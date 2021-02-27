package cn.mao.service.impl;

import cn.mao.dao.TriprecordMapper;
import cn.mao.pojo.Triprecord;
import cn.mao.service.TriprecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TriprecordServiceImpl implements TriprecordService {

    @Autowired
    private TriprecordMapper triprecordMapper;

    @Override
    public List<Triprecord> getTriprecordAll() {
        List<Triprecord> triprecords = triprecordMapper.selectTriprecordAll();
        return triprecords;
    }

    @Override
    public List<Triprecord> getTriprecordByPage(int page, int limit) {
        List<Triprecord> triprecords = triprecordMapper.selectTriprecordByPage(page, limit);
        return triprecords;
    }

    @Override
    public List<Triprecord> getTriprecordAllByname(String residentname) {
        List<Triprecord> triprecords = triprecordMapper.selectTriprecordAllByname(residentname);
        return triprecords;
    }

    @Override
    public void addTriprecord(Triprecord triprecord) {

        triprecordMapper.insert(triprecord);
    }

    @Override
    public Triprecord getTriprecordByDoorid(String doorid) {
        Triprecord triprecord = triprecordMapper.selectByDoorid(doorid);
        return triprecord;
    }

}

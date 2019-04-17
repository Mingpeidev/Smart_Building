package cn.mao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mao.dao.TriprecordMapper;
import cn.mao.pojo.Triprecord;
import cn.mao.service.TriprecordService;

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

}

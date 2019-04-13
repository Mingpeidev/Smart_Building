package cn.mao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mao.dao.RealtimedataMapper;
import cn.mao.pojo.Realtimedata;
import cn.mao.service.RealtimedataService;

@Service
public class RealtimedataServiceImpl implements RealtimedataService{
	
	@Autowired
	private RealtimedataMapper realtimedataMapper;

	@Override
	public List<Realtimedata> getRealtimedatas() {
		List<Realtimedata> realtimedatas=(List<Realtimedata>) realtimedataMapper.selectByPrimaryKey(1);
		return realtimedatas;
	}

}

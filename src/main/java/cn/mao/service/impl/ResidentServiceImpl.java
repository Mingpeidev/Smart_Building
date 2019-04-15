package cn.mao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mao.dao.ResidentMapper;
import cn.mao.pojo.Resident;
import cn.mao.service.ResidentService;

@Service
public class ResidentServiceImpl implements ResidentService {

	@Autowired
	private ResidentMapper residentMapper;

	@Override
	public void addResident(Resident resident) {

		residentMapper.insert(resident);
	}

}

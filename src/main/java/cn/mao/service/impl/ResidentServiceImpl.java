package cn.mao.service.impl;

import java.util.List;

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

	@Override
	public List<Resident> getResidentAll() {

		List<Resident> resident = residentMapper.selectResidentAll();

		return resident;
	}

	@Override
	public void editResidentInfo(Resident resident) {
		residentMapper.updateByPrimaryKey(resident);

	}

	@Override
	public void deleteResidentInfo(Integer id) {
		residentMapper.deleteByPrimaryKey(id);

	}

	@Override
	public List<Resident> getResidentByPage(int page, int limit) {
		List<Resident> residents = residentMapper.selectResidentByPage(page, limit);

		return residents;
	}

	@Override
	public Resident getResidentByDoorid(String doorid) {
		Resident resident = residentMapper.selectByDoorid(doorid);
		return resident;
	}

}

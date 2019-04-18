package cn.mao.service;

import java.util.List;

import cn.mao.pojo.Resident;

public interface ResidentService {

	/**
	 * 添加居民信息
	 */
	void addResident(Resident resident);

	/**
	 * 获取所有居民信息
	 * 
	 * @return
	 */
	public List<Resident> getResidentAll();

	/**
	 * 分页显示在表格
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<Resident> getResidentByPage(int page, int limit);

	/**
	 * 修改居民信息
	 * 
	 * @param resident
	 */
	void editResidentInfo(Resident resident);

	/**
	 * 删除居民信息
	 * 
	 * @param id
	 */
	void deleteResidentInfo(Integer id);

	public Resident getResidentByDoorid(String doorid);

}

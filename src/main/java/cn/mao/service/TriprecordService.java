package cn.mao.service;

import java.util.List;

import cn.mao.pojo.Triprecord;

public interface TriprecordService {
	/**
	 * 获取所有居民出行信息
	 * 
	 * @return
	 */
	public List<Triprecord> getTriprecordAll();

	/**
	 * 分页显示在表格
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<Triprecord> getTriprecordByPage(int page, int limit);

}
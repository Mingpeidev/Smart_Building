package cn.mao.service;

import java.util.List;

import cn.mao.pojo.Realtimedata;

public interface RealtimedataService {

	public List<Realtimedata> getRealtimedataAll();

	public void updateRealtimedata(Realtimedata realtimedata);
}
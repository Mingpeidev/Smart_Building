package cn.mao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mao.dao.SettingMapper;
import cn.mao.pojo.Setting;
import cn.mao.service.SettingService;

@Service
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingMapper settingMapper;

	@Override
	public void updateSetting(Setting setting) {
		settingMapper.updateByPrimaryKey(setting);

	}

	@Override
	public Setting selectSetting(Integer id) {
		Setting setting = settingMapper.selectByPrimaryKey(id);
		return setting;
	}

}

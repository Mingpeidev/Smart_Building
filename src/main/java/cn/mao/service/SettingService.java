package cn.mao.service;

import cn.mao.pojo.Setting;

public interface SettingService {
    /**
     * 更新设置表
     *
     * @param setting
     */
    public void updateSetting(Setting setting);

    /**
     * 查询设置信息
     *
     * @param id
     * @return
     */
    public Setting selectSetting(Integer id);
}

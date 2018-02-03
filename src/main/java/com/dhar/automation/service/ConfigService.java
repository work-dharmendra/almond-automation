package com.dhar.automation.service;

import com.dhar.automation.common.Constants;
import com.dhar.automation.dao.ConfigurationDao;
import com.dhar.automation.dto.SettingsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Dharmendra Chouhan
 */
@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service
public class ConfigService {

    @Resource
    private ConfigurationDao configurationDao;

    public SettingsDTO getSettings(){
        Map<String, String> configMap = configurationDao.getConfigurations();

        SettingsDTO settingsDTO = new SettingsDTO();
        settingsDTO.gridUrl = configMap.get(Constants.GRID_URL_KEY);

        return settingsDTO;
    }

    public SettingsDTO saveSettings(SettingsDTO settingsDTO) {
        configurationDao.updateConfig(Constants.GRID_URL_KEY, settingsDTO.gridUrl);
        return settingsDTO;
    }

    public void addConfig(String name, String value) {
        configurationDao.addConfig(name, value);

    }
}

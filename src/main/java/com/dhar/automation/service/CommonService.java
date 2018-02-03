package com.dhar.automation.service;

import com.dhar.automation.dao.ConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Dharmendra.Singh on 4/17/2015.
 */
@Service
public class CommonService {

    @Autowired
    ConfigurationDao configurationDao;

    public Map<String,String> getConfigurations(){
        return configurationDao.getConfigurations();
    }
}

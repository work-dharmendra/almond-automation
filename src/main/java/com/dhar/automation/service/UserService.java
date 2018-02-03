package com.dhar.automation.service;

import com.dhar.automation.common.HibernateDetachUtility;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.dao.UserDao;
import com.dhar.automation.domain.User;
import com.dhar.automation.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Dharmendra.Singh on 4/17/2015.
 */
@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public List<User> findAllUsers(){
        return userDao.findAllUsers();
    }

    public UserDTO createUser(UserDTO dto){
        dto.id = null;
        User user = MapEntityDto.buildUser(dto);

        user = userDao.createUser(user);

        HibernateDetachUtility.deProxy(user);

        return MapEntityDto.buildUserDTO(user);
    }

    public UserDTO updateUser(UserDTO dto){
        User user = MapEntityDto.buildUser(dto);

        userDao.updateUser(user);

        HibernateDetachUtility.deProxy(user);

        return MapEntityDto.buildUserDTO(user);
    }

    public UserDTO findUser(Long id){
        User user = userDao.findUser(id);
        HibernateDetachUtility.deProxy(user);

        return MapEntityDto.buildUserDTO(user);
    }
}

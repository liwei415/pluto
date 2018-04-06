package xyz.inux.pluto.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.inux.pluto.domain.dao.UsersDao;
import xyz.inux.pluto.domain.dao.po.UsersPo;

@Service("SampleRepository")
public class SampleRepository {

    @Autowired
    private UsersDao usersDao;

    public String getUserById(String id) {
        UsersPo usersPo = usersDao.queryById(id);
//        return ClazzConverter.converterClass(demoPo, DemoModel.class);
        return usersPo.getuMobile();
    }
}

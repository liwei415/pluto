package xyz.inux.pluto.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.inux.pluto.domain.dao.mysql.UsersDao;
import xyz.inux.pluto.model.pojo.po.UsersPo;

@Service("SampleRepository")
public class SampleRepository {

    @Autowired
    private UsersDao usersDao;

    public String getUserById(String id) {
        UsersPo usersPo = usersDao.queryById(id);
//        return ClazzConverter.converterClass(demoPo, DemoModel.class);
        return usersPo.getuMobile();
    }

    public String editUserById(String id) {
        usersDao.editById(id);

        if (1 == 1) {
            throw new NullPointerException();
        }

        return "OK";
    }


}

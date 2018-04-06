package xyz.inux.pluto.domain.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.springframework.stereotype.Service;
import xyz.inux.pluto.domain.dao.po.UsersPo;

@Service(value = "UserDao")
@Mapper
public interface UsersDao {

    @Select("SELECT * FROM users WHERE u_id = #{id}")
    UsersPo queryById(@Param("id") String id);
}
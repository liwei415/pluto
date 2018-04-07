package xyz.inux.pluto.domain.dao.mysql;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;
import xyz.inux.pluto.model.pojo.po.UsersPo;

@Service(value = "UsersDao")
@Mapper
public interface UsersDao {

    @Select("SELECT * FROM users WHERE u_id = #{id}")
    UsersPo queryById(@Param("id") String id);

    @Update("UPDATE users SET u_mobile = '11111111' WHERE u_id = #{id}")
    void editById(@Param("id") String id);
}
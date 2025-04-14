package com.example.springboot.mapper;

import com.example.springboot.entity.Trace;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TraceMapper {

    @Insert("insert into `trace` (id, type, amount, date) values (#{id}, #{type}, #{amount}, #{date})")
    void insert(Trace trace);

    @Select("select * from `trace` where id = #{id} order by id, date desc")
    List<Trace> selectByTraceId(Integer id);

    @Select("select sum(case " +
            "when type = '活期存款' or type = '定期存款' then amount " +
            "when type = '活期取款' or type = '定期取款' then -amount " +
            "else 0 end) " +
            "from `trace` where id = #{id}")
    double selectMoney(Integer id);


}

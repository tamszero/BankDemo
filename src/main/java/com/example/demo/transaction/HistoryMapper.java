package com.example.demo.transaction;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryMapper {
    int insert(History history);
    List<History> findByAccountId(@Param("accountId") Long accountId);
}

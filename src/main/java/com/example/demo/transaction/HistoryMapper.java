package com.example.demo.transaction;

import com.example.demo.account.dto.HistoryView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HistoryMapper {
    int insert(History history);
    List<HistoryView> findByAccountId(@Param("accountId") Long accountId,
                                      @Param("offset") int offset,
                                      @Param("size") int size);
}

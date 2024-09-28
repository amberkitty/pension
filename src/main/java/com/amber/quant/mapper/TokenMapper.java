package com.amber.quant.mapper;

import com.amber.quant.model.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface TokenMapper {

    @Select("SELECT * FROM token")
    List<Token> getTokenList();
}

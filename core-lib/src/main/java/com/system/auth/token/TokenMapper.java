package com.system.auth.token;

import com.system.auth.domain.Token;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TokenMapper {
    public Optional<Token> SELECT_TOKEN(String token);
    int INSERT_TOKEN(Token token);
    int UPDATE_TOKEN(Token token);

    List<Token> SELECT_ALL_TOKEN(Token request);
}

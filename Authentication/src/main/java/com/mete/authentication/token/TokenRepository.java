package com.mete.authentication.token;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token,Integer> {
    Optional<Token> findTokenByToken(String token);
    long removeTokenByRevoked(boolean bool);

    @Query(value="""
            select t from Token t where t.user.userId=:userId and t.revoked=false
            """, nativeQuery = false)
    List<Token> getActiveTokens(@Param("userId") String userId);

    @Query(value = """
            select t from Token t where t.revoked=true
            """, nativeQuery = false)
    List<Token> getRevokedTokens();
}

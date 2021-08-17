package com.marvin.apiuserregisteremailsender.repository;

import com.marvin.apiuserregisteremailsender.domain.AppUser;
import com.marvin.apiuserregisteremailsender.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findAppUserByUserName(String userName);

    Optional<AppUser> findAppUserByToken(Token token);
}

package ru.vogulev.sofia_wb_tg_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vogulev.sofia_wb_tg_bot.entity.WbUser;

import java.util.List;

@Repository
public interface WbUserRepository extends JpaRepository<WbUser, Long> {

    List<WbUser> getWbUsersByState(String state);
}
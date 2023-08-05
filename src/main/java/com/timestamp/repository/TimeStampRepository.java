package com.timekeeping.repository;

import com.timekeeping.entity.TimeStamp;
import com.timekeeping.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeStampRepository extends JpaRepository<TimeStamp,Integer> {
    @Query("SELECT t FROM TimeStamp t WHERE t.isLoggedOut = false AND t.userInfo = :userInfo")
    TimeStamp findNotLoggedOutByUserInfo(@Param("userInfo") UserInfo userInfo);
}

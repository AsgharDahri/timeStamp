package com.timekeeping.service;

import com.timekeeping.dto.AuthRequest;
import com.timekeeping.dto.Response;
import com.timekeeping.entity.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String authenticateAndTimeStamp(AuthRequest authRequest);
    String clockIn(int userId);
    String addUser(UserInfo userInfo);

}

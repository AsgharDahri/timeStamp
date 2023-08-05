package com.timekeeping.ServiceImpl;
import com.timekeeping.dto.AuthRequest;
import com.timekeeping.entity.TimeStamp;
import com.timekeeping.entity.UserInfo;
import com.timekeeping.repository.TimeStampRepository;
import com.timekeeping.repository.UserInfoRepository;
import com.timekeeping.service.JwtService;
import com.timekeeping.service.UserService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime; // Import java.time.LocalTime
import java.util.*;

@Service
public class UserServicImpl implements UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TimeStampRepository timeStampRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public String authenticateAndTimeStamp(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String jwtToken =  jwtService.generateToken(authRequest.getUsername());
            if(!jwtToken.isEmpty()){
                return jwtToken;
            }else{
                return "Not Authenticated.";
            }
        }
        else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    public String clockIn(int userId) {
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);
        if(userInfo.isPresent()){
            TimeStamp userTimeStamp = timeStampRepository.findNotLoggedOutByUserInfo(userInfo.get());
            if (userTimeStamp==null) {
                    TimeStamp timeStamp = new TimeStamp();
                    timeStamp.setStartTime(LocalTime.now()); // Use LocalTime.now() for startTime
                    LocalDate currentDate = LocalDate.now();
                    timeStamp.setDate(currentDate.toString());
                    DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                    timeStamp.setDay(dayOfWeek.name());
                    timeStamp.setUserInfo(userInfo.get());
                    timeStampRepository.save(timeStamp);
                return "Clocked in";
            }
            return "You are already Clocked in";
        }

        return "User not found!";
    }
    private int generateNDigitNumber() {
        Set<Integer> existingLoginNumbers = new HashSet<>();
        List<UserInfo> userInfos = userInfoRepository.findAll();

        for (UserInfo userInfo : userInfos) {
            existingLoginNumbers.add(userInfo.getLoginNumber());
        }

        int n = 4;
        int min = (int) Math.pow(10, n - 1);
        int max = (int) Math.pow(10, n) - 1;
        Random random = new Random();
        int loginNumber;

        do {
            loginNumber = random.nextInt(max - min + 1) + min;
        } while (existingLoginNumbers.contains(loginNumber));

        return loginNumber;
    }
    public String addUser(UserInfo userInfo) {
        int loginNumber = generateNDigitNumber();
        userInfo.setLoginNumber(loginNumber);
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return "user added to system ";
    }
}

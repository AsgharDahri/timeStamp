
package com.timekeeping.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.time.LocalTime; // Import java.time.LocalTime

@Getter
@Setter
@Entity
public class TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean isUserForgot;
    private String date;
    private String day;
    private LocalTime startTime; // Use java.time.LocalTime for the startTime field
    private LocalTime exitTime; // Use java.time.LocalTime for the exitTime field
    private int difference;
    private boolean isLoggedOut;
    @ManyToOne
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;
}

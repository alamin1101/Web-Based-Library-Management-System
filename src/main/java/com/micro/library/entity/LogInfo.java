package com.micro.library.entity;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LogInfo {

    @Id
    @SequenceGenerator(name = "log_history", sequenceName = "log_history", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "")
    private int logId;
    private String user;
    private String loginTime;
    private String logoutTime;
    private int spendTime;



}

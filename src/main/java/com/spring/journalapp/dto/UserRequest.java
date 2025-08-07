package com.spring.journalapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String userName;

    private String password;

    private String email;

    private String sentimentAnalysis;

    private String city;
}

package com.spring.journalapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 8, max = 64, message = "password must be between 8 and 64 characters")
    private String password;

    @Email(message = "email is in wrong format")
    private String email;

    private String sentimentAnalysis;

    @Pattern(regexp = "^[\\w\\-.\\s]*$", message = "city contains invalid characters")
    private String city;
}

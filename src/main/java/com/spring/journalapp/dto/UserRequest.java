package com.spring.journalapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserRequest {

    @NotBlank(message = "username shouldn't be null or blank")
    @Pattern(regexp = "^[\\w\\-.\\s]+$", message = "username contains invalid characters")
    private String userName;

    @NotBlank(message = "password shouldn't be null or blank")
    @Size(min = 8, max = 64, message = "password must be between 8 and 64 characters")
    private String password;

    @NotBlank(message = "email shouldn't be null or blank")
    @Email(message = "email is in wrong format")
    private String email;

    private String sentimentAnalysis;

    @NotBlank(message = "city shouldn't be null or blank")
    @Pattern(regexp = "^[\\w\\-.\\s]+$", message = "city contains invalid characters")
    private String city;
}

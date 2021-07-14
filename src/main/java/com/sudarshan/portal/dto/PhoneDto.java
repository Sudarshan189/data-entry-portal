package com.sudarshan.portal.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class PhoneDto {
    @NotNull(message = "Phone should not be null")
    @NotEmpty(message = "Phone should not be null")
    private String phoneNumber;
    private String otp;
}

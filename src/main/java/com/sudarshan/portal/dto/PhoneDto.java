package com.sudarshan.portal.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created By Sudarshan Shanbhag
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class PhoneDto {
    @NotNull(message = "Phone should not be null")
    @NotEmpty(message = "Phone should not be null")
    @Size(min = 10, max = 10, message = "Phone number should be length of 10")
    private String phoneNumber;
    private String otp;
}

package com.sudarshan.portal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created By Sudarshan Shanbhag
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(exclude = {"otp"})
@NoArgsConstructor
public class PhoneDto {
    @NotNull(message = "Phone should not be null")
    @NotEmpty(message = "Phone should not be null")
    @Pattern(regexp="^[0-9]+$", message="Phone number must not be alpha-numeric")
    @Size(min = 10, max = 10, message = "Phone number should be length of 10")
    private String phoneNumber;
    private String otp;


    public PhoneDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

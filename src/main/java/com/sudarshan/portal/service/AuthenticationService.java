package com.sudarshan.portal.service;

import com.sudarshan.portal.dto.PhoneDto;

/**
 * Created By Sudarshan Shanbhag
 */
public interface AuthenticationService {

    PhoneDto generateOtp(PhoneDto phoneDto);

}

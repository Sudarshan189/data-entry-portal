package com.sudarshan.portal.service;

import com.sudarshan.portal.dto.PhoneDto;

public interface AuthenticationService {

    Boolean generateOtp(PhoneDto phoneDto);

}

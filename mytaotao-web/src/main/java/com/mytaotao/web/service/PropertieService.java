package com.mytaotao.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertieService {
    
    @Value("${MYTAOTAO_SSO_URL}")
    public String MYTAOTAO_SSO_URL;
    
    @Value("${MYTAOTAO_ORDER_URL}")
    public String MYTAOTAO_ORDER_URL;
}

package org.ares.app.demo.common.api.verify.impl;

import java.util.Map;

import org.ares.app.demo.common.api.verify.ApiVerify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("noIpVerify")
public class NoIpVerify implements ApiVerify {

	@Override
	public boolean verify(Map<String, ?> param) {
		return true;
	}
	
	@Value("${client.ip}")
	String client_ip;

}

package org.ares.app.demo.common.api.verify.impl;

import static org.ares.app.demo.common.cfg.Params.CLIENT_REQUEST_IP;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Map;

import org.ares.app.demo.common.api.verify.ApiVerify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("multiIpVerify")
public class MultiIpVerify implements ApiVerify {

	@Override
	public boolean verify(Map<String, ?> param) {
		boolean r=false;
		if(isEmpty(client_ip))
			return r;
		String request_ip=param.get(CLIENT_REQUEST_IP)+"";
		String[] ips=client_ip.split(",");
		int len=Math.min(MAX_COUNT, ips.length);
		for(int i=0;i<len;i++) {
			if(request_ip.equals(ips[i])) {
				r=true;
				break;
			}
		}
		return r;
	}
	
	@Value("${client.ip}")
	String client_ip;
	final int MAX_COUNT=3;
	
}

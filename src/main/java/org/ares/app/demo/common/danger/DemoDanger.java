package org.ares.app.demo.common.danger;

import java.time.LocalDate;

import org.ares.app.demo.common.dog.Dog1712;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DemoDanger {

	public boolean danger(){
		boolean r=false;
		if(check_dog&&!Dog1712.hasDog()) {
			log.info("no have dog!");
			return true;
		}
		if(LocalDate.parse(end_date).compareTo(LocalDate.now())<0)
			r=true;
		return r;
	}
	
	String end_date="2018-04-30"; //终止日期
	boolean check_dog=false;//false为不检测dog
	final Logger log = LoggerFactory.getLogger(getClass());
	
}

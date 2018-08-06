package org.ares.app.demo.common.adv;

import javax.annotation.Resource;

import org.ares.app.demo.common.danger.DemoDanger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DemoAdvice {
	
	@Around("execution (* org.ares.app.demo..*Service*.*(..))")
	public Object serviceExceptionHandle(ProceedingJoinPoint pjp) throws RuntimeException{
		Object r=null;
		try {
			if(dg.danger())
				return null;
			r=pjp.proceed();
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage());
		}
		return r;
	}
	
	@Resource DemoDanger dg;
}

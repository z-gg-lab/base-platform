
package com.hdos.platform.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hdos.platform.core.shiro.ApiStatelessRealm;

/**
 * 异常切面处理类
 * 
 * @author 黄洪波
 * @since 1.0
 * @version 2015-05-26 黄洪波
 */
@Service
public class ExceptionAspect {
    
	private final Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);
	
    /**
     * 异常拦截器
     * 
     * @param pjp 异常点
     * @return 异常
     * @throws Throwable 
     */
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Logger objLogger = LoggerFactory.getLogger(pjp.getTarget().getClass());
        
        // / 此处返回的是拦截的方法的返回值，如果不执行此方法，则不会执行被拦截的方法，利用环绕通知可以很好的做权限管理
        // long lBegin = System.currentTimeMillis();
        Object objResult = null;
        try {
            objResult = pjp.proceed();
        } catch (RuntimeException e) {
            objLogger.error("平台捕获到系统异常,进行日志输出定位", e);
            throw new RuntimeException(e);
            // Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	logger.info(e.getMessage());
		}
        // long lEnd = System.currentTimeMillis();
        // Object[] objLogOut = { pjp, Arrays.deepToString(pjp.getArgs()), lEnd - lBegin };
        return objResult;
    }
    
    /**
     * 异常处理
     * 
     * @param throwable 产生的异常
     */
    public void processException(Throwable throwable) {
    }
}

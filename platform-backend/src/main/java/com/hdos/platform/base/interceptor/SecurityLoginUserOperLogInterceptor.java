package com.hdos.platform.base.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdos.platform.base.filter.LoginContext;
import com.hdos.platform.base.filter.LoginUserInfo;
import com.hdos.platform.base.operationlog.model.UserLogConfigVO;
import com.hdos.platform.base.operationlog.service.UserActivityLogService;
import com.hdos.platform.base.user.model.LoginUserVO;
import com.hdos.platform.common.util.DateUtils;

/**
 * 安全登录用户的业务操作拦截器
 * @author chenyang
 *
 */
@Component
public class SecurityLoginUserOperLogInterceptor implements MethodInterceptor {

	private static final int MAX_LOGGER_THREAD = 4;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Runnable serviceInterceptor;
	private ExecutorService executorService;
	private Thread thread;
	
	@Autowired
	private UserActivityLogService userActivityLogService;

	@PostConstruct
	public void init() {
		LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>(1024);
		ThreadFactory threadFactory = new LoggingThreadFactory();
		executorService = new ThreadPoolExecutor(MAX_LOGGER_THREAD, MAX_LOGGER_THREAD, 0L, TimeUnit.MILLISECONDS,
		        blockingQueue, threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
		serviceInterceptor = DynamicControllerInterceptor.registerServiceInterceptor(this);

	}

	@PreDestroy
	public void destroy() {
		if (serviceInterceptor != null) {
			thread =new Thread(serviceInterceptor);
			thread.start();
//			serviceInterceptor.run();
			serviceInterceptor = null;
		}
		if (executorService != null) {
			executorService.shutdown();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		// 是否需要写正常日志
		Throwable executeThrowable = null;
		Object result = null;
		try {
			result = methodInvocation.proceed();
		} catch (Exception t) {
			executeThrowable = t;
			throw t;
		} finally {
			LoginUserVO loginUserVO = new LoginUserVO();
			LoginUserInfo loginUserInfo = LoginContext.getCurrentUser();
//			if(loginUserInfo == null) {
//				return result;
//			}
//			loginUserVO.setIp(loginUserInfo.getIp());
//			loginUserVO.setUserAccount(loginUserInfo.getUserAccount());
//			loginUserVO.setUserId(loginUserInfo.getUserId());
//			loginUserVO.setUserName(loginUserInfo.getUserName());
//			logging(loginUserVO, methodInvocation, result, executeThrowable);
			
			//20161109
			if(loginUserInfo != null) {
				loginUserVO.setIp(loginUserInfo.getIp());
				loginUserVO.setUserAccount(loginUserInfo.getUserAccount());
				loginUserVO.setUserId(loginUserInfo.getUserId());
				loginUserVO.setUserName(loginUserInfo.getUserName());
				logging(loginUserVO, methodInvocation, result, executeThrowable);
			}
			
		}
		return result;
	}

	protected void logging(LoginUserVO userVO, MethodInvocation methodInvocation, Object result,
	        Throwable throwable) {
		executorService.submit(new SafeTask(new LoggingTask(userVO, methodInvocation, result, throwable)));
	}

	/**
	 * 记录日志任务实现
	 * 
	 * @author matao
	 * @date 2016年6月18日
	 */
	class LoggingTask implements Runnable {

		private final String time = DateUtils.getNowDateTime();
		private LoginUserVO userVO;
		private MethodInvocation methodInvocation;
		private Object result;
		private Throwable throwable;

		/**
		 * 日志任务Task
		 * 
		 * @param loginUserInfo
		 * @param methodInvocation
		 * @param result
		 * @param throwable
		 */
		public LoggingTask(LoginUserVO userVO, MethodInvocation methodInvocation, Object result,
		        Throwable throwable) {
			this.userVO = userVO;
			this.methodInvocation = methodInvocation;
			this.result = result;
			this.throwable = throwable;
		}

		/** {@inheritDoc} */
		public void run() {
			// 执行方法函数
			final Method invokeMethod = methodInvocation.getMethod();

			// 构造执行函数的唯一标记
			final String invokeUUID = invokeMethod.getDeclaringClass().getName() + "." + invokeMethod.getName();

			// 获取活动日志记录对象
			if (userActivityLogService == null) {
				return;
			}

			// 提取活动日志模板
			UserLogConfigVO templateVO = userActivityLogService.findUserLogConfigVO(invokeUUID);
			if (templateVO == null) {
				return;
			}
			
			String detailContext = "";
			if(throwable == null) {
				detailContext = templateVO.getLogcfgSuccess();
			} else {
				detailContext = templateVO.getLogcfgFailed() + "【异常信息】：";
				detailContext += throwable.getMessage();
			}
			
			userActivityLogService.recordUserLog(userVO, templateVO.getLogcfgOper(), detailContext, throwable == null);
		}
	}

	class SafeTask implements Runnable {
		private Runnable unSafeTask;

		/**
		 * @param unSafeTask
		 */
		public SafeTask(Runnable unSafeTask) {
			this.unSafeTask = unSafeTask;
		}

		/** {@inheritDoc} */
		public void run() {
			if (unSafeTask != null) {
				try {
					thread = new Thread(unSafeTask);
					thread.start();
//					unSafeTask.run();
				} catch (Exception e) {
					logger.error("Logging thread run failed.", e);
				}
			}
		}
	}

	/** Logging Thread Factory */
	class LoggingThreadFactory implements ThreadFactory {
		private final ThreadGroup threadGroup = new ThreadGroup("PFormLog Thread Group");

		private final AtomicInteger atomicInteger = new AtomicInteger();

		/** {@inheritDoc} */
		public Thread newThread(Runnable runnable) {
			Thread t = new Thread(threadGroup, runnable, "Logging-Thread-" + atomicInteger.incrementAndGet());
			t.setDaemon(true);
			return t;
		}
	}

	/** Logging NoThread Factory */
	class LoggingNoThreadFactory implements ThreadFactory {
		/** {@inheritDoc} */
		public Thread newThread(Runnable runnable) {
			return null;
		}
	}

	class LinkedNoBlockingQueue<E> extends LinkedBlockingQueue<E> {
		/** serialVersionUID */
		private static final long serialVersionUID = 2011050733339449535L;

		/** {@inheritDoc} */
		public boolean offer(E e) {
			return false;
		}
	}
}

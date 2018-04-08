package xyz.inux.pluto.common.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceMonitorInterceptor implements MethodInterceptor {
	private final static Logger logger = LoggerFactory.getLogger(PerformanceMonitorInterceptor.class);

	private int threshold = 100; // 以毫秒表示的阈值

	public PerformanceMonitorInterceptor() {
		super();
	}
	
	/**
	 * 判断方法调用的时间是否超过阈值，如果是，则打印性能日志.
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String name = invocation.getMethod().getDeclaringClass().getName() + "." + invocation.getMethod().getName();
		
		Profiler.start("Invoking method: " + name);

		try {
			return invocation.proceed();
		} finally {
			Profiler.release();
			if (Profiler.isReleased()) {
				/*
				 * 这个判断，
				 * 保证了PerformanceMonitorInterceptor兼容了PerformanceInstrumentInterceptor的功能
				 */
				long elapseTime = Profiler.getDuration();
				if (elapseTime > threshold) {
					StringBuilder builder = new StringBuilder();
					builder.append(" ").append(name); // 方法
					builder.append(" ").append(threshold).append(" (ms)"); // 阈值
																			// (ms)
					builder.append(" ").append(elapseTime).append(" (ms)\r\n"); // 实际执行时间
																				// (ms)
					builder.append(Profiler.dump());
					logger.info(builder.toString());
				} else {
					if (logger.isDebugEnabled()) {
						StringBuilder builder = new StringBuilder();
						builder.append(" ").append(name); // 方法
						builder.append(" ").append(elapseTime).append(" (ms)\r\n"); // 实际执行时间为 (ms)
					}
				}
				
				Profiler.reset();
			}
		}
	}

	/**
	 * @return the threshold
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
}

package com.gmlsoftware.config.logger;

import org.apache.log4j.MDC;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;


public class MyPatternLayout extends PatternLayout {

	@Override
    public String format(LoggingEvent event) {
		Object threadIdObj = MDC.get("threadId");

		if (threadIdObj != null) {
			MDC.put("threadId", threadIdObj);			
		}

        return super.format(event);
    }

	@Override
	public String getHeader() {

		StringBuilder version = new StringBuilder();
		String separator = System.getProperty("line.separator");

		version.append(separator);
		version.append("----------------Version gmlsoftware customer management: 0.0.1");
		version.append(separator);
		version.append(separator);

		return version.toString();
	}
}

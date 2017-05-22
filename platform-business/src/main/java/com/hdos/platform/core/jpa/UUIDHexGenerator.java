package com.hdos.platform.core.jpa;

import java.io.Serializable;

/**
 * UUIDHexGenerator，from Hibernate
 * 
 * @author Arthur
 */
public class UUIDHexGenerator extends AbstractUUIDGenerator {

	private static String sep = "-";

	@Override
	public Serializable generate(Object obj) {
		// example: 402881e9-55f34a1b-0155-f34a1b63-0000
		return new StringBuilder(36).append(format(getIP())).append(sep).append(format(getJVM())).append(sep).append(format(getHiTime())).append(sep)
						.append(format(getLoTime())).append(sep).append(format(getCount())).toString();
	}

	protected String format(int intValue) {
		String formatted = Integer.toHexString(intValue);
		StringBuilder buf = new StringBuilder("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	protected String format(short shortValue) {
		String formatted = Integer.toHexString(shortValue);
		StringBuilder buf = new StringBuilder("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}
}

package com.jx.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {
	public static void info(String message) {
		Logger.getLogger("poly2tri").log(Level.INFO, message);
	}

	public static void info(String message, Object... args) {
		Logger.getLogger("poly2tri").log(Level.INFO, message, args);
	}

	public static void info(String message, Exception ex) {
		Logger.getLogger("poly2tri").log(Level.INFO, message, ex);
	}

	public static void warn(String message) {
		Logger.getLogger("poly2tri").log(Level.WARNING, message);
	}

	public static void warn(String message, Object... args) {
		Logger.getLogger("poly2tri").log(Level.WARNING, message, args);
	}

	public static void warn(String message, Throwable exception) {
		Logger.getLogger("poly2tri").log(Level.WARNING, message, exception);
	}

	public static void error(String message) {
		Logger.getLogger("poly2tri").log(Level.SEVERE, message);
	}
}

package org.javastack.agents;

import java.lang.instrument.Instrumentation;

import org.javastack.systemproperties.SystemClassLoaderProperties;

/**
 * 
 * Execute:
 * 
 * java -javaagent:systemclassloaderproperties.jar ...
 * 
 */
public class SystemClassLoaderPropertiesAgent {
	/**
	 * Load Java Agent on premain
	 * 
	 * @param agentArgs
	 * @param inst
	 * @throws Throwable
	 */
	public static void premain(final String agentArgs, final Instrumentation inst) throws Throwable {
		SystemClassLoaderProperties.getInstance().takeover();
	}

	/**
	 * Load Java Agent after JVM Statup
	 * 
	 * @param agentArgs
	 * @param inst
	 * @throws Throwable
	 */
	public static void agentmain(final String agentArgs, final Instrumentation inst) throws Throwable {
		SystemClassLoaderProperties.getInstance().takeover();
	}
}

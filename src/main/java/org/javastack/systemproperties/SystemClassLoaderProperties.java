package org.javastack.systemproperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SystemClassLoaderProperties extends Properties {
	private static final UUID PROP_ROOT_KEY = UUID.fromString("5afdfdcb-aaa2-47ca-8d2a-cba995c8055d");
	private static final UUID PROP_CLASSLOADER_KEY = UUID.fromString("7b5f3408-2bfa-407c-be87-4e539cfd2347");
	private static final long serialVersionUID = 42L;

	private static final Logger log = Logger.getLogger(SystemClassLoaderProperties.class.getName());
	private static final SystemClassLoaderProperties instance = new SystemClassLoaderProperties();
	private Properties origSystemProperties = null;

	public static final SystemClassLoaderProperties getInstance() {
		return instance;
	}

	private SystemClassLoaderProperties() {
		super(new Properties());
	}

	@SuppressWarnings("unchecked")
	private final Map<ClassLoader, Properties> getRootMap(final Properties prop) {
		return (Map<ClassLoader, Properties>) prop.get(PROP_ROOT_KEY);
	}

	private final ClassLoader getClassLoader() {
		// XXX SECURITY: This point is important
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null)
			classLoader = getClass().getClassLoader();
		if (classLoader == null)
			classLoader = ClassLoader.getSystemClassLoader();
		return classLoader;
	}

	private final Properties getLoaderProps(final Map<ClassLoader, Properties> rootMap,
			final ClassLoader classLoader) {
		if (classLoader == null) {
			return origSystemProperties;
		}
		Properties thisProp = rootMap.get(classLoader);
		if (thisProp == null) {
			final Properties parentProp = getLoaderProps(rootMap, classLoader.getParent());
			if (checkExcludedClasssLoader(classLoader)) {
				thisProp = parentProp;
			} else {
				// Copy On Access
				thisProp = (Properties) parentProp.clone();
				thisProp.remove(PROP_ROOT_KEY);
				thisProp.put(PROP_CLASSLOADER_KEY, String.valueOf(classLoader));
			}
			rootMap.put(classLoader, thisProp);
		}
		return thisProp;
	}

	private final boolean checkExcludedClasssLoader(final ClassLoader classLoader) {
		// final String name = classLoader.getClass().getName();
		// boolean skip = false;
		// // XXX SECURITY: This point is important
		// skip |= (classLoader.getParent() == null);
		// skip |= name.startsWith("sun.misc.Launcher$");
		// if (log.isLoggable(Level.FINE)) {
		// log.fine("checking: " + name + " parent=" + mapNull(classLoader.getParent(), "system") + " skip="
		// + skip);
		// }
		// return skip;
		return false;
	}

	private final Properties prop() {
		if (origSystemProperties == null) {
			if (Properties.class == System.getProperties().getClass()) {
				log.warning("Not initialized, fallback to SystemProperties");
				return System.getProperties();
			} else {
				log.warning("Not initialized, fallback to empty properties");
				if (!defaults.isEmpty()) {
					defaults.clear();
				}
				return defaults;
			}
		}
		final Properties prop = origSystemProperties; // System.getProperties();
		final Map<ClassLoader, Properties> rootMap = getRootMap(prop);
		return getLoaderProps(rootMap, getClassLoader());
	}

	@Override
	public synchronized Object setProperty(final String key, final String value) {
		return prop().setProperty(key, value);
	}

	@Override
	public synchronized String getProperty(final String key) {
		return prop().getProperty(key);
	}

	@Override
	public synchronized String getProperty(final String key, final String defaultValue) {
		return prop().getProperty(key, defaultValue);
	}

	@Override
	public synchronized Enumeration<?> propertyNames() {
		return prop().propertyNames();
	}

	@Override
	public synchronized Set<String> stringPropertyNames() {
		return prop().stringPropertyNames();
	}

	@Override
	public synchronized Enumeration<Object> elements() {
		return prop().elements();
	}

	@Override
	public synchronized Set<java.util.Map.Entry<Object, Object>> entrySet() {
		return prop().entrySet();
	}

	@Override
	public synchronized Collection<Object> values() {
		return prop().values();
	}

	@Override
	public synchronized void load(final Reader reader) throws IOException {
		prop().load(reader);
	}

	@Override
	public synchronized void load(final InputStream inStream) throws IOException {
		prop().load(inStream);
	}

	@Override
	@Deprecated
	public synchronized void save(final OutputStream out, final String comments) {
		prop().save(out, comments);
	}

	@Override
	public synchronized void store(final Writer writer, final String comments) throws IOException {
		prop().store(writer, comments);
	}

	@Override
	public synchronized void store(final OutputStream out, final String comments) throws IOException {
		prop().store(out, comments);
	}

	@Override
	public synchronized void loadFromXML(final InputStream in) throws IOException,
			InvalidPropertiesFormatException {
		prop().loadFromXML(in);
	}

	@Override
	public synchronized void storeToXML(final OutputStream os, final String comment) throws IOException {
		prop().storeToXML(os, comment);
	}

	@Override
	public synchronized void storeToXML(final OutputStream os, final String comment, final String encoding)
			throws IOException {
		prop().storeToXML(os, comment, encoding);
	}

	@Override
	public synchronized void list(final PrintStream out) {
		prop().list(out);
	}

	@Override
	public synchronized void list(final PrintWriter out) {
		prop().list(out);
	}

	@Override
	public synchronized Object remove(final Object key) {
		return prop().remove(key);
	}

	@Override
	public synchronized void clear() {
		prop().clear();
	}

	@Override
	public synchronized Object put(final Object key, final Object value) {
		return prop().put(key, value);
	}

	@Override
	public synchronized void putAll(final Map<? extends Object, ? extends Object> t) {
		prop().putAll(t);
	}

	@Override
	public synchronized int size() {
		return prop().size();
	}

	@Override
	public synchronized String toString() {
		return prop().toString();
	}

	@Override
	public synchronized boolean contains(final Object value) {
		return prop().contains(value);
	}

	@Override
	public synchronized boolean containsKey(final Object key) {
		return prop().containsKey(key);
	}

	@Override
	public synchronized boolean containsValue(final Object value) {
		return prop().containsValue(value);
	}

	@Override
	public synchronized boolean equals(final Object o) {
		return prop().equals(o);
	}

	@Override
	public synchronized Object get(final Object key) {
		return prop().get(key);
	}

	@Override
	public synchronized boolean isEmpty() {
		return prop().isEmpty();
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		return prop().keys();
	}

	@Override
	public synchronized Set<Object> keySet() {
		return prop().keySet();
	}

	@Override
	public synchronized Object clone() {
		return super.clone();
	}

	public synchronized void takeover() {
		synchronized (System.class) {
			if (origSystemProperties == null) {
				if (Properties.class != System.getProperties().getClass()) {
					// XXX SECURITY: This point is important
					System.setProperties(null);
				}
				final Properties props = System.getProperties();
				origSystemProperties = props;
				Map<ClassLoader, Properties> rootMap = getRootMap(props);
				if (rootMap == null) {
					rootMap = new WeakHashMap<ClassLoader, Properties>();
					props.put(PROP_ROOT_KEY, rootMap);
				}
				try {
					if (log.isLoggable(Level.FINE)) {
						log.fine("init:" + " ["
								+ mapNull(origSystemProperties.getClass().getClassLoader(), "system") + "] "
								+ origSystemProperties.getClass().getName() + "@"
								+ Integer.toHexString(System.identityHashCode(origSystemProperties)) + " ["
								+ mapNull(props.getClass().getClassLoader(), "system") + "] "
								+ props.getClass().getName() + "@"
								+ Integer.toHexString(System.identityHashCode(props)));
					}
				} catch (Throwable ign) {
				}
			}
			// XXX SECURITY: This point is important
			System.setProperties(this);
		}
	}

	private static final String mapNull(final Object obj, final String def) {
		return (obj == null) ? def : obj.toString();
	}

	public static void main(final String[] args) {
		System.setErr(System.out);
		System.out.println("-------------------------------- vanilla");
		System.out.println(">>> " + System.getProperties());
		System.out.println(">>> " + System.getProperties().stringPropertyNames());
		System.out.println(">>> " + System.getProperty("java.version"));

		System.out.println("-------------------------------- uninitialized");
		System.out.println(">>> " + SystemClassLoaderProperties.getInstance().getProperty("java.version"));

		System.out.println("-------------------------------- takeover");
		SystemClassLoaderProperties.getInstance().takeover();

		System.out.println("-------------------------------- initialized");
		System.out.println(">>> " + System.getProperties());
		System.out.println(">>> " + System.getProperties().stringPropertyNames());
		System.out.println(">>> " + System.getProperty("java.version"));
	}
}

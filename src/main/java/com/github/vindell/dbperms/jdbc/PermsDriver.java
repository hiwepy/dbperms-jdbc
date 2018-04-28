package com.github.vindell.dbperms.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.vindell.dbperms.jdbc.common.ConnectionInformation;
import com.github.vindell.dbperms.jdbc.event.JdbcEventListener;
import com.github.vindell.dbperms.jdbc.event.JdbcEventListenerFactory;
import com.github.vindell.dbperms.jdbc.event.JdbcEventListenerFactoryLoader;
import com.github.vindell.dbperms.jdbc.wrapper.ConnectionWrapper;

/**
 * JDBC driver for dbperms
 * 
 * @author ： <a href="https://github.com/vindell">vindell</a>
 */
public class PermsDriver implements java.sql.Driver {

	protected static Logger LOG = LoggerFactory.getLogger(PermsDriver.class);

	private static Driver INSTANCE = new PermsDriver();
	private static JdbcEventListenerFactory jdbcEventListenerFactory;

	static {
		try {
			DriverManager.registerDriver(PermsDriver.INSTANCE);
		} catch (SQLException e) {
			throw new IllegalStateException("Could not register PermsDriver with DriverManager", e);
		}
	}

	/**
	 * 查询驱动程序是否认为它可以打开到给定 URL 的连接
	 */
	@Override
	public boolean acceptsURL(final String url) {
		return url != null && url.startsWith("jdbc:perms:");
	}

	/**
	 * Parses out the real JDBC connection URL by removing "p6spy:".
	 *
	 * @param url
	 *            the connection URL
	 * @return the parsed URL
	 */
	private String extractRealUrl(String url) {
		return acceptsURL(url) ? url.replace("perms:", "") : url;
	}

	static List<Driver> registeredDrivers() {
		List<Driver> result = new ArrayList<Driver>();
		for (Enumeration<Driver> driverEnumeration = DriverManager.getDrivers(); driverEnumeration.hasMoreElements();) {
			result.add(driverEnumeration.nextElement());
		}
		return result;
	}
	
	/**
	 * Creates a new database connection to a given URL.
	 * 
	 * @param url the URL
	 * @param prop the properties
	 * @return a Connection object that represents a connection to the URL
	 * @throws SQLException
	 * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
	 */
	@Override
	public Connection connect(String url, Properties properties) throws SQLException {
		// if there is no url, we have problems
		if (url == null) {
			throw new SQLException("url is required");
		}

		if (!acceptsURL(url)) {
			return null;
		}

		// find the real driver for the URL
		Driver passThru = findPassthru(url);

		final long start = System.nanoTime();

		if (PermsDriver.jdbcEventListenerFactory == null) {
			PermsDriver.jdbcEventListenerFactory = JdbcEventListenerFactoryLoader.load();
		}
		final Connection conn;
		final JdbcEventListener jdbcEventListener = PermsDriver.jdbcEventListenerFactory.createJdbcEventListener();
		final ConnectionInformation connectionInformation = ConnectionInformation.fromDriver(passThru);
		jdbcEventListener.onBeforeGetConnection(connectionInformation);
		try {
			conn = passThru.connect(extractRealUrl(url), properties);
			connectionInformation.setConnection(conn);
			connectionInformation.setTimeToGetConnectionNs(System.nanoTime() - start);
			jdbcEventListener.onAfterGetConnection(connectionInformation, null);
		} catch (SQLException e) {
			connectionInformation.setTimeToGetConnectionNs(System.nanoTime() - start);
			jdbcEventListener.onAfterGetConnection(connectionInformation, e);
			throw e;
		}

		return ConnectionWrapper.wrap(conn, jdbcEventListener, connectionInformation);
	}

	/**
	 * 查找可执行当前数据库URL的真实数据库驱动对象
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @param url  数据库URL
	 * @return 数据库驱动对象
	 * @throws SQLException 未找到，则抛出异常
	 */
	protected Driver findPassthru(String url) throws SQLException {
		String realUrl = extractRealUrl(url);
		Driver passthru = null;
		for (Driver driver : registeredDrivers()) {
			try {
				if (driver.acceptsURL(realUrl)) {
					passthru = driver;
					break;
				}
			} catch (SQLException e) {
			}
		}
		if (passthru == null) {
			throw new SQLException("Unable to find a driver that accepts " + realUrl);
		}
		return passthru;
	}

	/**
	 * 获得此驱动程序的可能属性信息
	 */
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties properties) throws SQLException {
		return findPassthru(url).getPropertyInfo(url, properties);
	}
	
	/**
	 * 获取此驱动程序的主版本号
	 */
	@Override
	public int getMajorVersion() {
		return PermsVersion.getMajorVersion();
	}

	/**
	 * 获得此驱动程序的次版本号
	 */
	@Override
	public int getMinorVersion() {
		return PermsVersion.getMinorVersion();
	}

	/**
	 * 报告此驱动程序是否是一个真正的 JDBC CompliantTM 驱动程序
	 */
	@Override
	public boolean jdbcCompliant() {
		// This is a bit of a problem since there is no URL to determine the passthru!
		return true;
	}

	// Note: @Override annotation not added to allow compilation using Java 1.6
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Feature not supported");
	}

	public static void setJdbcEventListenerFactory(JdbcEventListenerFactory jdbcEventListenerFactory) {
		PermsDriver.jdbcEventListenerFactory = jdbcEventListenerFactory;
	}
	 


}

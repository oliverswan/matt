package net.oliver.database;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import net.oliver.app.app6.Constants;

public class DBManager implements IDatabaseManager {

	private static Logger log = LoggerFactory.getLogger(DBManager.class);

	private static ComboPooledDataSource dataSource = null;
	
	private static DBManager instance = new DBManager();
	
	
	public static DBManager getInstance() {
		return instance;
	}
	
	static {

		Properties conf = new Properties();
		try {
			conf.load(new FileInputStream(new File("./config/config.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		dataSource = new ComboPooledDataSource(conf.getProperty(Constants.DB_DATASOURCE_NAME));

		try {
			dataSource.setDriverClass(conf.getProperty(Constants.DB_DBDRIVERNAME_KEY));
			dataSource.setJdbcUrl(conf.getProperty(Constants.DB_PROTOCOL));
			dataSource.setUser(conf.getProperty(Constants.DB_USERNAME_KEY));
			dataSource.setPassword(conf.getProperty(Constants.DB_PASSWORD_KEY));
			dataSource.setAcquireIncrement(Integer.parseInt(conf.getProperty(Constants.DB_INCREMENT)));
			dataSource.setInitialPoolSize(Integer.parseInt(conf.getProperty(Constants.DB_INITIALSIZE)));
			dataSource.setMinPoolSize(Integer.parseInt(conf.getProperty(Constants.DB_MINPOOLSIZE)));
			dataSource.setMaxPoolSize(Integer.parseInt(conf.getProperty(Constants.DB_MAXPOOLSIZE)));
			dataSource.setMaxStatements(Integer.parseInt(conf.getProperty(Constants.DB_MAXSTATEMENTS)));
			dataSource.setMaxStatementsPerConnection(
					Integer.parseInt(conf.getProperty(Constants.DB_MAXSTATEMENTSPERCONNECTION)));
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

	}

	private static Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			log.error("Exception during get DataBase Connection ,Message : " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Map> select(String sql) throws Exception {
		List<Map> result = new ArrayList<Map>();
		Connection con = DBManager.getConnection();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] colnames = new String[columnCount];
			for (int j = 1; j <= columnCount; j++) {
				colnames[j - 1] = rsmd.getColumnName(j);
			}
			while (rs.next()) {
				Map row = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					if ("".equals(colnames[i - 1])) {
						row.put("count", rs.getString(i));
					} else {
						row.put(colnames[i - 1], rs.getString(i));
					}

				}
				result.add(row);
			}
			rs.close();
			st.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return result;
	}

	@Override
	public int update(String sql) throws Exception {
		Connection con = DBManager.getConnection();
		try {
			Statement st = con.createStatement();
			int result = st.executeUpdate(sql);
			st.close();
			return result;
		} finally {
			con.close();
		}
	}

	@Override
	public void close() throws Exception {
		dataSource.close();
	}

}

package com.github.andreptb.fitnesse;

import java.sql.Driver;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * Slim fixture to execute jdbc commands, see README.md for more information.
 */
public class JdbcFixture {

	private static final String SELECT_COMMAND_PREFIX = "SELECT";
	/**
	 * Registered databases, the key being database name and value an instance of JdbcTemplate
	 */
	private Map<String, JdbcTemplate> templateMap = new TreeMap<>();

	/**
	 * Registers a database to further execute SQL commands
	 *
	 * <p><code>
	 * | connect jdbc on | <i>database</i> | with url | <i>url</i> | and driver | <i>driver</i> | and username | <i>username</i> | and | <i>password</i> |
	 * </code></p>
	 * @param url
	 */
	@SuppressWarnings("unchecked")
	public boolean connectJdbcOnWithUrlAndDriverAndUsernameAndPassword(String dataBaseId, String url, String driverClassName, String username, String password) throws ReflectiveOperationException {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClass((Class<Driver>) Class.forName(driverClassName));
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		this.templateMap.put(dataBaseId, new JdbcTemplate(dataSource));
		return true;
	}

	/**
	 * Simply runs a SQL command, used for udpates, inserts which the result doesn't matter.
	 *
	 * <p><code>
	 * | run in | <i>database</i> | the sql | <i>sql</i> |
	 * </code></p>
	 * @param database registered database to run the SQL
	 * @param sql to run
	 */
	public boolean runInTheSql(String database, final String sql) {
		getDatabaseJdbcTemplate(database).update(sql);
		return true;
	}

	/**
	 * Used generally when the result is assigned to a variable
	 *
	 * <p><code>
	 * | $variable= | query in | <i>database</i> | with sql | <i>sql</i> |
	 * </code></p>
	 * @param database registered database to run the SQL
	 * @param sql to run
	 */
	public String queryInWithSql(String database, String sql) {
		JdbcTemplate template = getDatabaseJdbcTemplate(database);
		if (sql != null && !sql.trim().toUpperCase().startsWith(JdbcFixture.SELECT_COMMAND_PREFIX)) {
			return Objects.toString(template.update(sql));
		}
		List<String> results = template.queryForList(sql, String.class);
		if(results == null || results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

	private JdbcTemplate getDatabaseJdbcTemplate(String database) {
		JdbcTemplate template = this.templateMap.get(database);
		if(template == null) {
			throw new IllegalArgumentException(String.format("No database registered for name '%s'. Registered databases: %s", database, this.templateMap.keySet()));
		}
		return template;
	}

}

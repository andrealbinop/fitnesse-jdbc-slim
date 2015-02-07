package org.andreptb.fitnesse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Driver;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Slim fixture to execute jdbc commands, see README.md for more information.
 */
public class JdbcFixture {

	/**
	 * Registered databases, the key being database name and value an instance of JdbcTemplate
	 */
	private Map<String, JdbcTemplate> templateMap = new TreeMap<>();

	/**
	 * <p><code>
	 * | connect jdbc on | <i>database</i> | with url | <i>url</i> | and driver | <i>driver</i> | and username | <i>username</i> | and | <i>password</i> |
	 * </code></p>
	 * @param url
	 */
	public boolean connectJdbcOnWithUrlAndDriverAndUsernameAndPassword(String dataBaseId, String url, String driverClassName, String username, String password) throws ReflectiveOperationException {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClass((Class<? extends Driver>) Class.forName(driverClassName));
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		this.templateMap.put(dataBaseId, new JdbcTemplate(dataSource));
		return true;
	}

	/**
	 * <p><code>
	 * | execute in database | <i>database</i> | the sql | <i>sql</i> |
	 * </code></p>
	 * @param sql
	 */
	public String executeInDatabaseTheSql(String database, final String sql) {
		JdbcTemplate template = this.templateMap.get(database);
		if(template == null) {
			throw new IllegalArgumentException(String.format("No database registered for name '%s'. Registered databases: %s", database, templateMap.keySet()));
		}
		if(!sql.toUpperCase().startsWith("SELECT")) {
			return Boolean.toString(template.update(sql) > 0);
		}
		List<String> results = template.queryForList(sql, String.class);
		if(results == null || results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

}

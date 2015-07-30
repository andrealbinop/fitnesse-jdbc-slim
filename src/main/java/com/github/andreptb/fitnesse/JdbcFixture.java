package com.github.andreptb.fitnesse;

import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import fitnesse.ContextConfigurator;

/**
 * Slim fixture to execute jdbc commands, see README.md for more information.
 */
public class JdbcFixture {

	private static final String SELECT_COMMAND_PREFIX = "SELECT";
	/**
	 * Registered databases, the key being database name and value an instance of JdbcTemplate
	 */
	private static Map<String, JdbcTemplate> JDBCTEMPLATE_MAP = new TreeMap<>();
	/**
	 * Last used database name, useful to run multiple commands without needing to repeat database name
	 */
	private static String CURRENT_DATABASE;
	/**
	 * SQLs scripts directory which will be preppended if script file informed don't have an absolute path
	 */
	private static String SCRIPT_DIRECTORY = ContextConfigurator.DEFAULT_ROOT + "/files/sqlScripts";
	/**
	 * <p>
	 * <code>
	 * | register connection | <i>name</i> | with url | <i>url</i> | and driver | <i>driver</i> | and username | <i>username</i> | and | <i>password</i> |
	 * </code>
	 * </p>
	 * Registers a database to further execute SQL commands
	 *
	 * @param dataBaseId
	 */
	@SuppressWarnings("unchecked")
	public boolean registerConnectionWithUrlAndDriverAndUsernameAndPassword(String name, String url, String driverClassName, String username, String password) throws ReflectiveOperationException {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClass((Class<Driver>) Class.forName(driverClassName));
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		JdbcFixture.JDBCTEMPLATE_MAP.put(name, new JdbcTemplate(dataSource));
		JdbcFixture.CURRENT_DATABASE = name;
		return true;
	}

	/**
	 * @return current database configured to run statements
	 */
	public String getCurrentDatabase() {
		return JdbcFixture.CURRENT_DATABASE;
	}

	/**
	 * <p>
	 * <code>
	 * | switch to connection | <i>name</i> |
	 * </code>
	 * </p>
	 * Switch the database configured to run statements. The configuration must be previosly registered with "register connection" action before it can be switched with this action
	 *
	 * @param name previosly registered database name
	 * @return <code>true</code> if switch occurs sucessfully
	 * @throws IllegalArgumentException if this name wasn't registered with "register connection" action
	 */
	public boolean switchToConnection(String name) {
		if (!JdbcFixture.JDBCTEMPLATE_MAP.containsKey(name)) {
			throw new IllegalArgumentException("This base is not yet registered, please register with \"register connection\" action before switching");
		}
		JdbcFixture.CURRENT_DATABASE = name;
		return true;
	}

	/**
	 * <p>
	 * <code>
	 * | run script | <i>scriptFile1, scriptFile2</i> |
	 * </code>
	 * </p>
	 * Runs a SQL script on the currently connected database. Creates a transaction before running, tries to rollback everything if anything fails.
	 *
	 * @param scriptFile Script file to run, can be multiple comma separated files
	 * @return <code>true</code> if script runs successfully
	 * @throws ScriptException if script fails to run.
	 */
	public boolean runScript(final String scriptFile) {
		final DataSource dataSource = getDatabaseJdbcTemplate().getDataSource();
		TransactionTemplate transaction = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
		return transaction.execute(new TransactionCallback<Boolean>() {
			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				Collection<Resource> scriptResources = new ArrayList<>();
				for (String script : StringUtils.split(scriptFile, ",")) {
					FileSystemResource resource = new FileSystemResource(StringUtils.strip(script));
					scriptResources.add(new FileSystemResource(StringUtils.strip(script)));
				}
				new ResourceDatabasePopulator(scriptResources.toArray(new Resource[scriptResources.size()])).execute(dataSource);
				return true;
			}
		});
	}

	/**
	 * <p>
	 * <code>
	 * | $variable= | run sql | <i>sql</i> |
	 * </code>
	 * </p>
	 * Runs a SQL and responds the first column from the first row fetched from the result set.
	 *
	 * @param database registered database to run the SQL
	 * @param sql to run
	 * @return first column from the first row fetched from the result set if SQL is SELECT. If sql is INSERT/UPDATE/DELETE, returns the rows affected
	 */
	public String runSql(String sql) {
		JdbcTemplate template = getDatabaseJdbcTemplate();
		if (sql.trim().toUpperCase().startsWith(JdbcFixture.SELECT_COMMAND_PREFIX)) {
			return template.query(sql, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						return rs.getString(1);
					}
					return null;
				}
			});
		}
		return Objects.toString(template.update(sql));
	}

	/**
	 * @return {@link JdbcTemplate} instance for {@link #CURRENT_DATABASE}
	 * @throws IllegalArgumentException if {@link #CURRENT_DATABASE} is blank
	 */
	private JdbcTemplate getDatabaseJdbcTemplate() {
		String database = getCurrentDatabase();
		if (StringUtils.isBlank(database)) {
			throw new IllegalArgumentException("Please connect to a database before running SQL commands");
		}
		return JdbcFixture.JDBCTEMPLATE_MAP.get(database);
	}

}

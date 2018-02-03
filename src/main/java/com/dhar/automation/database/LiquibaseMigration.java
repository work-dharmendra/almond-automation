package com.dhar.automation.database;

import com.dhar.automation.exception.AutomationBaseRuntimeException;
import com.dhar.automation.exception.AutomationErrorType;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Dharmendra Chouhan
 */

@Component
public class LiquibaseMigration implements Migration {
    private static Logger LOGGER = LoggerFactory.getLogger(LiquibaseMigration.class);

    @Autowired
    DataSource dataSource;

    @Override
    public void update(Map<String, String> params) {
        LOGGER.info("updating database");

        try {
            Class.forName("com.mysql.jdbc.Driver");

            /*Connection connection = DriverManager.getConnection("jdbc:mysql://" + params.get("host") + "/" + params.get("database")
                    , params.get("username"), params.get("password"));*/

            Connection connection = dataSource.getConnection();

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("liquibase/changelog.xml", new ClassLoaderResourceAccessor(), database);
            String file = "liquibase.log";
            FileWriter writer = new FileWriter(file);

            liquibase.update(new Contexts(), writer);
            LOGGER.info("Database is successfully updated and executed sql are logged at {}", file);
            writer.flush();
            writer.close();
            liquibase.update(new Contexts(), new LabelExpression());
        }
        catch (UnknownHostException e) {
            LOGGER.error("unable to update database using liquibase because of unknown host exception, error = {}", e);
            throw new AutomationBaseRuntimeException(AutomationErrorType.ERROR_DATABASE_HOST_NOTFOUND, e.getMessage());
        } catch (SQLException e) {
            LOGGER.error("unable to update database using liquibase because of unknown host exception, error = {}", e);
            throw new AutomationBaseRuntimeException(AutomationErrorType.ERROR_DATABASE_SQL, e.getMessage());
        } catch (Exception e) {
            LOGGER.error("unable to update database using liquibase, error = {}", e);
            throw new AutomationBaseRuntimeException(AutomationErrorType.ERROR_DATABASE_UPDATE_FAIL, e.getMessage());
        }
    }
}

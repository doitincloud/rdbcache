package doitincloud.rdbcache.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {

    @Value("${spring.datasource.initialization:true}")
    private Boolean initialization = true;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabasePopulator databasePopulator;

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);
        initializer.setEnabled(initialization);
        return initializer;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        return template;
    }
}

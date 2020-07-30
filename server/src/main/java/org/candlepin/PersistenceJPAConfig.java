/*
 *  Copyright (c) 2009 - ${YEAR} Red Hat, Inc.
 *
 *  This software is licensed to you under the GNU General Public License,
 *  version 2 (GPLv2). There is NO WARRANTY for this software, express or
 *  implied, including the implied warranties of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 *  along with this software; if not, see
 *  http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 *  Red Hat trademarks are not licensed under GPLv2. No permission is
 *  granted to use or replicate Red Hat trademarks that are incorporated
 *  in this software or its documentation.
 */

package org.candlepin;

//import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
        //(mode = AdviceMode.ASPECTJ)
//@EnableJpaRepositories(
//        basePackages = "org.candlepin.model",
//        entityManagerFactoryRef = "candlepinEntityManagerFactory",
//        transactionManagerRef = "candlepinTransactionManager")
//@PropertySource("classpath:application.properties")
public class PersistenceJPAConfig {
    @Bean
    public SessionFactory sessionFactory(@Qualifier("entityManagerFactory") EntityManagerFactory emf) {
        return emf.unwrap(SessionFactory.class);
    }
//    @Autowired
//    public Environment env;
//
//    @Bean(name="candlepinDataSource")
//    @Primary
//    public ComboPooledDataSource dataSource() throws PropertyVetoException {
//        ComboPooledDataSource dataSource = new ComboPooledDataSource();
//        dataSource.setMinPoolSize(Integer.parseInt(env.getProperty("hibernate.c3p0.min_size")));
//        dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("hibernate.c3p0.max_size")));
//        dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("hibernate.c3p0.idle_test_period")));
//        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setPassword(env.getProperty("spring.datasource.password"));
//        dataSource.setUser(env.getProperty("spring.datasource.username"));
//        dataSource.setDriverClass(env.getProperty("spring.datasource.driver-class-name"));
//        return dataSource;
//    }
//
//    /**
//     * Declare the JPA entity manager factory.
//     */
//    @Bean(name = "candlepinEntityManagerFactory")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("candlepinDataSource") ComboPooledDataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean entityManagerFactory =
//                new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactory.setDataSource(dataSource);
//
//        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
//
//        // Classpath scanning of @Component, @Service, etc annotated class
//        entityManagerFactory.setPackagesToScan("org.candlepin.model");
//
//        // Vendor adapter
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
//
//        // Hibernate properties
//        Properties additionalProperties = new Properties();
//        additionalProperties.put(
//                "hibernate.dialect",
//                env.getProperty("hibernate.dialect"));
//        additionalProperties.put(
//                "hibernate.show_sql",
//                env.getProperty("hibernate.show_sql"));
//        additionalProperties.put(
//                        "current_session_context_class",
//                        env.getProperty("hibernate.current_session_context_class")) ;
//        entityManagerFactory.setJpaProperties(additionalProperties);
//
////        return builder
////                .dataSource(dataSource)
////                .packages("org.candlepin.model")
////                .persistenceUnit("default")
////                .build();
//
//        return entityManagerFactory;
//    }
//
//    /**
//     * Declare the transaction manager.
//     */
//    @Bean(name = "candlepinTransactionManager")
//    @Primary
//    public JpaTransactionManager transactionManager(@Qualifier("candlepinEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
//        JpaTransactionManager transactionManager =
//                new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                entityManagerFactory.getObject());
//        return transactionManager;
//    }
//
////    @Bean
////    public LocalSessionFactoryBean sessionFactory(@Qualifier("candlepinDataSource") ComboPooledDataSource dataSource) {
////        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
////        sessionFactory.setDataSource(dataSource);
////        sessionFactory.setPackagesToScan(new String[]{"org.candlepin.model"});
////        sessionFactory.setHibernateProperties(hibernateProperties());
////
////        return sessionFactory;
////    }
//
//    private final Properties hibernateProperties() {
//        Properties hibernateProperties = new Properties();
//        hibernateProperties.put(
//                "hibernate.dialect",
//                env.getProperty("hibernate.dialect"));
//        hibernateProperties.put(
//                "hibernate.show_sql",
//                env.getProperty("hibernate.show_sql"));
//        hibernateProperties.put(
//                "hibernate.current_session_context_class",
//                env.getProperty("hibernate.current_session_context_class"));
//        return hibernateProperties;
//    }
//
////    @Bean
////    public PlatformTransactionManager hibernateTransactionManager(@Qualifier("candlepinDataSource") ComboPooledDataSource dataSource1) {
////        HibernateTransactionManager transactionManager
////                = new HibernateTransactionManager();
////        transactionManager.setSessionFactory(sessionFactory(dataSource1).getObject());
////        return transactionManager;
////    }
//
////    @Bean
////    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
////        return new PersistenceExceptionTranslationPostProcessor();
////    }
}

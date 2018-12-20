package com.manimalang.config;

import java.io.IOException;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import com.manimalang.cron.Cronjob;
import com.manimalang.utils.ApplicationProperties;

@Configuration
public class ApplicationContextConfig {
	
	private static final Logger logger = Logger.getLogger(ApplicationContextConfig.class);
	  
	  @Autowired
	  private Environment env;
	


	 @Bean
	  public ResourceBundleMessageSource messageSource() {
	      ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
	      // Load property in src/resources/message.properties
	      rb.setBasenames(new String[] { "messages" });
	      rb.setUseCodeAsDefaultMessage(true);
	      return rb;
	  }
	 
	 // Transaction Manager
	  @Autowired
	  @Bean(name = "transactionManager")
	  public DataSourceTransactionManager getTransactionManager(DataSource dataSource) {
	      DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
	 
	      return transactionManager;
	  }
	  
	  @Bean(initMethod="init")
	  public ApplicationProperties  getMyBean() {
	   return new ApplicationProperties();
	  }
	  
	 /* @Bean(name = "multipartResolver")
	  public CommonsMultipartResolver multipartResolver() {
	      CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	      multipartResolver.setMaxUploadSize(200000000);
	      return new CommonsMultipartResolver();
	  }*/
/*
	  @Bean(name = "dataSource")
	  public DataSource getDataSource() {
	      DriverManagerDataSource dataSource = new DriverManagerDataSource();
	 
	      // See: datasouce-cfg.properties
	      dataSource.setDriverClassName(env.getProperty("ds.database-driver"));
	      dataSource.setUrl(env.getProperty("ds.url"));
	      dataSource.setUsername(env.getProperty("ds.username"));
	      dataSource.setPassword(env.getProperty("ds.password"));
	 
	      logger.info("## getDataSource: " + dataSource);
	 
	      return dataSource;
	  }*/
	  
	  @Bean
	  public VelocityEngine velocityEngine() throws VelocityException, IOException{
	  	VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
	  	Properties props = new Properties();
	  	props.put("resource.loader", "class");
	  	props.put("class.resource.loader.class", 
	  			  "org.apache.velocity.runtime.resource.loader." + 
	  			  "ClasspathResourceLoader");
	  	factory.setVelocityProperties(props);
	  	
	  	return factory.createVelocityEngine();
	  }
	  
	  @Bean
	  public JavaMailSender getMailSender(){
	      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	       
	      //Using gmail
	      mailSender.setHost(env.getProperty("mail.host"));
	      mailSender.setPort((Integer.parseInt(env.getProperty("mail.port"))));
	      mailSender.setUsername(env.getProperty("mail.username"));
	      mailSender.setPassword(env.getProperty("mail.password"));
	      
	      mailSender.setJavaMailProperties(getMailProperties());
	      return mailSender;
	  }
	  
	  @Bean 
	  public Session getSessionObject() {
		  Session sessionobj = Session.getInstance(getMailProperties(),
			         new javax.mail.Authenticator() {
			            protected PasswordAuthentication getPasswordAuthentication() {
			               return new PasswordAuthentication(env.getProperty("mail.username"), env.getProperty("mail.password"));
				   }
			         });
		  return sessionobj;
	  }
	  
	  private Properties getMailProperties() {
		  Properties javaMailProperties = new Properties();
	      /*javaMailProperties.put("mail.smtp.starttls.enable", "true");
	      javaMailProperties.put("mail.smtp.auth", "true");
	      javaMailProperties.put("mail.transport.protocol", "smtp");
	      javaMailProperties.put("mail.debug", "true");*/
	      
	      javaMailProperties.put("mail.smtp.auth", "true");
	      javaMailProperties.put("mail.smtp.starttls.enable", "true");
	      javaMailProperties.put("mail.smtp.host", env.getProperty("mail.host"));
	      javaMailProperties.put("mail.smtp.port", Integer.parseInt(env.getProperty("mail.port")));
	      javaMailProperties.put("mail.debug", "false");
	      
	      return javaMailProperties;
	  }
	  
	  @Bean
		public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
			return new PropertySourcesPlaceholderConfigurer();
		}
	  
	  @Bean
	  public Cronjob taskScheduler() {
		  Cronjob className = new Cronjob();
	     // set properties
	     return className;
	  }
	  

	/*@Bean
	public FlatFileItemReader<ServiceDateSlot> csvAnimeReader() {
		FlatFileItemReader<ServiceDateSlot> reader = new FlatFileItemReader<ServiceDateSlot>();
		reader.setResource(new ClassPathResource("service_date_slot.csv"));
		reader.setLineMapper(new DefaultLineMapper<ServiceDateSlot>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "id", "service_master_id", "next_no_of_day_available" ,"time_slot_duration" ,"next_disabled_dates" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<ServiceDateSlot>() {
					{
						setTargetType(ServiceDateSlot.class);
					}
				});
			}
		});
		return reader;
	}*/
}
package com.devinforest;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
@PropertySource("classpath:google.properties")
public class DevinforestApplication {
	@Value("${google.username}")
	public String username;
	@Value("${google.password}")
	public String password;
	public static void main(String[] args) {
		SpringApplication.run(DevinforestApplication.class, args);
	}
	@Bean
	public JavaMailSender getJavaMailSender() {
	      JavaMailSenderImpl  javaMailSender = new JavaMailSenderImpl();
	      javaMailSender.setHost("smtp.gmail.com");
	      javaMailSender.setPort(587);
	      //System.out.println(username);
	      //System.out.println(password);
	      javaMailSender.setUsername(username);
	      javaMailSender.setPassword(password);
	      
	      Properties prop = new Properties();//Properties ==HashMap<String, String>
	      prop.setProperty("mail.smtp.auth", "true");
	      prop.setProperty("mail.smtp.starttls.enable", "true");
	      javaMailSender.setJavaMailProperties(prop);
	      return javaMailSender;
	   }
}

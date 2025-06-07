package org.dailyfarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DailyFarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyFarmApplication.class, args);
	}

}

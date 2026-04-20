package swp391.old_bicycle_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OldBicycleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(OldBicycleProjectApplication.class, args);
	}

}

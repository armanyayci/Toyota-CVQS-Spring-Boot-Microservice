package toyotabackend.toyotabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ToyotaBackendApplication {
	public static void main(String [] args) {

			System.load("C:/Users/arman/Documents/GitHub/toyota-backend/opencv/build/java/x64/opencv_java470.dll");
			SpringApplication.run(ToyotaBackendApplication.class, args);
		}
}

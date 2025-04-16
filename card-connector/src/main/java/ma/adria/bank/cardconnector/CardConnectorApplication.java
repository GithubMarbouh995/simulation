package ma.adria.bank.cardconnector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"ma.adria.bank"})
public class CardConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardConnectorApplication.class, args);
    }

}

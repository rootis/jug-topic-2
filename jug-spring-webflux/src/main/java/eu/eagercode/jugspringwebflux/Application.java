package eu.eagercode.jugspringwebflux;

import eu.eagercode.jugspringwebflux.configs.MainConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        new AnnotationConfigApplicationContext(MainConfig.class);

        System.out.println("Press ENTER to exit.");
        System.in.read();
    }
}

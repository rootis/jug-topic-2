package eu.eagercode.jugwebflux;

import eu.eagercode.jugwebflux.configs.NettyServerConfig;
import eu.eagercode.jugwebflux.routes.TopicRoute;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        NettyServerConfig.start(TopicRoute::routes);

        System.out.println("Press ENTER to exit.");
        System.in.read();
    }
}

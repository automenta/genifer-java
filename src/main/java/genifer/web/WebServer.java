package genifer.web;

import genifer.Genifer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;
import org.eclipse.jetty.server.handler.ResourceHandler;

/**
 * @author Federico Recio
 */
public class WebServer extends Application<SampleConfiguration> {

    public final Genifer genifer = new Genifer();
    
    private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();

    public static void main(String...args) throws Exception {
        if (args.length == 0)
            args = new String[] { "server" };
        
        new WebServer().run(args);
    }

    @Override
    public void initialize(Bootstrap<SampleConfiguration> bootstrap) {
        swaggerDropwizard.onInitialize(bootstrap);
    }

    @Override
    public void run(SampleConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new GeniferWeb(genifer));

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setEtags(true);
        resource_handler.setResourceBase("web");
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });

        environment.getApplicationContext().insertHandler(resource_handler);
        
        swaggerDropwizard.onRun(configuration, environment, "localhost");
    }
}

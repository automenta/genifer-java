package genifer.web;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;

/**
 * @author Federico Recio
 */
public class WebServer extends Application<SampleConfiguration> {

    private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();

    public static void main(String...args) throws Exception {
        if (args.length == 0)
            args = new String[] { "server" };
        
        new WebServer().run(args);
    }

    @Override
    public void initialize(Bootstrap<SampleConfiguration> bootstrap) {
        //bootstrap.addBundle(new AssetsBundle("/assets/web", "/", "index.html", "genifer-client"));

        swaggerDropwizard.onInitialize(bootstrap);
    }

    @Override
    public void run(SampleConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new GeniferWeb());
        environment.jersey().setUrlPattern("/*");
        
        new AssetsBundle("/web", "/").run(environment);
        
        swaggerDropwizard.onRun(configuration, environment, "localhost");
    }
}

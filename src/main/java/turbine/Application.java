package turbine;

import com.netflix.turbine.init.TurbineInit;
import com.netflix.turbine.plugins.PluginsFactory;
import com.netflix.turbine.streaming.servlet.TurbineStreamServlet;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by sgibb on 7/11/14.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableEurekaClient
public class Application extends SpringBootServletInitializer implements SmartLifecycle {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class).web(true);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    @Bean
    public ServletRegistrationBean mockStreamServlet() {
        return new ServletRegistrationBean(new TurbineStreamServlet(), "/turbine.stream");
    }

    private boolean running;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public void start() {
        PluginsFactory.setClusterMonitorFactory(new SpringAggregatorFactory());
        TurbineInit.init();
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}

package io.threatrix.threatmatrix.app;

import io.threatrix.threatmatrix.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("ThreatMatrix")
public class ThreatMatrix extends Application {

    public ThreatMatrix(ApplicationContext ctx) {
        super(ctx);
    }

    @Override
    public void start() {
        System.out.println("Started ThreatMatrix. ");
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}

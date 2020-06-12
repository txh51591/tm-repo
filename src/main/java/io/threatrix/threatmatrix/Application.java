package io.threatrix.threatmatrix;  import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class Application {
    @Autowired protected ApplicationContext ctx;
    public Application(ApplicationContext ctx){this.ctx=ctx;}
    public abstract void start();
    public void shutdown(){}
}
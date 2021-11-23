package io.threatrix.threatmatrix;  import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class Application {
    
    @Autowired protected ApplicationContext ctx;
    
    public Application(ApplicationContext ctx){this.ctx=ctx;}
    public abstract void start();
    public void shutdown(){}
}Organize your issues with project boards

Did you know you can manage projects in the same place you keep your code? Set up a project board on GitHub to streamline and automate your workflow.

Learn More Create a project
Sort tasks

Add issues and pull requests to your board and prioritize them alongside note cards containing ideas or task lists.

Plan your project

Sort tasks into columns by status. You can label columns with status indicators like "To Do", "In Progress", and "Done".

Automate your workflow

Set up triggering events to save time on project management—we’ll move tasks into the right columns for you.

Track progress

Keep track of everything happening in your project and see exactly what’s changed since the last time you looked.

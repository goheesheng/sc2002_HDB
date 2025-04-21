package ui.submenu;
import user.User;

import project.Project;
import java.util.List;

public class ProjectMenu {
    protected User user;

    public ProjectMenu(User user) {
        this.user = user;
    }

    public void viewProjects(List<Project> projects) {
        System.out.println("\n----- Viewing All Projects -----");

        //Check if projects is null or empty
        if (projects == null || projects.isEmpty()) {
            System.out.println("No projects available to display.");
            return;
        }
        
        // display if available
        for (Project project : projects) {
            System.out.println(project);
        }
    }   
}

    


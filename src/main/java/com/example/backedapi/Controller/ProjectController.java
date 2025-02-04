package com.example.backedapi.Controller;

import com.example.backedapi.Service.ProjectService;
import com.example.backedapi.model.Project;
import com.example.backedapi.model.Vo.ResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {
    private ProjectService projectService;
    @PostMapping("/add")
    public ResponseType<Project> addProject(@RequestBody Project project) {
        try {
            project=  projectService.addProject(project);
        }catch (Exception e){
            return new ResponseType<>( -1,null,"Error adding project");
        }

        return new ResponseType<>( 0,project);
    }
    @GetMapping("/get")
    public ResponseType<List<Project>> getProject() {
        return new ResponseType<>( 0,projectService.getProject());
    }

    @PostMapping("/update")
    public ResponseType<String> updateProject(@RequestBody Project project) {
        try {
            projectService.updateProject(project);
        }catch (Exception e){
            return new ResponseType<>( -1,"Error updating project");
        }

        return new ResponseType<>( 0,"Project updated successfully");
    }

    @PostMapping("/delete")
    public ResponseType<String> deleteProject(@RequestBody Project project) {
        try {
            projectService.deleteProject(project);
        }catch (Exception e){
            return new ResponseType<>( -1,"Error deleting project");
        }

        return new ResponseType<>( 0,"Project deleted successfully");
    }
}

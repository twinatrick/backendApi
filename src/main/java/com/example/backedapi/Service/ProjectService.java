package com.example.backedapi.Service;

import com.example.backedapi.Repository.ProjectRepository;
import com.example.backedapi.Repository.SkillMapUserAndProjectRepository;
import com.example.backedapi.Repository.SkillRepository;
import com.example.backedapi.model.Project;
import com.example.backedapi.model.Skill;
import com.example.backedapi.model.SkillMapUserAndProject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SkillMapUserAndProjectRepository skillMapUserAndProjectRepository;
    public Project addProject(Project project) {

        if (project.getKey() != null) {
            throw new IllegalArgumentException("Key must be null");
        }else if (project.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }else if(!projectRepository.findByName(project.getName()).isEmpty()){
            throw new IllegalArgumentException("Name already exists");
        }

        return   projectRepository.save(project);

    }
    public void updateProject(Project project) {
        if (project.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }else if (project.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        projectRepository.save(project);

    }
    public List<Project> getProject() {
        return projectRepository.findAll();
    }
    @Transactional
    public void deleteProject(Project project) {
        if (project.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        project=projectRepository.findById(project.getKey()).orElseThrow(
                ()->new IllegalArgumentException("Project not found")
        );
        SkillMapUserAndProject skillMapUserAndProject = new SkillMapUserAndProject();
        skillMapUserAndProject.setProject(project);
        Example<SkillMapUserAndProject> example = Example.of(skillMapUserAndProject);
        List<SkillMapUserAndProject> skillMapUserAndProjectList= skillMapUserAndProjectRepository.findAll(example);
        skillMapUserAndProjectRepository.deleteAll(skillMapUserAndProjectList);
        projectRepository.delete(project);

    }
}

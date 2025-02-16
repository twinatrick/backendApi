package com.example.backedapi.Service;

import com.example.backedapi.Repository.ProjectRepository;
import com.example.backedapi.Repository.SkillMapUserAndProjectRepository;
import com.example.backedapi.Repository.SkillRepository;
import com.example.backedapi.Repository.UserRepository;
import com.example.backedapi.model.db.Project;
import com.example.backedapi.model.db.Skill;
import com.example.backedapi.model.db.SkillMapUserAndProject;
import com.example.backedapi.model.db.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SkillMapUserAndProjectRepository skillMapUserAndProjectRepository;

    public Skill addSkill(Skill skill) {
        if (skill.getKey() != null) {
            throw new IllegalArgumentException("Key must be null");
        } else if (skill.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        Skill s=new Skill();
        s.setName(skill.getName());
        Example<Skill> example = Example.of(skill);
            if (skillRepository.exists(example)) {
            throw new IllegalArgumentException("Name already exists");
        }

        return skillRepository.save(skill);

    }

    public void updateSkill(Skill skill) {
        if (skill.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        } else if (skill.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        skillRepository.save(skill);

    }

    public List<Skill> getSkill() {
        return skillRepository.findAll();
    }

    public void BindSkillToUser(String skillKey, String UserKey) {
        Skill skill = skillRepository.findById(UUID.fromString(skillKey))
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));
        User user = userRepository.findById(UUID.fromString(UserKey))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        SkillMapUserAndProject skillMapUserAndProject = new SkillMapUserAndProject();
        skillMapUserAndProject.setSkill(skill);
        skillMapUserAndProject.setUser(user);
        Example<SkillMapUserAndProject> example = Example.of(skillMapUserAndProject);
        skillMapUserAndProjectRepository.findOne(
                example
        ).ifPresent(skillMapUserAndProject1 -> {
            throw new IllegalArgumentException("Skill already bind to user");
        });
        skillMapUserAndProjectRepository.save(skillMapUserAndProject);
    }

    public void BindSkillToProjectAndUser(String skillKey, String projectKey, String userKey) {
        Skill skill = skillRepository.findById(UUID.fromString(skillKey))
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));
        Project project = projectRepository.findById(UUID.fromString(projectKey))
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        User user = userRepository.findById(UUID.fromString(userKey))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        SkillMapUserAndProject skillMapUserAndProject = new SkillMapUserAndProject();
        skillMapUserAndProject.setSkill(skill);
        skillMapUserAndProject.setProject(project);
        skillMapUserAndProject.setUser(user);
        Example<SkillMapUserAndProject> example = Example.of(skillMapUserAndProject);
        skillMapUserAndProjectRepository.findOne(
                example
        ).ifPresent(skillMapUserAndProject1 -> {
            throw new IllegalArgumentException("Skill already bind to project");
        });
        skillMapUserAndProjectRepository.save(skillMapUserAndProject);
    }

    @Transactional
    public void deleteSkill(Skill skill) {
        if (skill.getKey() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        skill=skillRepository.findById(skill.getKey()).orElseThrow(()->new IllegalArgumentException("Skill not found"));
        SkillMapUserAndProject skillMapUserAndProject = new SkillMapUserAndProject();
        skillMapUserAndProject.setSkill(skill);
        Example<SkillMapUserAndProject> example = Example.of(skillMapUserAndProject);
        List<SkillMapUserAndProject> skillMapUserAndProjects = skillMapUserAndProjectRepository.findAll(example);
        skillMapUserAndProjectRepository.deleteAll(skillMapUserAndProjects);
        skillRepository.delete(skill);

    }
}

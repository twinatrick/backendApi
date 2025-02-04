package com.example.backedapi.Controller;

import com.example.backedapi.Service.SkillService;
import com.example.backedapi.model.Skill;
import com.example.backedapi.model.Vo.ResponseType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/skill")
public class SkillController {
    private SkillService skillService;
    @PostMapping("/add")
    public ResponseType<Skill> addSkill(@RequestBody Skill skill) {
        try {
            skill=   skillService.addSkill(skill);
        }catch (Exception e){
            return new ResponseType<>( -1,null,"Error adding skill");
        }

        return new ResponseType<>( 0,skill);
    }
    @GetMapping("/get")
    public ResponseType<List<Skill>> getSkill() {
        return new ResponseType<>( 0,skillService.getSkill());
    }
    @PostMapping("/update")
    public ResponseType<String> updateSkill(@RequestBody Skill skill) {
        try {
            skillService.updateSkill(skill);
        }catch (Exception e){
            return new ResponseType<>( -1,"Error updating skill");
        }

        return new ResponseType<>( 0,"Skill updated successfully");
    }

    @PostMapping("/delete")
    public ResponseType<String> deleteSkill(@RequestBody Skill skill) {
        try {
            skillService.deleteSkill(skill);
        }catch (Exception e){
            return new ResponseType<>( -1,"Error deleting skill");
        }

        return new ResponseType<>( 0,"Skill deleted successfully");
    }
}

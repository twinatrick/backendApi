package com.example.backedapi.Controller;

import com.example.backedapi.Service.FunctionService;
import com.example.backedapi.Service.UserService;
import com.example.backedapi.annotation.Ingnore;
import com.example.backedapi.fillter.JwtAuthenticationTokenFilter;
import com.example.backedapi.model.Function;
import com.example.backedapi.model.User;
import com.example.backedapi.model.Vo.LoginRequest;
import com.example.backedapi.model.Vo.ResponseType;
import com.example.backedapi.model.Vo.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/function")
public class FunctionController {
    @Autowired
    private FunctionService functionService;

    @PostMapping("/add")
    public ResponseType<?> addFunction(@RequestBody Function function) {
        try {
            function=  functionService.addFunction(function);
        }catch (Exception e){
            return new ResponseType<>( -1,null,"Error adding function");
        }

        return new ResponseType<>( 0,function);
    }
    @PostMapping("/update")

    public ResponseType<String> updateFunction(@RequestBody Function function) {
        try {
            functionService.updateFunction(function);
        }catch (Exception e){
            return new ResponseType<>( -1,"Error updating function");
        }

        return new ResponseType<>( 0,"Function updated successfully");
    }
    @PostMapping("/delete")
    public ResponseType<String> deleteFunction(@RequestBody Function function) {
        try {
            functionService.deleteFunction(function);
        }catch (Exception e){
            return new ResponseType<>( -1,"Error deleting function");
        }

        return new ResponseType<>( 0,"Function deleted successfully");
    }

    @GetMapping("/get")
    public ResponseType<List<Function>> getFunction() {
        return new ResponseType<>( 0,functionService.getFunction());
    }
}
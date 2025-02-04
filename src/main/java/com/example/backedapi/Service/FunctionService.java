package com.example.backedapi.Service;

import com.example.backedapi.Repository.FunctionRepository;
import com.example.backedapi.Repository.RoleFunctionRepository;
import com.example.backedapi.model.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionService {
    private final FunctionRepository functionRepository;
    private final RoleFunctionRepository roleFunctionRepository;

    public Function addFunction(Function function) {

        if (function.getId() != null) {
            throw new IllegalArgumentException("Key must be null");
        } else if (function.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        } else {
            Function f = new Function();
            f.setName(function.getName());
            Example<Function> example = Example.of(f);
            if (functionRepository.exists(example)) {
                throw new IllegalArgumentException("Name already exists");
            }
        }

        return functionRepository.save(function);

    }

    public List<Function> getFunction() {
        return functionRepository.findAll();
    }

    public void updateFunction(Function function) {
        if (function.getId() == null) {
            throw new IllegalArgumentException("Key must not be null");
        } else if (function.getName() == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        functionRepository.save(function);

    }

    public void deleteFunction(Function function) {
        if (function.getId() == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        roleFunctionRepository.deleteByFunction(function.getId());
        functionRepository.delete(function);

    }
}

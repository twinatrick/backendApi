package com.example.backedapi.Service;

import com.example.backedapi.Repository.FunctionRepository;
import com.example.backedapi.Repository.RoleFunctionRepository;
import com.example.backedapi.model.db.Function;
import com.example.backedapi.model.Vo.FunctionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Transactional
    public void deleteFunction(List<FunctionVo> function) {
        Date date = new Date();
        if (function.isEmpty()) {
            return;
        }
        List<Function> f= functionRepository.findAllById(function.stream().map(
                FunctionVo::getId
        ).map(UUID::fromString).collect(Collectors.toList()));
        roleFunctionRepository.deleteAllByFunctionIn(f)  ;
        functionRepository.deleteAll(f);
    }

@Transactional
    public void saveFunction(List<FunctionVo> function) {
        Date date = new Date();
        if (function.isEmpty()) {
            return;
        }
        List<Function> f = function.stream().map(
                functionVo -> {
                    Function temp = new Function();
                    if (functionVo.getId() != null) {
                        temp.setId(UUID.fromString(functionVo.getId()));
                    }
                    temp.setName(functionVo.getName());
                    temp.setParent(functionVo.getParent());
                    temp.setSort(functionVo.getSort());
                    temp.setType(functionVo.getType());
                    return temp;
                }
        ).collect(Collectors.toList());

        functionRepository.saveAll(f);
    }
    @Transactional
    public List<Function> saveFunctionNewChild(List<FunctionVo> function) {
        Date date= new Date();
        Sort sort = Sort.by(Sort.Direction.ASC, "sort");
        if (function.isEmpty()) {
            return functionRepository.findAll(sort) ;
        }
        List<String> GrandParentId = function.stream().map(
                FunctionVo::getGrandParentId
        ).collect(Collectors.toList());
        List<Function> saveNext = (GrandParentId.isEmpty()) ? new ArrayList<>() : functionRepository.findAllByGrandParentId(GrandParentId);
        List<Function> saveFunction = new ArrayList<>();
        for (FunctionVo functionVo : function) {
            for (Function f : saveNext) {
                if (f.getName().equals(functionVo.getParentName()) && f.getType() == 2 && f.getParent().equals(functionVo.getGrandParentId())) {
                    functionVo.setParent(f.getId().toString());
                    break;
                }
            }
            Function temp = new Function();
            temp.setName(functionVo.getName());
            temp.setParent(functionVo.getParent());
            temp.setSort(functionVo.getSort());
            temp.setType(3);
            saveFunction.add(temp);
        }

        if (!saveFunction.isEmpty()) {
            functionRepository.saveAll(saveFunction);
        }
        System.out.println("GrandParentId.size=" + GrandParentId.size() + "\n");
        System.out.println("saveFunctionNewChildTime=" + (( new Date().getTime() - date.getTime())/1000) + "\n");
        return  functionRepository.findAll(sort) ;
    }
        public Function getFunctionByName(String name) {
            return functionRepository.findFunctionByName(name);
        }

        public Function getFunctionByNameAndParent(String name, String parent) {
            return functionRepository.findFunctionByNameAndParent(name, parent);
        }
    }

package com.example.backedapi.model.Vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter@NoArgsConstructor
public class FunctionTransVo {
    private List<FunctionVo> deleteFunction;
    private List<FunctionVo> saveMainFunction;
    private List<FunctionVo> saveFunctionNewChild;
}

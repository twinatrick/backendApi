package com.example.backedapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Function {
    private String id="";
    private String name="";
    private String parent="";
    private String sort="";
}

package com.example.backedapi.model.Vo;

import com.example.backedapi.model.db.Function;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter@NoArgsConstructor
public class FunctionVo {
    private String id ;
    private String name="";
    private String parent="";
    private String sort="";
    private Integer type;
    private String parentName="";
    private String grandParentId="";
    private boolean disabled;
    private boolean edit;
    private boolean newAdd;
    private String newName="";
    private boolean delete;

    public Function toFunction(){
        Function function = new Function();
        if (this.id !=null)
            function.setId(UUID.fromString(this.id));
        function.setName(this.name);
        function.setParent(this.parent);
        function.setSort(this.sort);
        function.setType(this.type);
        return function;


    }
}


package  com.example.backedapi.Controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;

@Controller
class UserController {
    // This is a simple controller class that
    // will be used to handle the user requests
    @Getter
    private String name;

    Object getUser() {
        // This method will be used to get the user
        // from the database
        return null;
    }
}
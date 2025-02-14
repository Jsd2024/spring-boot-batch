package org.sb.batch.poc.app.controller;

import org.sb.batch.poc.app.model.UserTypeId;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usertype")
public class UserTypeController {

    @GetMapping("/{id}")
    public String getUserType(@PathVariable int id) {
        try {
            return UserTypeId.fromId(id).name();
        } catch (IllegalArgumentException e) {
            return "Invalid UserTypeId";
        }
    }
}

package org.sb.batch.poc.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeId {
    ADMIN(1), USER(2), NGO(3), COMPANY(4);

    private final int id;

//    UserTypeId(int id) {
//        this.id = id;
//    }

//    public int getId() {
//        return id;
//    }

    // Method to get UserTypeId by id
    public static UserTypeId fromId(int id) {
        for (UserTypeId type : UserTypeId.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UserTypeId: " + id);
    }
}

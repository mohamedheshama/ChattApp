package org.project.exceptions;

import java.sql.SQLException;

public class UserAlreadyExistException extends SQLException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}

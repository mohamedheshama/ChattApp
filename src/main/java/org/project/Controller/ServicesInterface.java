package org.project.Controller;

import org.project.model.dao.users.Users;
import java.rmi.Remote;

public interface ServicesInterface extends Remote{
    Users login(String phoneNumber,String Password);
    Boolean Register(Users user);

}

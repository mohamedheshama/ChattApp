package org.project.model.dao.friends;


import org.project.model.dao.users.Users;

public class Friends {
    private Users friend;
    private RequestStatus requestStatus;

    public Users getFriend() {
        return friend;
    }

    public void setFriend(Users friend) {
        this.friend = friend;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}

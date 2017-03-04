package testcompany.cloudmessagingtest2;

/**
 * Created by arnardesktop on 4.3.2017.
 */

public class Contact {
    private boolean isBlocked;
    private int id;
    private String username;

    Contact(boolean isBlocked, int id, String username){
        this.isBlocked = isBlocked;
        this.id = id;
        this.username = username;
    }

    public boolean isBlocked(){
        return this.isBlocked;
    }

    public String getUsername(){
        return this.username;
    }

    public int getId(){
        return this.id;
    }

}

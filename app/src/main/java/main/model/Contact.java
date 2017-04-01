package main.model;

public class Contact {
    private boolean isBlocked;
    private int id;
    private String username;

    public Contact(int id, String username, boolean isBlocked){
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

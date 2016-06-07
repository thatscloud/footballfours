package com.footballfours.core.user;

import javax.persistence.Column;

public class User
{
    @Column
    private String username;
    
    @Column
    private boolean authenticated = false;
    
    private String password = "password";
            
    public User(String password)
    {
        username = "Admin";
        authenticate(password);
    }
    
    public String getUsername()
    {
        return username;
    }

    public boolean isAuthenticated()
    {
        return authenticated;
    }

    public boolean authenticate(String password)
    {
        authenticated = this.password.equals( password );
        return authenticated;
    }
}

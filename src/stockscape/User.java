/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscape;

import java.io.Serializable;

/**
 *
 * @author bradl
 */
public class User implements Serializable{
    private int userId;
    private int portfolioId;
    private String username;
    private String password;
    
    public User(int userId, int portfolioId, String username, String password) {
        this.userId = userId;
        this.portfolioId = portfolioId;
        this.username = username;
        this.password = password;
    }
    
    public void setUserId(int id){
        userId = id;
    }
    
    public int getUserId(){
        return userId;
    }
    
    public void setPortfolioId(int id){
        portfolioId = id;
    }
    
    public int getPortfolioId(){
        return portfolioId;
    }
    
    public void setUsername(String name){
        username = name;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setPassword(String key){
        password = key;
    }
}

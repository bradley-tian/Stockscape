/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscape;

/**
 *
 * @author bradl
 */
public class Stock {
    private int id;
    private int userId;
    private String stockName;
    private String stockTicker;
    private String stockExchange;
    private double sharePrice;
    private double sharesOwned;
    private double equityCap;
    private String currency;
    
    public Stock (int id, int userId, String stockName, String stockTicker, String stockExchange, double sharePrice, double sharesOwned, double equityCap, String currency){
        this.id = id;
        this.userId = userId;
        this.stockName = stockName;
        this.stockTicker = stockTicker;
        this.stockExchange = stockExchange;
        this.sharePrice = sharePrice;
        this.sharesOwned = sharesOwned;
        this.equityCap = equityCap;
        this.currency = currency;
    }
    
    public void setId (int i){
        id = i;
    }
    
    public int getId(){
        return id;
    }
    
    public void setUserId (int u){
        userId = u;
    }
    
    public int getUserId(){
        return userId;
    }
    
    public void setStockName(String s){
        stockName = s;
    }
    
    public String getStockName(){
        return stockName;
    }
    
    public void setStockTicker(String t){
        stockTicker = t;
    }
    
    public String getStockTicker(){
        return stockTicker;
    }
    
    public void setStockExchange(String e){
        stockExchange = e;
    }
    
    public String getStockExchange(){
        return stockExchange;
    }
    
    public void setSharePrice(double s){
        sharePrice = s;
    }
    
    public double getSharePrice(){
        return sharePrice;
    }
    
    public void setSharesOwned(double s){
        sharesOwned = s;
    }
    
    public double getSharesOwned(){
        return sharesOwned;
    }
    
    public void setEquityCap(double e){
        equityCap = e;
    }
    
    public double getEquityCap(){
        return equityCap;
    }
    
    public void setCurrency(String c){
        currency = c;
    }
    
    public String getCurrency(){
        return currency;
    }
    
}


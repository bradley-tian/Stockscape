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
public class Stockscape {

    public static void main(String[] args) {
        // TODO code application logic here
        Login loginPage = new Login();
        loginPage.setVisible(true);
        HomePage home = new HomePage();
        home.setVisible(false);
    }
    
}

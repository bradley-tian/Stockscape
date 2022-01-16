 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscape;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author bradl
 */
public class HomePage extends javax.swing.JFrame {
    /**
     * Creates new form HomePage
     */
    private Connection con;
    public HomePage() {
        initComponents();
        createTableColumns();
        display_stocks();
        hideFeatures();
        setDefaultCurrencyMultiplier();
        displayInCurrency(setInUsDollar(), "US Dollar");
    }
    
    public void createTableColumns(){
        //Create the default columns of the JTable using DefaultTableModel
        DefaultTableModel model = (DefaultTableModel)portfolioTable.getModel();
        model.addColumn("Stock Name");
        model.addColumn("Ticker");
        model.addColumn("Exchange");
        model.addColumn("Share Price");
        model.addColumn("Shares Owned");
        model.addColumn("Equity Cap.");
        model.addColumn("Currency");
        model.addColumn("In US Dollar");
    }
    
    public void hideFeatures(){
        //Set all warning labels and buttons for different functions invisible upon loading the main page
        deleteWarningLabel.setVisible(false);
        addPositionPanel.setVisible(false);
        btnSavePosition.setVisible(false);
        btnCancel.setVisible(false);
        btnSaveModifiedPosition.setVisible(false);
        btnCancelModifiedPosition.setVisible(false);
    }
    
    public ArrayList<Stock> stocksList(){
        ArrayList<Stock> stockList = new ArrayList();
        //Set up connection with server using JDBC driver
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver");

        } catch (ClassNotFoundException e)
        {
            System.out.println("No JDBC Driver");
            e.printStackTrace();           
        }
        //Using the array list of 'Stock' objects, record all rows of the "Portfolio" database table
        try{
            con = DriverManager.getConnection("jdbc:derby://DESKTOP-SJ8Q2AE:1527/Stockscapedatabase", "ad", "ad");
            String query = "SELECT * FROM Portfolio";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            Stock stock;
            while(rs.next()){
                stock = new Stock(rs.getInt("ID"), rs.getInt("USER_ID"), rs.getString("STOCKNAME"), rs.getString("STOCKTICKER"), rs.getString("STOCKEXCHANGE"), rs.getDouble("SHAREPRICE"), rs.getDouble("SHARESOWNED"), rs.getDouble("EQUITYCAP"), rs.getString("CURRENCY"));
                stockList.add(stock);
            }
        } catch (Exception e) {
            System.out.println("error");
        }
        return stockList;
    }
    
    public void display_stocks(){
        //Get the array list containing "Portfolio" table data
        ArrayList<Stock> stocks = stocksList();
        DefaultTableModel model = (DefaultTableModel)portfolioTable.getModel();
        //Clears any residual or unnecessary rows
        model.setRowCount(0);
        Object[] row = new Object[7];
        //Using object[] array, set the value for each row on the JTable
        for(int i = 0; i < stocks.size(); i++){
            row[0] = stocks.get(i).getStockName();
            row[1] = stocks.get(i).getStockTicker();
            row[2] = stocks.get(i).getStockExchange();
            row[3] = stocks.get(i).getSharePrice();
            row[4] = stocks.get(i).getSharesOwned();
            row[5] = stocks.get(i).getEquityCap();
            row[6] = stocks.get(i).getCurrency();
            model.addRow(row);
        }
    }
    
    public void setDefaultCurrencyMultiplier(){
        //Set the default currency rates in the text fields
        txtDollarToYuan.setText("7.00");
        txtDollarToEuro.setText("0.90");
    }
    
    public ArrayList<Double> setInUsDollar(){
        ArrayList<Stock> stocks = stocksList();
        ArrayList<Double> equityUsDollar = new ArrayList<>();
        
        try{
        //Convert the inputs in the text fields to doubles
        double chineseYuanExchange = Double.valueOf(txtDollarToYuan.getText());
        double euroExchange = Double.valueOf(txtDollarToEuro.getText());
        for (int i = 0; i < stocks.size(); i++){
        //if the stocks are of a different currency, convert their equity amounts using the appropriate convertion rates
            if(stocks.get(i).getCurrency().equals("Chinese Yuan")){
                stocks.get(i).setEquityCap(Math.round((stocks.get(i).getEquityCap() / chineseYuanExchange)*100.0)/100.0);
            }else if(stocks.get(i).getCurrency().equals("Euro")){
                stocks.get(i).setEquityCap(Math.round((stocks.get(i).getEquityCap() / euroExchange)*100.0)/100.0);
            }
        }
        currencyWarningLabel.setVisible(false);
        }catch(NumberFormatException ignore){
            currencyWarningLabel.setVisible(true);
        }
        //Store the new data in an array list of double for display purposes
        for (int i = 0; i < stocks.size(); i++){
            equityUsDollar.add(stocks.get(i).getEquityCap());
        }
        return equityUsDollar;
    }
    
    public ArrayList<Double> setInChineseYuan(){
        ArrayList<Stock> stocks = stocksList();
        ArrayList<Double> equityChineseYuan = new ArrayList<>();
        try{
        //Convert the inputs in the text fields to doubles
        double chineseYuanExchange = Double.valueOf(txtDollarToYuan.getText());
        double euroExchange = Double.valueOf(txtDollarToEuro.getText());
        //if the stocks are of a different currency, convert their equity amounts using the appropriate convertion rates
        for (int i = 0; i < stocks.size(); i++){
            if(stocks.get(i).getCurrency().equals("US Dollar")){
                stocks.get(i).setEquityCap(Math.round((stocks.get(i).getEquityCap() * (1* chineseYuanExchange))*100.0)/100.0);
            }else if(stocks.get(i).getCurrency().equals("Euro")){
                stocks.get(i).setEquityCap(Math.round((stocks.get(i).getEquityCap() * (1 * (chineseYuanExchange / euroExchange)))*100.0)/100.0);
            }
        }
        currencyWarningLabel.setVisible(false);
        }catch(NumberFormatException ignore){
            currencyWarningLabel.setVisible(true);
        }
        //Store the new data in an array list of double for display purposes
        for (int i = 0; i < stocks.size(); i++){
            equityChineseYuan.add(stocks.get(i).getEquityCap());
        }
        return equityChineseYuan;
    }
    
    public ArrayList<Double> setInEuro(){
        ArrayList<Stock> stocks = stocksList();
        ArrayList<Double> equityEuro = new ArrayList<>();
        try{
        //Convert the inputs in the text fields to doubles
        double chineseYuanExchange = Double.valueOf(txtDollarToYuan.getText());
        double euroExchange = Double.valueOf(txtDollarToEuro.getText());
        //if the stocks are of a different currency, convert their equity amounts using the appropriate convertion rates
        for (int i = 0; i < stocks.size(); i++){
            if(stocks.get(i).getCurrency().equals("US Dollar")){
                stocks.get(i).setEquityCap(Math.round((stocks.get(i).getEquityCap() * (1*euroExchange))*100.0)/100.0);
            }else if(stocks.get(i).getCurrency().equals("Chinese Yuan")){
                stocks.get(i).setEquityCap(Math.round((stocks.get(i).getEquityCap() * ((1/euroExchange)*chineseYuanExchange)*100.0)/100.0));
            }
        }
        currencyWarningLabel.setVisible(false);
        }catch(NumberFormatException ignore){
            currencyWarningLabel.setVisible(true);
        }
        //Store the new data in an array list of double for display purposes
        for (int i = 0; i < stocks.size(); i++){
            equityEuro.add(stocks.get(i).getEquityCap());
        }
        return equityEuro;
    }
    
    public void displayInCurrency (ArrayList<Double> equityList, String currency){
        DefaultTableModel model = (DefaultTableModel)portfolioTable.getModel();
        //Clear residual custom currency column
        model.setColumnCount(7);
        //Add in a new column that I am sure matches the latest updated stock positions
        model.addColumn("In " + currency);
        //Set the value for each of the table boxes using the data from the array list of doubles
        for(int i = 0; i < equityList.size(); i++){
            model.setValueAt(equityList.get(i), i, 7);
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        StockscapePUEntityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("StockscapePU").createEntityManager();
        portfolioQuery = java.beans.Beans.isDesignTime() ? null : StockscapePUEntityManager.createQuery("SELECT p FROM Portfolio p");
        portfolioList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : portfolioQuery.getResultList();
        welcomeLabel = new javax.swing.JLabel();
        portfolioLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        portfolioTable = new javax.swing.JTable();
        btnAddPosition = new javax.swing.JButton();
        btnRemovePosition = new javax.swing.JButton();
        btnChineseYuan = new javax.swing.JButton();
        btnUsDollar = new javax.swing.JButton();
        btnEuro = new javax.swing.JButton();
        viewLabel = new javax.swing.JLabel();
        addPositionPanel = new javax.swing.JPanel();
        newPositionLabel = new javax.swing.JLabel();
        stockNameLabel = new javax.swing.JLabel();
        stockTickerLabel = new javax.swing.JLabel();
        exchangeLabel = new javax.swing.JLabel();
        sharePriceLabel = new javax.swing.JLabel();
        currencyLabel = new javax.swing.JLabel();
        sharesOwnedLabel = new javax.swing.JLabel();
        txtStockName = new javax.swing.JTextField();
        txtStockTicker = new javax.swing.JTextField();
        txtStockExchange = new javax.swing.JTextField();
        txtSharePrice = new javax.swing.JTextField();
        txtSharesOwned = new javax.swing.JTextField();
        btnCancel = new javax.swing.JButton();
        btnSavePosition = new javax.swing.JButton();
        positionWarningLabel = new javax.swing.JLabel();
        btnSetChineseYuan = new javax.swing.JButton();
        btnSetUsDollar = new javax.swing.JButton();
        btnSetEuro = new javax.swing.JButton();
        selectedCurrencyLabel = new javax.swing.JLabel();
        btnSaveModifiedPosition = new javax.swing.JButton();
        btnCancelModifiedPosition = new javax.swing.JButton();
        btnModifyPosition = new javax.swing.JButton();
        deleteWarningLabel = new javax.swing.JLabel();
        exchangeRateLabel1 = new javax.swing.JLabel();
        exchangeRateLabel2 = new javax.swing.JLabel();
        dollarToYuanLabel = new javax.swing.JLabel();
        txtDollarToYuan = new javax.swing.JTextField();
        dollarToEuroLabel1 = new javax.swing.JLabel();
        txtDollarToEuro = new javax.swing.JTextField();
        currencyWarningLabel = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        welcomeLabel.setFont(new java.awt.Font("Century Gothic", 0, 36)); // NOI18N
        welcomeLabel.setText("Welcome to Stockscape!");

        portfolioLabel.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        portfolioLabel.setText("Your Portfolio");

        portfolioTable.setModel(portfolioTable.getModel());
        portfolioTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                portfolioTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(portfolioTable);

        btnAddPosition.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        btnAddPosition.setText("Add Position");
        btnAddPosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddPositionMouseClicked(evt);
            }
        });
        btnAddPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPositionActionPerformed(evt);
            }
        });

        btnRemovePosition.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        btnRemovePosition.setText("Remove Position");
        btnRemovePosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemovePositionMouseClicked(evt);
            }
        });
        btnRemovePosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePositionActionPerformed(evt);
            }
        });

        btnChineseYuan.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        btnChineseYuan.setText("Chinese Yuan");
        btnChineseYuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChineseYuanMouseClicked(evt);
            }
        });
        btnChineseYuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChineseYuanActionPerformed(evt);
            }
        });

        btnUsDollar.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        btnUsDollar.setText("US Dollar");
        btnUsDollar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUsDollarMouseClicked(evt);
            }
        });
        btnUsDollar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsDollarActionPerformed(evt);
            }
        });

        btnEuro.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        btnEuro.setText("Euro");
        btnEuro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEuroMouseClicked(evt);
            }
        });
        btnEuro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEuroActionPerformed(evt);
            }
        });

        viewLabel.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        viewLabel.setText("View your positions in:");

        newPositionLabel.setFont(new java.awt.Font("Century Gothic", 0, 24)); // NOI18N
        newPositionLabel.setText("Add/Modify Position");

        stockNameLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        stockNameLabel.setText("Stock Name:");

        stockTickerLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        stockTickerLabel.setText("Ticker:");

        exchangeLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        exchangeLabel.setText("Exchange:");

        sharePriceLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        sharePriceLabel.setText("Share Price:");

        currencyLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        currencyLabel.setText("Currency:");

        sharesOwnedLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        sharesOwnedLabel.setText("Shares Owned:");

        btnCancel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSavePosition.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        btnSavePosition.setText("Save");
        btnSavePosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSavePositionMouseClicked(evt);
            }
        });

        positionWarningLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        positionWarningLabel.setText("Please fill out the required information.");

        btnSetChineseYuan.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        btnSetChineseYuan.setText("Chinese Yuan");
        btnSetChineseYuan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSetChineseYuanMouseClicked(evt);
            }
        });

        btnSetUsDollar.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        btnSetUsDollar.setText("US Dollar");
        btnSetUsDollar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSetUsDollarMouseClicked(evt);
            }
        });

        btnSetEuro.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        btnSetEuro.setText("Euro");
        btnSetEuro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSetEuroMouseClicked(evt);
            }
        });
        btnSetEuro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetEuroActionPerformed(evt);
            }
        });

        selectedCurrencyLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        selectedCurrencyLabel.setText("-- ");

        btnSaveModifiedPosition.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        btnSaveModifiedPosition.setText("Save");
        btnSaveModifiedPosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveModifiedPositionMouseClicked(evt);
            }
        });

        btnCancelModifiedPosition.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        btnCancelModifiedPosition.setText("Cancel");
        btnCancelModifiedPosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelModifiedPositionMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout addPositionPanelLayout = new javax.swing.GroupLayout(addPositionPanel);
        addPositionPanel.setLayout(addPositionPanelLayout);
        addPositionPanelLayout.setHorizontalGroup(
            addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPositionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(addPositionPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSaveModifiedPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(newPositionLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addPositionPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addPositionPanelLayout.createSequentialGroup()
                                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(stockNameLabel)
                                    .addComponent(stockTickerLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStockTicker, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtStockName, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(addPositionPanelLayout.createSequentialGroup()
                                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(exchangeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(currencyLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStockExchange, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(addPositionPanelLayout.createSequentialGroup()
                                        .addComponent(btnSetChineseYuan)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSetUsDollar)))))
                        .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addPositionPanelLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(addPositionPanelLayout.createSequentialGroup()
                                        .addComponent(sharePriceLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtSharePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(addPositionPanelLayout.createSequentialGroup()
                                        .addComponent(sharesOwnedLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtSharesOwned, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(addPositionPanelLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(btnSetEuro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectedCurrencyLabel))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addPositionPanelLayout.createSequentialGroup()
                        .addComponent(positionWarningLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 452, Short.MAX_VALUE)
                        .addComponent(btnSavePosition, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel)
                    .addComponent(btnCancelModifiedPosition)))
        );
        addPositionPanelLayout.setVerticalGroup(
            addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPositionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newPositionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stockNameLabel)
                    .addComponent(sharePriceLabel)
                    .addComponent(txtStockName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSharePrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stockTickerLabel)
                    .addComponent(sharesOwnedLabel)
                    .addComponent(txtStockTicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSharesOwned, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exchangeLabel)
                    .addComponent(txtStockExchange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currencyLabel)
                    .addComponent(btnSetChineseYuan)
                    .addComponent(btnSetUsDollar)
                    .addComponent(btnSetEuro)
                    .addComponent(selectedCurrencyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCancel)
                        .addComponent(btnSavePosition))
                    .addComponent(positionWarningLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPositionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveModifiedPosition)
                    .addComponent(btnCancelModifiedPosition))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        btnModifyPosition.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        btnModifyPosition.setText("Modify Position");
        btnModifyPosition.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnModifyPositionMouseClicked(evt);
            }
        });
        btnModifyPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyPositionActionPerformed(evt);
            }
        });

        deleteWarningLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        deleteWarningLabel.setText("Please highlight a row properly.");

        exchangeRateLabel1.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        exchangeRateLabel1.setText("Exchange Rate:");

        exchangeRateLabel2.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        exchangeRateLabel2.setText("Exchange Rate:");

        dollarToYuanLabel.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        dollarToYuanLabel.setText("$1 = ¥");

        txtDollarToYuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDollarToYuanActionPerformed(evt);
            }
        });

        dollarToEuroLabel1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        dollarToEuroLabel1.setText("$1 = £");

        txtDollarToEuro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDollarToEuroActionPerformed(evt);
            }
        });

        currencyWarningLabel.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        currencyWarningLabel.setText("Please enter a valid exchange rate.");

        btnExit.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        btnExit.setText("Exit");
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExitMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(747, 747, 747)
                        .addComponent(deleteWarningLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(addPositionPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnAddPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModifyPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(btnRemovePosition))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(welcomeLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(portfolioLabel, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(viewLabel)
                            .addComponent(exchangeRateLabel1)
                            .addComponent(exchangeRateLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnUsDollar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnChineseYuan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEuro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(dollarToEuroLabel1)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtDollarToEuro))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(dollarToYuanLabel)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtDollarToYuan)))))
                            .addComponent(currencyWarningLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeLabel)
                .addGap(18, 18, 18)
                .addComponent(portfolioLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(viewLabel)
                        .addGap(27, 27, 27)
                        .addComponent(btnUsDollar)
                        .addGap(64, 64, 64)
                        .addComponent(btnChineseYuan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exchangeRateLabel1)
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dollarToYuanLabel)
                            .addComponent(txtDollarToYuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnEuro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exchangeRateLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dollarToEuroLabel1)
                            .addComponent(txtDollarToEuro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currencyWarningLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddPosition)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRemovePosition)
                        .addComponent(btnModifyPosition)))
                .addGap(12, 12, 12)
                .addComponent(deleteWarningLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addPositionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(btnExit)))
                .addContainerGap(290, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddPositionActionPerformed

    private void btnRemovePositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRemovePositionActionPerformed

    private void btnChineseYuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChineseYuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnChineseYuanActionPerformed

    private void btnUsDollarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsDollarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUsDollarActionPerformed

    private void btnEuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEuroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEuroActionPerformed

    private void btnAddPositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddPositionMouseClicked
        // TODO add your handling code here:
        hideFeatures();
        addPositionPanel.setVisible(true);
        btnSavePosition.setVisible(true);
        btnCancel.setVisible(true);
        txtStockName.setText("");
        txtStockTicker.setText("");
        txtStockExchange.setText("");
        txtSharePrice.setText("");
        txtSharesOwned.setText("");
    }//GEN-LAST:event_btnAddPositionMouseClicked

    private void btnSavePositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSavePositionMouseClicked
        // TODO add your handling code here:
        double sharePrice = 0;
        double sharesOwned = 0;
        try{
            //Prepare a query command containing the input inserted by the user
            sharePrice = Double.valueOf(txtSharePrice.getText());
            sharesOwned = Double.valueOf(txtSharesOwned.getText());
            String query = "INSERT INTO Portfolio (USER_ID, STOCKNAME, STOCKTICKER, STOCKEXCHANGE, SHAREPRICE, SHARESOWNED, EQUITYCAP, CURRENCY) VALUES (?,?,?,?,?,?,?,?)";
        try {
            //Further prepare the values of the command using user inputs
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, 10001);
            st.setString(2, txtStockName.getText());
            st.setString(3, txtStockTicker.getText());
            st.setString(4, txtStockExchange.getText());
            st.setDouble(5, sharePrice);
            st.setDouble(6, sharesOwned);
            st.setDouble(7, Math.round((sharePrice * sharesOwned)*100.0)/100.0);
            st.setString(8, selectedCurrencyLabel.getText());
            st.executeUpdate();
            display_stocks();
            hideFeatures();
        } catch (SQLException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }
        }catch(NumberFormatException ignore){
            positionWarningLabel.setText("Please ensure all fields are filled out properly.");
        }
    }//GEN-LAST:event_btnSavePositionMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        hideFeatures();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void btnSetEuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetEuroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSetEuroActionPerformed

    private void btnSetChineseYuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSetChineseYuanMouseClicked
        // TODO add your handling code here:
        selectedCurrencyLabel.setText("Chinese Yuan");
    }//GEN-LAST:event_btnSetChineseYuanMouseClicked

    private void btnSetUsDollarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSetUsDollarMouseClicked
        // TODO add your handling code here:
        selectedCurrencyLabel.setText("US Dollar");
    }//GEN-LAST:event_btnSetUsDollarMouseClicked

    private void btnSetEuroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSetEuroMouseClicked
        // TODO add your handling code here:
        selectedCurrencyLabel.setText("Euro");
    }//GEN-LAST:event_btnSetEuroMouseClicked

    private void btnModifyPositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnModifyPositionMouseClicked
        // TODO add your handling code here:
        hideFeatures();
        btnSaveModifiedPosition.setVisible(true);
        btnCancelModifiedPosition.setVisible(true);
        ArrayList<Stock> stocks = stocksList();
        addPositionPanel.setVisible(true);
        try{
        //Get what the user has entered before from the JTable and automatically fill them out the in corresponding text fields
        txtStockName.setText(stocks.get(portfolioTable.getSelectedRow()).getStockName());
        txtStockTicker.setText(stocks.get(portfolioTable.getSelectedRow()).getStockTicker());
        txtStockExchange.setText(stocks.get(portfolioTable.getSelectedRow()).getStockExchange());
        txtSharePrice.setText(Double.toString(stocks.get(portfolioTable.getSelectedRow()).getSharePrice()));
        txtSharesOwned.setText(Double.toString(stocks.get(portfolioTable.getSelectedRow()).getSharesOwned()));
        deleteWarningLabel.setVisible(false);
        }catch(ArrayIndexOutOfBoundsException e){
            deleteWarningLabel.setVisible(true);
            addPositionPanel.setVisible(false);
        }
    }//GEN-LAST:event_btnModifyPositionMouseClicked

    private void btnModifyPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifyPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnModifyPositionActionPerformed

    private void btnRemovePositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemovePositionMouseClicked
        // TODO add your handling code here:
        int row = portfolioTable.getSelectedRow();
        ArrayList<Stock> stocks = stocksList();
        int selectedId;
        try{
        //Set up a delete query command
            selectedId = stocks.get(row).getId();
            String query = ("DELETE FROM PORTFOLIO WHERE ID =" + (selectedId));
            try {
            Statement st = con.createStatement();
            st.executeUpdate(query);
            deleteWarningLabel.setVisible(false);
        } catch (SQLException ex) {
            ex.printStackTrace();
            deleteWarningLabel.setVisible(true);
            }
        }catch(ArrayIndexOutOfBoundsException e){
            deleteWarningLabel.setVisible(true);
        }
        display_stocks();
    }//GEN-LAST:event_btnRemovePositionMouseClicked

    private void btnSaveModifiedPositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveModifiedPositionMouseClicked
        // TODO add your handling code here:
        double sharePrice = 0;
        double sharesOwned = 0;
        int row = portfolioTable.getSelectedRow();
        ArrayList<Stock> stocks = stocksList();
        int selectedId = -1;
        try{
            sharePrice = Double.valueOf(txtSharePrice.getText());
            sharesOwned = Double.valueOf(txtSharesOwned.getText());
            //Get the corresponding row in the database by tracing the corresponding ID
            selectedId = stocks.get(row).getId();
            String query = ("UPDATE Portfolio SET USER_ID = ?, STOCKNAME = ?, STOCKTICKER = ?, STOCKEXCHANGE = ?, SHAREPRICE = ?, SHARESOWNED = ?, EQUITYCAP = ?, CURRENCY = ? WHERE ID = " + selectedId);
        try {
            //Prepare update statement (different from the "add position" function, which uses an INSERT command)
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, 10001);
            st.setString(2, txtStockName.getText());
            st.setString(3, txtStockTicker.getText());
            st.setString(4, txtStockExchange.getText());
            st.setDouble(5, sharePrice);
            st.setDouble(6, sharesOwned);
            st.setDouble(7, Math.round((sharePrice * sharesOwned)*100.0)/100.0);
            st.setString(8, selectedCurrencyLabel.getText());
            st.executeUpdate();
            hideFeatures();
        } catch (SQLException ex) {
            positionWarningLabel.setText("Please ensure all fields are filled out properly.");
        }
        }catch(NumberFormatException ignore){
            positionWarningLabel.setText("Please ensure all fields are filled out properly.");
        }catch(ArrayIndexOutOfBoundsException e){
            deleteWarningLabel.setVisible(true);
        }
        display_stocks();
    }//GEN-LAST:event_btnSaveModifiedPositionMouseClicked

    private void btnCancelModifiedPositionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelModifiedPositionMouseClicked
        // TODO add your handling code here:
        hideFeatures();
    }//GEN-LAST:event_btnCancelModifiedPositionMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtDollarToYuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDollarToYuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDollarToYuanActionPerformed

    private void txtDollarToEuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDollarToEuroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDollarToEuroActionPerformed

    private void btnUsDollarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUsDollarMouseClicked
        // TODO add your handling code here:
        displayInCurrency(setInUsDollar(), "US Dollar");
    }//GEN-LAST:event_btnUsDollarMouseClicked

    private void btnChineseYuanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChineseYuanMouseClicked
        // TODO add your handling code here:
        displayInCurrency(setInChineseYuan(), "Chinese Yuan");
    }//GEN-LAST:event_btnChineseYuanMouseClicked

    private void btnEuroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEuroMouseClicked
        // TODO add your handling code here:
        displayInCurrency(setInEuro(), "Euro");
    }//GEN-LAST:event_btnEuroMouseClicked

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnExitMouseClicked

    private void portfolioTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_portfolioTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_portfolioTableMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.persistence.EntityManager StockscapePUEntityManager;
    private javax.swing.JPanel addPositionPanel;
    private javax.swing.JButton btnAddPosition;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelModifiedPosition;
    private javax.swing.JButton btnChineseYuan;
    private javax.swing.JButton btnEuro;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnModifyPosition;
    private javax.swing.JButton btnRemovePosition;
    private javax.swing.JButton btnSaveModifiedPosition;
    private javax.swing.JButton btnSavePosition;
    private javax.swing.JButton btnSetChineseYuan;
    private javax.swing.JButton btnSetEuro;
    private javax.swing.JButton btnSetUsDollar;
    private javax.swing.JButton btnUsDollar;
    private javax.swing.JLabel currencyLabel;
    private javax.swing.JLabel currencyWarningLabel;
    private javax.swing.JLabel deleteWarningLabel;
    private javax.swing.JLabel dollarToEuroLabel1;
    private javax.swing.JLabel dollarToYuanLabel;
    private javax.swing.JLabel exchangeLabel;
    private javax.swing.JLabel exchangeRateLabel1;
    private javax.swing.JLabel exchangeRateLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel newPositionLabel;
    private javax.swing.JLabel portfolioLabel;
    private java.util.List<entity.Portfolio> portfolioList;
    private javax.persistence.Query portfolioQuery;
    private javax.swing.JTable portfolioTable;
    private javax.swing.JLabel positionWarningLabel;
    private javax.swing.JLabel selectedCurrencyLabel;
    private javax.swing.JLabel sharePriceLabel;
    private javax.swing.JLabel sharesOwnedLabel;
    private javax.swing.JLabel stockNameLabel;
    private javax.swing.JLabel stockTickerLabel;
    private javax.swing.JTextField txtDollarToEuro;
    private javax.swing.JTextField txtDollarToYuan;
    private javax.swing.JTextField txtSharePrice;
    private javax.swing.JTextField txtSharesOwned;
    private javax.swing.JTextField txtStockExchange;
    private javax.swing.JTextField txtStockName;
    private javax.swing.JTextField txtStockTicker;
    private javax.swing.JLabel viewLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}

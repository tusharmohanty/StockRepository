package stocks.ui.components.panels;

import layout.TableLayout;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class BottomPortfolioMenuPanel extends JPanel implements ActionListener {
    JLabel stockName,stockCode,exchange;
    JTextField stockNameText,stockCodeText;
    JComboBox stockCodeCombo,exchangeCombo;
    JButton addWatchList,addStocks;

    public static final BottomPortfolioMenuPanel INSTANCE = new BottomPortfolioMenuPanel();

    private BottomPortfolioMenuPanel(){
        stockName = new JLabel("Stock Name");
        stockCode = new JLabel("Stock Code");
        stockCode = new JLabel("Stock Code");
        exchange = new JLabel("Exchange");
        stockNameText = new JTextField();
        stockCodeText = new JTextField();
        exchangeCombo = new JComboBox(new String[]{"NSE","BSE"});
        try {
            stockCodeCombo =new JComboBox<>(DataAccess.INSTANCE.getStockList().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        addWatchList = new JButton("Add Watchlist");
        addStocks = new JButton("Add Stocks");
        addStocks.addActionListener(this);
        addWatchList.addActionListener(this);
        double size[][] =
                {{0.11,0.08,0.06,0.1,0.65},
                        {0.25,0.25,0.25}};
        this.setLayout(new TableLayout(size));

        this.add(stockNameText,"0,0,0,0");
        this.add(stockCodeText,"1,0,1,0");
        this.add(exchangeCombo,"2,0,2,0");
        this.add(addStocks,"3,0,3,0");

        this.add(stockCodeCombo,"0,1,2,1");
        this.add(addWatchList,"3,1,3,1");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Add Stocks")) {
            try {
                DataAccess.INSTANCE.saveStocks(stockCodeText.getText(),stockNameText.getText(),exchangeCombo.getSelectedItem().toString());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                stockCodeCombo =new JComboBox(DataAccess.INSTANCE.getStockList().toArray());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Add Watchlist")) {
            try {
                DataAccess.INSTANCE.addWatchlist(((StocksBean)stockCodeCombo.getSelectedItem()).getStockCode());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

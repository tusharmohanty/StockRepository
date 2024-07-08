package stocks.ui.components.panels;

import layout.TableLayout;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.model.data.SyncData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BottomPortfolioMenuPanel extends JPanel implements ActionListener {
    JLabel stockName,stockCode,exchange;
    JTextField stockNameText,stockCodeText;
    JComboBox stockCodeCombo,exchangeCombo,watchlistStockCombo;
    JButton addWatchList,addStocks,removeWatchList,syncData;

    public static final BottomPortfolioMenuPanel INSTANCE = new BottomPortfolioMenuPanel();

    private BottomPortfolioMenuPanel(){
        stockName = new JLabel("Stock Name");
        stockCode = new JLabel("Stock Code");
        exchange = new JLabel("Exchange");
        stockNameText = new JTextField();
        stockCodeText = new JTextField();
        exchangeCombo = new JComboBox(new String[]{"NSE","BSE"});
        try {
            stockCodeCombo =new JComboBox<>(DataAccess.INSTANCE.getStockList().toArray());
            watchlistStockCombo = new JComboBox<>(DataAccess.INSTANCE.getWatchlistStocks().toArray());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        addWatchList = new JButton("Add Watchlist");
        addStocks = new JButton("Add Stocks");
        removeWatchList = new JButton("Remove Watchlist");
        syncData = new JButton("Sync");
        addStocks.addActionListener(this);
        addWatchList.addActionListener(this);
        removeWatchList.addActionListener(this);
        syncData.addActionListener(this);
        double size[][] =
                {{0.11,0.08,0.125,0.25,0.36},
                        {0.25,0.25,0.25,0.25}};
        this.setLayout(new TableLayout(size));

        this.add(stockNameText,"0,0,0,0");
        this.add(stockCodeText,"1,0,1,0");
        this.add(exchangeCombo,"2,0,2,0");
        this.add(addStocks,"3,0,3,0");

        this.add(stockCodeCombo,"0,1,2,1");
        this.add(addWatchList,"3,1,3,1");

        this.add(watchlistStockCombo,"0,2,2,0");
        this.add(removeWatchList,"3,2,3,2");

        this.add(syncData,"3,3,3,3");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Add Stocks")) {
            try {
                DataAccess.INSTANCE.saveStocks(stockCodeText.getText(),stockNameText.getText(),exchangeCombo.getSelectedItem().toString());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            reloadStockList();
        }
        else if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Add Watchlist")) {
            try {
                DataAccess.INSTANCE.addUpdateWatchlist(((StocksBean)stockCodeCombo.getSelectedItem()).getStockCode(),true);
                PortfolioPanel.INSTANCE.portfolioData = DataAccess.INSTANCE.getPortfolioData();
                PortfolioPanel.INSTANCE.tableModel.fireTableDataChanged();
                reloadStockWatchList();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Remove Watchlist")) {
            try {
                DataAccess.INSTANCE.addUpdateWatchlist(((StocksBean)watchlistStockCombo.getSelectedItem()).getStockCode(),false);
                PortfolioPanel.INSTANCE.portfolioData = DataAccess.INSTANCE.getPortfolioData();
                try {
                    PortfolioPanel.INSTANCE.tableModel.fireTableDataChanged();
                }
                catch(Exception E){
                    E.printStackTrace();
                }
                reloadStockWatchList();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Sync")) {
            SyncData sync = new SyncData();

            try {
                sync.syncData();
                ConsolidatedPortfolio.INSTANCE.refreshData(DataAccess.INSTANCE.getConsolidatedPortfolio());
                PortfolioPanel.INSTANCE.portfolioData = DataAccess.INSTANCE.getPortfolioData();
                try {
                    PortfolioPanel.INSTANCE.tableModel.fireTableDataChanged();
                }
                catch(Exception E){
                    E.printStackTrace();
                }
            } catch (IOException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void reloadStockList(){
        try {
            stockCodeCombo.removeAllItems();
            List<StocksBean> stockList = DataAccess.INSTANCE.getStockList();
            for(int tempCount =0;tempCount < stockList.size();tempCount++){
                stockCodeCombo.addItem(stockList.get(tempCount));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void reloadStockWatchList(){
        try {
            watchlistStockCombo.removeAllItems();
            List<StocksBean> stockList = DataAccess.INSTANCE.getWatchlistStocks();
            for(int tempCount =0;tempCount < stockList.size();tempCount++){
                watchlistStockCombo.addItem(stockList.get(tempCount));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

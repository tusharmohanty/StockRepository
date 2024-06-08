package stocks.ui.components.panels;

import layout.TableLayout;
import stocks.model.beans.ConsolidatedPortfolioBean;
import stocks.model.data.DataAccess;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ConsolidatedPortfolio extends JPanel {
    Color green =new Color(76,153,0);
    public static ConsolidatedPortfolio INSTANCE = new ConsolidatedPortfolio();

    JLabel totalInvestments = new JLabel();
    JLabel totalInvestmentLabel =           new JLabel("Total Investments :");
    JLabel totalProfitLossPercentageLabel = new JLabel("Total P/L %            :");
    JLabel totalProfitLossAmountLabel = new JLabel("Total P/L               :");
    JLabel netPortFolioLabel = new JLabel("Net Portfolio         :");
    JLabel totalProfitLossPercentage = new JLabel();

    JLabel totalProfitLossAmount = new JLabel();

    JLabel netPortFolio = new JLabel();
    private ConsolidatedPortfolio(){
        double size[][] =
                {{0.01,0.25,0.74},
                        {0.25,0.25,0.25,0.25}};
        this.setLayout(new TableLayout(size));

        this.add(totalInvestmentLabel,    "1,0,1,0");
        this.add(totalProfitLossPercentageLabel,"1,1,1,1");
        this.add(totalProfitLossAmountLabel,"1,2,1,2");
        this.add(netPortFolioLabel,"1,3,1,3");

        this.add(totalInvestments,    "2,0,2,0");
        this.add(totalProfitLossPercentage,"2,1,2,1");
        this.add(totalProfitLossAmount,"2,2,2,2");
        this.add(netPortFolio,"2,3,2,3");
        ConsolidatedPortfolioBean bean = null;
        try {
            bean = DataAccess.INSTANCE.getConsolidatedPortfolio();
            refreshData(bean);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void  refreshData(ConsolidatedPortfolioBean bean){
        totalInvestments.setText(bean.getTotalInvestments() +"");
        netPortFolio.setText(bean.getTotalPortfolio()+"");
        totalProfitLossAmount.setText(bean.getProfitLoss()+ "");
        totalProfitLossPercentage.setText(bean.getProfitLossPercentage() +"");
        if(bean.getProfitLoss() >0){
            netPortFolio.setForeground(green);
            totalProfitLossAmount.setForeground(green);
            totalProfitLossPercentage.setForeground(green);
        }

    }
}

package stocks.ui.components.panels;

import layout.TableLayout;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.model.data.SyncData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class ShortListSignalsPanel extends JPanel implements ActionListener {
    JLabel stopLossLabel,qtyLabel,priceLabel,totalLabel,riskLabel,riskPercentageLabel;
    JTextField stopLoss,qty,price,total,risk,riskPercentage;
    JButton shortlist;

    public static final ShortListSignalsPanel INSTANCE = new ShortListSignalsPanel();

    private ShortListSignalsPanel(){
        priceLabel = new JLabel("Price:");
        price= new JTextField();
        price.setEditable(false);
        stopLossLabel = new JLabel("Stop Loss");
        stopLoss = new JTextField();
        qtyLabel = new JLabel("Qty");
        qty = new JTextField();
        totalLabel = new JLabel("Total");
        total = new JTextField();
        total.setEditable(false);
        riskLabel = new JLabel("Risk");
        risk = new JTextField();
        risk.setEditable(false);
        riskPercentageLabel = new JLabel("Risk%");
        riskPercentage = new JTextField();
        riskPercentage.setEditable(false);
        shortlist = new JButton("ShortList");
        double size[][] =
                {{0.1,0.15,0.05,0.08,0.08,0.1,0.44},
                        {0.25,0.25,0.25,0.25}};
        this.setLayout(new TableLayout(size));
        this.add(priceLabel,"0,0,0,0");
        this.add(price,"1,0,1,0");
        this.add(qtyLabel,"2,0,2,0");
        this.add(qty,"3,0,3,0");
        this.add(totalLabel,"4,0,4,0");
        this.add(total,"5,0,5,0");
        this.add(stopLossLabel,"0,1,0,1");
        this.add(stopLoss,"1,1,1,1");
        this.add(riskLabel,"2,1,2,1");
        this.add(risk,"3,1,3,1");
        this.add(riskPercentageLabel,"4,1,4,1");
        this.add(riskPercentage,"5,1,5,1");
       //this.add(shortlist,"4,0,4,0");
        qty.addActionListener(this);
        qty.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                int quantity = Integer.parseInt(qty.getText());
                total.setText(BigDecimal.valueOf(Double.parseDouble(price.getText())).multiply(BigDecimal.valueOf(quantity)).toString());
            }
        });

        stopLoss.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                int quantity = Integer.parseInt(qty.getText());
                BigDecimal dStopLoss = BigDecimal.valueOf(Double.parseDouble(stopLoss.getText()));
                BigDecimal dPrice =BigDecimal.valueOf(Double.parseDouble(price.getText()));
                BigDecimal dRisk =dPrice.subtract(dStopLoss);
                risk.setText(dRisk.toString());
                riskPercentage.setText((dRisk.divide(dPrice,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100l)).toString()));
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("asdasd");
        if(e.getSource() instanceof JTextField  && e.getActionCommand().equals("qty")) {
            int quantity = Integer.parseInt(qty.getText());
            total.setText(BigDecimal.valueOf(Double.parseDouble(price.getText())).multiply(BigDecimal.valueOf(quantity)).toString());
        }
    }
}

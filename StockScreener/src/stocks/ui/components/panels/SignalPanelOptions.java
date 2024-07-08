package stocks.ui.components.panels;

import layout.TableLayout;
import stocks.model.StockConstants;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.model.data.SyncData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SignalPanelOptions extends JPanel implements ActionListener {
    JCheckBox weekHigh4,ma200,volume;
    public static final SignalPanelOptions INSTANCE = new SignalPanelOptions();

    private SignalPanelOptions(){
        weekHigh4 = new JCheckBox(StockConstants.WEEK_HIGH_4);
        ma200 = new JCheckBox("200 MA");
        volume = new JCheckBox("Vol");
        weekHigh4.addActionListener(this);
        ma200.addActionListener(this);
        volume.addActionListener(this);
        double size[][] =
                {{0.33,0.33,0.33},
                        {0.5,0.5}};
        this.setLayout(new TableLayout(size));

        this.add(weekHigh4,"0,0,0,0");
        this.add(ma200,"1,0,1,0");
        this.add(volume,"2,0,2,0");
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

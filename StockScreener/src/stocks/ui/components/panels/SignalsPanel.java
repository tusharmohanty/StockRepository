package stocks.ui.components.panels;

import stocks.model.beans.SignalsBean;
import stocks.model.data.DataAccess;
import stocks.ui.components.charts.MainPriceChart;
import stocks.ui.components.tableModel.PortfolioTableModel;
import stocks.ui.components.tableModel.SignalsTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static stocks.util.Utils.openScreener;

public class SignalsPanel extends JPanel implements ActionListener {
    public static final SignalsPanel INSTANCE = new SignalsPanel();
    public  List<SignalsBean> signals = new ArrayList<SignalsBean>();
    public SignalsTableModel tableModel;
    JScrollPane scrollPane = null;
    JTable table = null;
    private SignalsPanel(){
        this.add(SignalPanelOptions.INSTANCE);
        try {
            signals =DataAccess.INSTANCE.getSignals();
            for(int tempCount =0; tempCount < signals.size();tempCount ++){
                SignalsBean beanObj = signals.get(tempCount);
                System.out.println(beanObj.getStockCode() + "====" + beanObj.getSignalType());
            }
            initializeComponents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeComponents() throws SQLException {

        tableModel = new SignalsTableModel();
        table = new JTable(tableModel);
        table.setModel(tableModel);

        //table.setDefaultRenderer(String.class,new PortfolioPanel.CustomTableRenderer());
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane = new JScrollPane(table);
        this.add(scrollPane);

        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(85);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);


        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                // Ignore extra messages.
                if (e.getValueIsAdjusting())
                    return;

                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                    System.out.println("No rows are selected.");
                } else {
                    int selectedRow = lsm.getMinSelectionIndex();
                    tableRowSelectionEvent(selectedRow);
                }
            }
        });
    }

    private void tableRowSelectionEvent(int selectedRow){
        String selectedStock = "";
        Double price =null;
        try {
            selectedStock = table.getValueAt(table.getSelectedRow(), 0).toString();
            price = (SignalsPanel.INSTANCE.signals.get(selectedRow)).getPrice();
            if(price != null) {
                ShortListSignalsPanel.INSTANCE.price.setText(price.toString());
            }
        }
        catch(Exception E){
            E.printStackTrace();
        }
        try {
            CommentsPanel.INSTANCE.buyAlerts = DataAccess.INSTANCE.getAlertList(selectedStock,"B");
            CommentsPanel.INSTANCE.refreshAlerts();
            PortfolioMenuPanel.INSTANCE.enableButtons(true);
            MainPriceChart.INSTANCE.stockData = DataAccess.INSTANCE.getStockData(selectedStock);
            MainPriceChart.INSTANCE.positionData= DataAccess.INSTANCE.getPositionData(selectedStock,"A");
            MainPriceChart.INSTANCE.closedPositionData=DataAccess.INSTANCE.getPositionData(selectedStock,"I");
            MainPriceChart.INSTANCE.selectedStock = selectedStock;
            MainPriceChart.INSTANCE.refreshDataSet();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

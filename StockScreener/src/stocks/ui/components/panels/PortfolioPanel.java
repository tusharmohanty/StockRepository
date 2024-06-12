package stocks.ui.components.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import layout.TableLayout;
import stocks.model.beans.PortfolioBean;
import stocks.model.beans.PositionBean;
import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.ui.components.Dashboard;
import stocks.ui.components.charts.MainPriceChart;
import stocks.ui.components.tableModel.PortfolioTableModel;
import stocks.ui.components.tableModel.PositionTableModel;

import static stocks.util.Utils.openDashboard;
import static stocks.util.Utils.openScreener;

public class PortfolioPanel extends JPanel implements ActionListener{
    public static final PortfolioPanel INSTANCE = new PortfolioPanel();
    public List<PortfolioBean> portfolioData = new ArrayList();
    public PortfolioTableModel tableModel;

    JTable table = null;
    JScrollPane scrollPane = null;

    private PortfolioPanel() {
        try {
            portfolioData = DataAccess.INSTANCE.getPortfolioData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.setLayout(new BorderLayout());
        try {
            initializeComponents();
        }
        catch(Exception E) {
            E.printStackTrace();
        }
        addComponentsToPanel();
    }
    private void addComponentsToPanel() {

        this.add(scrollPane);
    }

    private void initializeComponents() throws SQLException {

        tableModel = new PortfolioTableModel();
        table = new JTable(tableModel);
        table.setModel(tableModel);

        table.setDefaultRenderer(String.class,new CustomTableRenderer());
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(55);
        table.getColumnModel().getColumn(2).setPreferredWidth(55);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(6).setPreferredWidth(55);
        table.getColumnModel().getColumn(7).setPreferredWidth(25);
        table.getColumnModel().getColumn(8).setPreferredWidth(8);
        table.getColumnModel().getColumn(9).setPreferredWidth(45);
        table.getColumnModel().getColumn(10).setPreferredWidth(45);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    if(column == 8){
                        openScreener(table.getValueAt(table.getSelectedRow(), 0).toString());
                    }
                }
            }
        });
        scrollPane = new JScrollPane(table);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(event.getValueIsAdjusting()){
                    String selectedStock = "";
                    try {
                        selectedStock = table.getValueAt(table.getSelectedRow(), 0).toString();
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
                // do some actions here, for example
                // print first column value from selected row
                System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
                System.out.println(event.getValueIsAdjusting() );
            }
        });



    }
    public void initializeTableSorting(){
        // Table sorting
        table.setAutoCreateRowSorter(true);
    }
    public void actionPerformed(ActionEvent e) {

    }
    //Custom DefaultTableCellRenderer

    public static class CustomTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                              boolean isSelected, boolean hasFocus, int row, int column)
        {
            Object formattedValue = value;
            if(column == 1 || column== 2 || column ==4 || column == 5 || column == 6 || column == 7 || column ==10){
                DecimalFormat format = new DecimalFormat("#.##");
                formattedValue =format.format(Double.parseDouble(value.toString()));
            }
            Component c = super.getTableCellRendererComponent(table, formattedValue, isSelected,
                    hasFocus, row, column);

            Double pLValue = Double.parseDouble(table.getValueAt(row, 5).toString());
            boolean isGrey = false;
            if(row%2 == 0){
                isGrey = true;
            }
            if(isSelected){
                c.setBackground(new Color(224,224,224));
            }
            else{
                c.setBackground(new Color(255,255,255));
            }
            if(pLValue < 0) {
                //set to red bold font
                c.setForeground(Color.RED);
                c.setFont(new Font("Dialog", Font.PLAIN, 12));
            } else {
                //stay at default
                c.setForeground(Color.BLACK);
                c.setFont(new Font("Dialog", Font.PLAIN, 12));
            }
            return c;
        }

    }

}

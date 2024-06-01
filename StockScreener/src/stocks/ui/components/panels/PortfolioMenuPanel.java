package stocks.ui.components.panels;

import layout.TableLayout;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import stocks.model.StockConstants;
import stocks.model.data.DataAccess;
import stocks.ui.components.Dashboard;
import stocks.ui.components.PortfolioView;
import stocks.util.DateLabelFormatter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class PortfolioMenuPanel extends JPanel implements ActionListener {
    public static final PortfolioMenuPanel INSTANCE = new PortfolioMenuPanel();
    JComboBox commentBasis,commentType,earningCallQuarter,buySell;
    JDatePickerImpl datePicker = null;
    JTextPane textPane = null;
    JButton addComments,viewConcallNotes,earningCallNotes,addStopLoss;
    JTextField stopLoss = null;
    boolean stockSelected = false;

    private PortfolioMenuPanel(){
        initializeComponents();
        this.setLayout(new GridBagLayout());
        addComponentsToPanel();
    }
    private void initializeComponents() {
        commentBasis = new JComboBox(new String[] {"Fundamental","Technical","Personal"});
        commentType = new JComboBox(new String[] {"Positive","Neutral","Negative"});
        earningCallQuarter = new JComboBox(new String[]{"Q32024","Q42024","Q12025","Q22025","Q32025"});
        buySell = new JComboBox((new String[]{"B","S"}));
        stopLoss= new JTextField();
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
        datePicker = new JDatePickerImpl(datePanel,new DateLabelFormatter());
        textPane = new JTextPane();
        StyledDocument textdoc = textPane.getStyledDocument();
        try {
            textdoc.insertString(textdoc.getLength(), "Fundamentals ", new SimpleAttributeSet() );
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        addComments = new JButton("Add Comments");
        viewConcallNotes = new JButton("Earnings Notes");
        earningCallNotes = new JButton("Add Earnings Notes");
        earningCallNotes.addActionListener(this);
        viewConcallNotes.addActionListener(this);
        addStopLoss= new JButton("Add");
        enableButtons(false);
        addStopLoss.addActionListener(this);


    }
    private void addComponentsToPanel() {
        double size[][] =
                {{0.12,0.12,0.18,0.2,0.38},
                        {0.15,0.15,0.15,0.15,0.35}};
        this.setLayout(new TableLayout(size));

        this.add(datePicker,"0,0,2,0");

        this.add(commentBasis,"0,1,1,1");
        this.add(commentType,"2,1,2,1");
        this.add(addComments,"3,1,3,1");

        this.add(earningCallQuarter,"0,2,1,2");
        this.add(earningCallNotes,"2,2,3,2");

        this.add(buySell,"0,3,0,3");
        this.add(stopLoss,"1,3,1,3");
        this.add(addStopLoss,"2,3,2,3");

        this.add(textPane,"0,4,4,4");

    }
    public void actionPerformed(ActionEvent e) {
        String stockCode,quarter;
        int year;
        stockCode = quarter = "";
        if(stockSelected) {
            stockCode = PortfolioPanel.INSTANCE.table.getValueAt(PortfolioPanel.INSTANCE.table.getSelectedRow(), 0).toString();
        }
        quarter = getQuarter(PortfolioMenuPanel.INSTANCE.earningCallQuarter.getSelectedItem().toString());
        year = getYear(PortfolioMenuPanel.INSTANCE.earningCallQuarter.getSelectedItem().toString());
        if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Add Earnings Notes")) {
            JFileChooser earningCall = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Pdf Files", "pdf");
            earningCall.setFileFilter(filter);
            int returnVal = earningCall.showOpenDialog(PortfolioView.INSTANCE);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " +
                        earningCall.getSelectedFile().getAbsolutePath());
                System.out.println(PortfolioMenuPanel.INSTANCE.earningCallQuarter.getSelectedItem());
                saveEarningNotes(quarter, earningCall.getSelectedFile().getAbsolutePath(),stockCode);
            }
        }
        else if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Earnings Notes")){
             String reportLocation = getLocalEarningsNoteLocation(stockCode,quarter,year);
        }
        else if(e.getSource() instanceof JButton  && e.getActionCommand().equals("Add")){
            String watchListType = this.buySell.getSelectedItem().toString();
            double  alertPrice = Double.parseDouble(this.stopLoss.getText());
            double threshold = 5d;
            String comments = this.textPane.getText();
            java.sql.Date  dateValue = new java.sql.Date(this.datePicker.getModel().getYear(),this.datePicker.getModel().getMonth(),this.datePicker.getModel().getDay());
            try {
                DataAccess.INSTANCE.saveAlerts(stockCode,watchListType,threshold,comments,dateValue,alertPrice);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    private String getLocalEarningsNoteLocation (String stockCode,String quarter, int year){
        String fileLocation = StockConstants.STOCK_SCREENER_HOME+"/earningNotes/pdf/" + stockCode+"_"+year +quarter+".pdf";
        File fileObj = new File(fileLocation);
        if(!fileObj.exists()) {
            try {
                DataAccess.INSTANCE.downloadAndSave(stockCode, year, quarter, fileLocation);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ConCallNotesPanel.INSTANCE.renderReport(StockConstants.STOCK_SCREENER_HOME+"/earningNotes/",stockCode+"_"+year +quarter+".pdf");
        return fileLocation;
    }
    private String getQuarter (String quarterString){
        return quarterString.substring(0,2);
    }

    private int getYear (String quarterString){
        return Integer.parseInt(quarterString.substring(2));
    }
    private void saveEarningNotes(String quarter,String filePath,String stockCode){
        try {
            DataAccess.INSTANCE.saveEarningsNotes(getYear(quarter),getQuarter(quarter),filePath,stockCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void enableButtons(boolean isEnabled){
        earningCallNotes.setEnabled(isEnabled);
        viewConcallNotes.setEnabled(isEnabled);
        addComments.setEnabled(isEnabled);
        if(isEnabled) {
            stockSelected = true;
        }
        buySell.setEnabled(isEnabled);
        addStopLoss.setEnabled(isEnabled);
        stopLoss.setEnabled(isEnabled);
    }
}


package stocks.ui.components.panels;

import layout.TableLayout;
import stocks.model.StockConstants;
import stocks.model.beans.AlertBean;
import stocks.model.beans.CommentsBean;
import stocks.model.data.DataAccess;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;
import java.sql.SQLException;
import java.util.Map;

public class CommentsPanel extends JPanel{
    public static final CommentsPanel INSTANCE = new CommentsPanel();
    List<CommentsBean> commentList;
    List <AlertBean> buyAlerts;
    JTextPane alertPane,fPositiveTextPane,fNegativeTextPane,fNeutralTextPane;
    StyledDocument fPositiveTextdoc,fNegativeTextDoc,fNeutralTextDoc;
    SimpleAttributeSet positiveCommentsStyle, negativeCommentsStyle,neutralCommentsStyle;


    {
        try {
            commentList = DataAccess.INSTANCE.getCommentsData("adsas");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CommentsPanel() {
        /**
         * Style for headers common across allPanes**/
        Color positiveColor = new Color(145,218,179);
        Color negativeColor = new Color(220,22,22);
        Color neutralColor = new Color(192,192,192);

        positiveCommentsStyle = new SimpleAttributeSet();
        negativeCommentsStyle = new SimpleAttributeSet();
        neutralCommentsStyle = new SimpleAttributeSet();

        SimpleAttributeSet headerStyle = new SimpleAttributeSet();
        StyleConstants.setBold(headerStyle, true);

        alertPane = new JTextPane();
        alertPane.setBackground(positiveColor );
        alertPane.setContentType("text/html");

        fNegativeTextPane = new JTextPane();
        fNegativeTextPane.setBackground(negativeColor);
        fNegativeTextDoc = fNegativeTextPane.getStyledDocument();

        fNeutralTextPane = new JTextPane();
        fNeutralTextPane.setBackground(neutralColor);
        fNeutralTextDoc = fNeutralTextPane.getStyledDocument();

        JTextPane tPositiveTextPane = new JTextPane();
        tPositiveTextPane.setBackground(new Color(145,218,179));
        StyledDocument tPositiveTextdoc = tPositiveTextPane.getStyledDocument();

        JTextPane tNegativeTextPane = new JTextPane();
        tNegativeTextPane.setBackground(new Color(220,22,22));
        StyledDocument tNegativeTextDoc = tNegativeTextPane.getStyledDocument();


        SimpleAttributeSet fundamentalTextStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(fundamentalTextStyle, Color.RED);



        //   try {
//            if(commentList.get(StockConstants.FUNDAMENTAL) != null){
//                fPositiveTextdoc.insertString(fPositiveTextdoc.getLength(), "Fundamentals ", headerStyle );
//                Map<String,List<String>> fundamentalCommentList = commentList.get(StockConstants.FUNDAMENTAL);
//                for (Map.Entry<String,List<String>> entry : fundamentalCommentList.entrySet()){
//                    String key = entry.getKey();
//                    if(key.equals(StockConstants.POSITIVE)){
//                       List<String> positiveComments = entry.getValue();
//
//                    }
//                    if(key.equals(StockConstants.NEGATIVE)){
//                        List<String> negativeComments = entry.getValue();
//                        for(int tempCount =0;tempCount < negativeComments.size();tempCount++){
//                            fNegativeTextDoc.insertString(fNegativeTextDoc.getLength(),"\n" + negativeComments.get(tempCount),negativeCommentsStyle );
//                        }
//                    }
//                }
//            }
//          }

//        } catch (BadLocationException e) {
//            throw new RuntimeException(e);
//        }
        double size[][] =
                {{0.01,0.33,0.33,0.32,0.01},
                        {0.25, 0.25,0.25,0.25}};
        this.setLayout(new TableLayout(size));
        this.add(alertPane,"1,0,1,1");
        //this.add(fNegativeTextPane,"2,0,2,0");
        //this.add(fNeutralTextPane,"3,0,3,0");
        //}
        //if(fCommentList.get(StockConstants.NEGATIVE) != null ) {
         //   this.add(tNegativeTextPane,"1,1,1,2");
        //}
        //Map<String,List<String>> tCommentList = commentList.get(StockConstants.TECHNICAL);
        //if(tCommentList.get(StockConstants.POSITIVE) != null ) {
         //   this.add(tPositiveTextPane,"1,2,1,2");
        //}
        //if(tCommentList.get(StockConstants.NEGATIVE) != null ) {
         //   this.add(tNegativeTextPane,"1,3,1,3");
        //}
    }

    public void refreshFundamentalPositiveContent(){
        fPositiveTextPane.setText("");
        fNeutralTextPane.setText("");
        fNegativeTextPane.setText("");
        for(int tempCount =0;tempCount < commentList.size();tempCount ++){
            CommentsBean beanObj = commentList.get(tempCount);
            if(beanObj.getCommentBasis().equals(StockConstants.FUNDAMENTAL)){
                if(beanObj.getCommentType().equals(StockConstants.POSITIVE)) {
                    try {
                        fPositiveTextdoc.insertString(fPositiveTextdoc.getLength(), "\n" + beanObj.getCommentText(), positiveCommentsStyle);
                    } catch (BadLocationException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(beanObj.getCommentType().equals(StockConstants.NEGATIVE)) {
                    try {
                        fNegativeTextDoc.insertString(fNegativeTextDoc.getLength(), "\n" + beanObj.getCommentText(), negativeCommentsStyle);
                    } catch (BadLocationException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(beanObj.getCommentType().equals(StockConstants.NEUTRAL)) {
                    try {
                        fNeutralTextDoc.insertString(fNeutralTextDoc.getLength(), "\n" + beanObj.getCommentText(), neutralCommentsStyle);
                    } catch (BadLocationException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            if(beanObj.getCommentBasis().equals(StockConstants.TECHNICAL)){
                try {
                    fPositiveTextdoc.insertString(fPositiveTextdoc.getLength(),"\n" + beanObj.getCommentText(),positiveCommentsStyle );
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void refreshAlerts(){
        String alertText ="<html>";
        for(int tempCount =0;tempCount < buyAlerts.size();tempCount ++){
            AlertBean beanObj = buyAlerts.get(tempCount);
            alertText +="<b>" + beanObj.getAlertDate() +":</b>" + beanObj.getComments()  + "<br>";
        }
        alertText +="</html>";
        alertPane.setText(alertText);
    }
}

package stocks.ui.components;

import java.awt.Container;
import java.awt.Dimension;

import layout.TableLayout;
import javax.swing.JFrame;

import stocks.ui.components.panels.*;

public class PortfolioView extends JFrame{
    public  final PortfolioPanel portfolioPanel = PortfolioPanel.INSTANCE;
    public final CommentsPanel commentsPanel = CommentsPanel.INSTANCE;
    public  final MainChartPanel chartPanel = MainChartPanel.INSTANCE;
    public final BottomPortfolioMenuPanel bottomPortfolioMenuPanel = BottomPortfolioMenuPanel.INSTANCE;
    public static final PortfolioView INSTANCE = new PortfolioView();
    private  PortfolioView() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Stock Screener 1.0 - Portfolio");
        this.setPreferredSize(new Dimension(3000,10000));
        this.setLocationRelativeTo(null);

    }

    private void addMainComponentsToPane(Container pane) {
        double size[][] =
                {{0.3,0.7},
                        {0.64,0.12,0.12,0.12}};

        this.setLayout(new TableLayout(size));
        this.add(portfolioPanel,    "0,0,0,0");
        this.add(ConsolidatedPortfolio.INSTANCE,"0,1,0,1");
        this.add(PortfolioMenuPanel.INSTANCE,"0,2,0,3");

        // 2nd column content
        this.add(chartPanel,"1,0,1,0");
        this.add(commentsPanel,"1,1,1,2");
        this.add(bottomPortfolioMenuPanel,"1,3,1,3");
    }

    public void initializeComponents(){
        addMainComponentsToPane(this.getContentPane());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        PortfolioView uiObj = PortfolioView.INSTANCE;
        uiObj.initializeComponents();
        PortfolioPanel.INSTANCE.initializeTableSorting();
        uiObj.pack();
        uiObj.setVisible(true);
    }

}

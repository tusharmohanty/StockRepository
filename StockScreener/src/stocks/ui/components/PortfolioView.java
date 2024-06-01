package stocks.ui.components;

import java.awt.Container;
import java.awt.Dimension;

import layout.TableLayout;
import javax.swing.JFrame;

import stocks.ui.components.panels.*;

public class PortfolioView extends JFrame{
    public  final PortfolioPanel portfolioPanel = PortfolioPanel.INSTANCE;
    public final CommentsPanel commentsPanel = CommentsPanel.INSTANCE;
    public static final PortfolioView INSTANCE = new PortfolioView();
    private  PortfolioView() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Stock Screener 1.0 - Portfolio");
        this.setPreferredSize(new Dimension(3000,10000));
        this.setLocationRelativeTo(null);

    }

    private void addMainComponentsToPane(Container pane) {
        double size[][] =
                {{0.01,0.3,0.69},
                        {0.7,0.16, 0.14}};

        this.setLayout(new TableLayout(size));
        this.add(portfolioPanel,    "1,0,1,0");
        this.add(commentsPanel,"2,0,2,1");
        this.add(ConsolidatedPortfolio.INSTANCE,"1,1,1,1");
        this.add(PortfolioMenuPanel.INSTANCE,"1,2,2,2");
    }

    public void initializeComponents(){
        addMainComponentsToPane(this.getContentPane());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        PortfolioView uiObj = PortfolioView.INSTANCE;
        uiObj.initializeComponents();
        uiObj.pack();
        uiObj.setVisible(true);
    }

}

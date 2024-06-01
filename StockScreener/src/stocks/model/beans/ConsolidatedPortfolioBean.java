package stocks.model.beans;

public class ConsolidatedPortfolioBean {
    double totalInvestments,totalPortfolio,profitLoss,profitLossPercentage;
    public ConsolidatedPortfolioBean(){
        totalInvestments = totalPortfolio = profitLoss = profitLossPercentage = 0d;
    }

    public double getTotalInvestments() {
        return totalInvestments;
    }

    public void setTotalInvestments(double totalInvestments) {
        this.totalInvestments = totalInvestments;
    }

    public double getTotalPortfolio() {
        return totalPortfolio;
    }

    public void setTotalPortfolio(double totalPortfolio) {
        this.totalPortfolio = totalPortfolio;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public double getProfitLossPercentage() {
        return profitLossPercentage;
    }

    public void setProfitLossPercentage(double profitLossPercentage) {
        this.profitLossPercentage = profitLossPercentage;
    }
}

package stocks.model.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

public class PortfolioBean {
    long portfolioId, qty;
    String stockCode;
    double price,currentPrice,pL,weightage, totalPortfolioValue,totalInvestment, plActual,delta,move;
    Date txnDate;

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getPL() {
        return pL;
    }

    public void setPL() {
        if(this.price != 0) {
            this.pL = round(((this.currentPrice - this.price) / this.price) * 100, 2);
        }
    }

    public double getWeightage() {
        return weightage;
    }

    public void setWeightage(double weightage) {
        this.weightage = weightage;
    }

    public double getTotalPortfolioValue() {
        return totalPortfolioValue;
    }

    public void setTotalPortfolioValue(double totalPortfolioValue) {
        this.totalPortfolioValue = totalPortfolioValue;
    }

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public double getPlActual() {
        return plActual;
    }

    public void setPlActual(double plActual) {
        this.plActual = plActual;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getMove() {
        return move;
    }

    public void setMove(double move) {
        this.move = move;
    }
}

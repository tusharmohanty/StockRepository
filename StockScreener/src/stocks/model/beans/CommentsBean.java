package stocks.model.beans;

import java.util.Date;

public class CommentsBean {
    private long commentId;
    private String stockCode,commentBasis,commentType,commentText;
    private java.sql.Date commentDate;



    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getCommentBasis() {
        return commentBasis;
    }

    public void setCommentBasis(String commentBasis) {
        this.commentBasis = commentBasis;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(java.sql.Date commentDate) {
        this.commentDate = commentDate;
    }
}

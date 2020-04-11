/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jfree.chart.annotations;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * @author peter
 */
public class CategoryDomainAnnotation extends AxisAnnotation implements CategoryAnnotation{
    private Comparable category;
    
    private boolean drawAsLine;
    
    public CategoryDomainAnnotation(Comparable category){
        this.category = category;
    }
    public Comparable getCategory(){
        return this.category;
    }
    public void setCategory(Comparable category){
        this.category = category;
        fireAnnotationChanged();
    }
    public boolean getDrawAsLine(){
        return this.drawAsLine;
    }
    public void setDrawAsLine(boolean drawAsLine){
        this.drawAsLine = drawAsLine;
        fireAnnotationChanged();
    }
    /**
     * Draws the annotation.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param dataArea  the data area.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     */
    public void draw(Graphics2D g2, CategoryPlot plot, Rectangle2D dataArea,
                     CategoryAxis domainAxis, ValueAxis rangeAxis){
        if(category == null) return;
        CategoryDataset dataset = null;
        for(int i = 0; i < plot.getDatasetCount(); i++) {
            if(plot.getDomainAxisForDataset(i) == domainAxis){
                dataset = plot.getDataset(i);
            }
        }
        int columnIndex = dataset.getColumnIndex(category);
        if (columnIndex < 0) {
            return;
        }
        int index = plot.getDomainAxisIndex(domainAxis);
        RectangleEdge axisEdge = plot.getDomainAxisEdge(index);
        Shape markerShape = null;
        if (drawAsLine) {
            double v = domainAxis.getCategoryMiddle(columnIndex,
                    dataset.getColumnCount(), dataArea,
                    axisEdge);
            markerShape = this.getLine(dataArea, axisEdge, v);
            g2.setPaint(getPaint());
            g2.setStroke(getStroke());
            g2.draw(markerShape);
        }
        else {
            double v0 = domainAxis.getCategoryStart(columnIndex,
                    dataset.getColumnCount(), dataArea,
                    axisEdge);
            double v1 = domainAxis.getCategoryEnd(columnIndex,
                    dataset.getColumnCount(), dataArea,
                    axisEdge);
            Line2D startLine = this.getLine(dataArea, axisEdge, v0);
            Line2D endLine = this.getLine(dataArea, axisEdge, v1);
            Path2D.Double path = new Path2D.Double();
            path.moveTo(startLine.getX1(), startLine.getY1());
            path.lineTo(startLine.getX2(), startLine.getY2());
            path.lineTo(endLine.getX2(), endLine.getY2());
            path.lineTo(endLine.getX1(), endLine.getY1());
            path.closePath();
            g2.setPaint(getPaint());
            g2.fill(path);
            g2.setStroke(getOutlineStroke());
            g2.setPaint(getOutlinePaint());
            g2.draw(startLine);
            g2.draw(endLine);
            markerShape = path;
        }
        drawLabel(g2, dataArea, markerShape.getBounds2D(), axisEdge);
    }
}

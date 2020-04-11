/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jfree.chart.annotations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.plot.CategoryPlot;

/**
 *
 * @author peter
 */
public class CategoryRangeIntervalAnnotation extends IntervalAnnotation implements CategoryAnnotation{
    
    public CategoryRangeIntervalAnnotation(double startValue, double endValue){
        super(startValue, endValue);
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
        int index = plot.getRangeAxisIndex(rangeAxis);
        if(index < 0) return;
        AxisLocation location = plot.getRangeAxisLocation(index);
        RectangleEdge axisEdge = Plot.resolveRangeAxisLocation(location, plot.getOrientation());
        double start = rangeAxis.valueToJava2D(getStartValue(), dataArea, axisEdge);
        Line2D startLine = this.getLine(dataArea, axisEdge, start);
        double end = rangeAxis.valueToJava2D(getEndValue(), dataArea, axisEdge);
        Line2D endLine = this.getLine(dataArea, axisEdge, end);
        if(startLine == null || endLine == null) return;
        Path2D.Double path = new Path2D.Double();
        path.moveTo(startLine.getX1(), startLine.getY1());
        path.lineTo(startLine.getX2(), startLine.getY2());
        path.lineTo(endLine.getX2(), endLine.getY2());
        path.lineTo(endLine.getX1(), endLine.getY1());
        path.closePath();
        g2.setPaint(getPaint());
        g2.fill(path);
        g2.setPaint(getOutlinePaint());
        g2.setStroke(getOutlineStroke());
        g2.draw(startLine);
        g2.draw(endLine);
        drawLabel(g2, dataArea, path.getBounds2D(), axisEdge);
    }
}

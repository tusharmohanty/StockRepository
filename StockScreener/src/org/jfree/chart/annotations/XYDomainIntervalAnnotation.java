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
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.RectangleEdge;

/**
 *
 * @author peter
 */
public class XYDomainIntervalAnnotation extends IntervalAnnotation implements XYAnnotation{
    
    public XYDomainIntervalAnnotation(double startValue, double endValue){
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
     * @param rendererIndex  the renderer index.
     * @param info  an optional info object that will be populated with
     *              entity information.
     */
    public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea,
                     ValueAxis domainAxis, ValueAxis rangeAxis,
                     int rendererIndex,
                     PlotRenderingInfo info){
        int index = plot.getDomainAxisIndex(domainAxis);
        if(index < 0) return;
        AxisLocation dal = plot.getDomainAxisLocation(index);
        RectangleEdge axisEdge = Plot.resolveDomainAxisLocation(dal, plot.getOrientation());
        double start = domainAxis.valueToJava2D(getStartValue(), dataArea, axisEdge);
        Line2D startLine = this.getLine(dataArea, axisEdge, start);
        double end = domainAxis.valueToJava2D(getEndValue(), dataArea, axisEdge);
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
        addEntity(info, path, rendererIndex, getToolTipText(), getURL());
    }
}

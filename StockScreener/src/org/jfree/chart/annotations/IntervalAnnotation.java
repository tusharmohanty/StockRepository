/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jfree.chart.annotations;

import java.awt.Paint;
import java.awt.Stroke;

/**
 *
 * @author peter
 */
public abstract class IntervalAnnotation extends AxisAnnotation {

    private double startValue;
    private double endValue;

    /**
     *
     */
    protected IntervalAnnotation() {
        super();
    }

    /**
     *
     * @param startValue
     * @param endValue
     */
    protected IntervalAnnotation(double startValue, double endValue) {
        super();
        this.startValue = startValue;
        this.endValue = endValue;
    }

    /**
     *
     * @param stroke
     * @param paint
     * @param startValue
     * @param endValue
     */
    protected IntervalAnnotation(Stroke stroke, Paint paint, double startValue, double endValue) {
        super(stroke, paint);
        this.startValue = startValue;
        this.endValue = endValue;
    }

    /**
     *
     * @return
     */
    public double getStartValue() {
        return this.startValue;
    }

    /**
     *
     * @param value
     */
    public void setStartValue(double value) {
        this.startValue = value;
        fireAnnotationChanged();
    }

    /**
     *
     * @return
     */
    public double getEndValue() {
        return this.endValue;
    }

    /**
     *
     * @param value
     */
    public void setEndValue(double value) {
        this.endValue = value;
        fireAnnotationChanged();
    }
}

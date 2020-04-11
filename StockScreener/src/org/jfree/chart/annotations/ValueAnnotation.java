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
public abstract class ValueAnnotation extends AxisAnnotation {

    private double value;

    protected ValueAnnotation() {
        super();
    }

    protected ValueAnnotation(Stroke stroke, Paint paint, double value) {
        super(stroke, paint);
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
        fireAnnotationChanged();
    }
}

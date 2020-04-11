
package org.jfree.chart.annotations;

import java.awt.BasicStroke;
import java.io.Serializable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.AxisAnnotationEntity;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.ui.LengthAdjustmentType;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.io.SerialUtilities;
import org.jfree.text.TextUtilities;
import org.jfree.text.TextBlock;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

public abstract class AxisAnnotation extends AbstractAnnotation implements Serializable {

    /**
     * The tool tip text.
     */
    private String toolTipText;

    /**
     * The URL.
     */
    private String url;

    private transient Stroke stroke;

    private transient Stroke outlineStroke;

    private transient Paint paint;

    private transient Paint outlinePaint;
    /**
     * The linePpaint (null is not allowed).
     */
    private transient Paint labelBackgroundPaint;

    /**
     * The content.
     */
    private TextBlock content;

    /**
     * The label.
     */
    private String label = "";

    /**
     * The label font.
     */
    private Font labelFont;

    /**
     * The outline visible flag.
     */
    private boolean labelBackgroundVisible;

    private boolean labelVisible;

    /**
     * The label linePpaint.
     */
    private transient Paint labelPaint;

    /**
     * The label position.
     */
    private RectangleAnchor labelAnchor;

    /**
     * The label angle.
     */
    private double labelAngle;

    /**
     * The text anchor for the label.
     */
    private TextAnchor labelTextAnchor;

    /**
     * The rotation anchor for the label.
     */
    private TextAnchor rotationAnchor;

    /**
     * The label offset from the marker rectangle.
     */
    private RectangleInsets labelOffset;

    private boolean labelClippingAllowed;

    /**
     * The offset type for the domain or range axis (never <code>null</code>).
     */
    private LengthAdjustmentType labelOffsetType;

    protected AxisAnnotation() {
        this(new BasicStroke(1.0f), Color.LIGHT_GRAY);
    }

    protected AxisAnnotation(Stroke stroke, Paint paint) {
        this(stroke, paint, new BasicStroke(1.0f), Color.BLACK);
    }

    protected AxisAnnotation(Stroke stroke, Paint paint, Stroke outlineStroke, Paint outlinePaint) {
        super();
        this.stroke = stroke;
        this.paint = paint;
        this.outlineStroke = outlineStroke;
        this.outlinePaint = outlinePaint;
        this.labelFont = new Font("SansSerif", Font.PLAIN, 12);
        this.labelPaint = Color.black;
        this.labelAnchor = RectangleAnchor.TOP;
        this.rotationAnchor = TextAnchor.TOP_CENTER;
        this.labelOffset = new RectangleInsets(3.0, 3.0, 3.0, 3.0);
        this.labelOffsetType = LengthAdjustmentType.CONTRACT;
        this.labelTextAnchor = TextAnchor.TOP_CENTER;
        this.labelBackgroundPaint = Color.LIGHT_GRAY;
        this.labelBackgroundVisible = false;
        this.labelClippingAllowed = true;
        this.content = TextUtilities.createTextBlock(label, labelFont, labelPaint);
    }

    public Stroke getStroke() {
        return this.stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        fireAnnotationChanged();
    }

    public Stroke getOutlineStroke() {
        return this.outlineStroke;
    }

    public void setOutlineStroke(Stroke stroke) {
        this.outlineStroke = stroke;
        fireAnnotationChanged();
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
        fireAnnotationChanged();
    }

    public Paint getOutlinePaint() {
        return this.outlinePaint;
    }

    public void setOutlinePaint(Paint paint) {
        this.outlinePaint = paint;
        fireAnnotationChanged();
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
        this.content = TextUtilities.createTextBlock(getLabel(), labelFont, labelPaint);
        fireAnnotationChanged();
    }

    /**
     * Returns the tool tip text for the annotation. This will be displayed in a
     * {@link org.jfree.chart.ChartPanel} when the mouse pointer hovers over the
     * annotation.
     *
     * @return The tool tip text (possibly <code>null</code>).
     *
     * @see #setToolTipText(String)
     */
    public String getToolTipText() {
        return this.toolTipText;
    }

    /**
     * Sets the tool tip text for the annotation.
     *
     * @param text the tool tip text (<code>null</code> permitted).
     *
     * @see #getToolTipText()
     */
    public void setToolTipText(String text) {
        this.toolTipText = text;
        fireAnnotationChanged();
    }

    /**
     * Returns the URL for the annotation. This URL will be used to provide
     * hyperlinks when an HTML image map is created for the chart.
     *
     * @return The URL (possibly <code>null</code>).
     *
     * @see #setURL(String)
     */
    public String getURL() {
        return this.url;
    }

    /**
     * Sets the URL for the annotation.
     *
     * @param url the URL (<code>null</code> permitted).
     *
     * @see #getURL()
     */
    public void setURL(String url) {
        this.url = url;
        fireAnnotationChanged();
    }

    public double getLabelAngle() {
        return this.labelAngle;
    }

    public void setLabelAngle(double labelAngle) {
        this.labelAngle = labelAngle;
        fireAnnotationChanged();
    }

    public boolean isLabelVisible() {
        return this.labelVisible;
    }

    public void setLabelVisible(boolean v) {
        if (v != this.labelVisible) {
            this.labelVisible = v;
            fireAnnotationChanged();

        }
    }

    public Font getLabelFont() {
        return this.labelFont;
    }

    public void setLabelFont(Font labelFont) {
        this.labelFont = labelFont;
        this.content = TextUtilities.createTextBlock(getLabel(), labelFont, labelPaint);
        fireAnnotationChanged();
    }

    public Paint getLabelPaint() {
        return this.labelPaint;
    }

    public void setLabelPaint(Paint labelPaint) {
        this.labelPaint = labelPaint;
        this.content = TextUtilities.createTextBlock(getLabel(), labelFont, labelPaint);
        fireAnnotationChanged();
    }

    public RectangleInsets getLabelOffset() {
        return this.labelOffset;
    }

    public void setLabelOffset(RectangleInsets offset) {
        this.labelOffset = offset;
        fireAnnotationChanged();
    }

    public LengthAdjustmentType getLabelOffsetType() {
        return this.labelOffsetType;
    }

    public void setLabelOffsetTyte(LengthAdjustmentType offsetType) {
        this.labelOffsetType = offsetType;
        fireAnnotationChanged();
    }

    public Paint getLabelBackgroundPaint() {
        return this.labelBackgroundPaint;
    }

    public void setLabelBackgroundPaint(Paint paint) {
        this.labelBackgroundPaint = paint;
        fireAnnotationChanged();
    }

    public boolean isLabelBackgroundVisible() {
        return this.labelBackgroundVisible;
    }

    public void setLabelBackgroundVisible(boolean flag) {
        this.labelBackgroundVisible = flag;
        fireAnnotationChanged();
    }

    public boolean isLabelClippingAllowed() {
        return this.labelClippingAllowed;
    }

    public void setLabelClippingAllowed(boolean flag) {
        this.labelClippingAllowed = flag;
        fireAnnotationChanged();
    }

    public RectangleAnchor getLabelAnchor() {
        return this.labelAnchor;
    }

    public void setLabelAnchor(RectangleAnchor labelAnchor) {
        this.labelAnchor = labelAnchor;
        fireAnnotationChanged();
    }

    public TextAnchor getLabelTextAnchor() {
        return this.labelTextAnchor;
    }

    public void setLabelTextAnchor(TextAnchor labelTextAnchor) {
        this.labelTextAnchor = labelTextAnchor;
        fireAnnotationChanged();
    }

    public TextAnchor getRotationAnchor() {
        return this.rotationAnchor;
    }

    public void setRotationAnchor(TextAnchor rotationAnchor) {
        this.rotationAnchor = rotationAnchor;
        fireAnnotationChanged();
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (this.outlineStroke != null ? this.outlineStroke.hashCode() : 0);
        result = 29 * result + (this.labelFont != null ? this.labelFont.hashCode() : 0);
        result = 29 * result + (this.paint != null ? this.paint.hashCode() : 0);
        result = 29 * result + (this.stroke != null ? this.stroke.hashCode() : 0);
        result = 29 * result + (this.outlinePaint != null ? this.outlinePaint.hashCode() : 0);
        return result;
    }

    protected void drawLabel(Graphics2D g2, Rectangle2D dataArea, Rectangle2D markerArea, RectangleEdge axisEdge) {
        /*Shape oldClip = g2.getClip();
        g2.setClip(null);*/
        if (!labelVisible) {
            return;
        }
        String label = getLabel();
        RectangleAnchor labelAnchor = getLabelAnchor();
        if (label != null) {
            Font labelFont = getLabelFont();
            g2.setFont(labelFont);
            Shape rawBounds = TextUtilities.calculateRotatedStringBounds(
                    label, g2, 0.0f, 0.0f,
                    getLabelTextAnchor(),
                    0.0, TextAnchor.CENTER);
            Shape rotatedBounds = TextUtilities.calculateRotatedStringBounds(
                    label, g2, 0.0f, 0.0f,
                    getLabelTextAnchor(),
                    getLabelAngle(), getRotationAnchor());
            //marker.getLabel
            Point2D coordinates = calculateMarkerTextAnchorPoint(
                    g2, axisEdge, markerArea, getLabelOffset(),
                    LengthAdjustmentType.EXPAND, labelAnchor);
            float anchorX = (float) coordinates.getX();
            float anchorY = (float) coordinates.getY();
            TextBlockAnchor tba = textToTextBlockAnchor(getLabelTextAnchor());
            float[] rotationAchor = calculateRotationAnchorPoint(tba, markerArea);
            Shape bounds = this.content.calculateBounds(g2, anchorX, anchorY, tba, rotationAchor[0], rotationAchor[1], labelAngle);
            if (getLabelBackgroundPaint() != null && isLabelBackgroundVisible()) {
                g2.setPaint(getLabelBackgroundPaint());
                g2.fill(bounds);
            }
            this.content.draw(g2, anchorX, anchorY, tba, rotationAchor[0], rotationAchor[1], labelAngle);
            /*Shape labelArea = TextUtilities.calculateRotatedStringBounds(
                    label, g2, anchorX, anchorY,
                    getLabelTextAnchor(), getLabelAngle(), getRotationAnchor());

            if (getLabelBackgroundPaint() != null && isLabelBackgroundVisible()) {
                g2.setPaint(getLabelBackgroundPaint());
                g2.fill(labelArea);
            }
            g2.setPaint(getLabelPaint());
            TextUtilities.drawRotatedString(label, g2, anchorX, anchorY,
                    getLabelTextAnchor(), getLabelAngle(), getRotationAnchor());*/
        }
    }

    protected TextBlockAnchor textToTextBlockAnchor(TextAnchor textAnchor) {
        if (textAnchor == TextAnchor.TOP_LEFT) {
            return TextBlockAnchor.TOP_LEFT;
        } else if (textAnchor == TextAnchor.CENTER_LEFT
                || textAnchor == TextAnchor.BASELINE_LEFT
                || textAnchor == TextAnchor.HALF_ASCENT_LEFT) {
            return TextBlockAnchor.CENTER_LEFT;
        } else if (textAnchor == TextAnchor.BOTTOM_LEFT) {
            return TextBlockAnchor.BOTTOM_LEFT;
        } else if (textAnchor == TextAnchor.TOP_CENTER) {
            return TextBlockAnchor.TOP_CENTER;
        } else if (textAnchor == TextAnchor.CENTER
                || textAnchor == TextAnchor.BASELINE_CENTER
                || textAnchor == TextAnchor.HALF_ASCENT_CENTER) {
            return TextBlockAnchor.CENTER;
        } else if (textAnchor == TextAnchor.BOTTOM_CENTER) {
            return TextBlockAnchor.BOTTOM_CENTER;
        } else if (textAnchor == TextAnchor.TOP_RIGHT) {
            return TextBlockAnchor.TOP_RIGHT;
        } else if (textAnchor == TextAnchor.CENTER_RIGHT
                || textAnchor == TextAnchor.BASELINE_RIGHT
                || textAnchor == TextAnchor.HALF_ASCENT_RIGHT) {
            return TextBlockAnchor.CENTER_RIGHT;
        } else if (textAnchor == TextAnchor.BOTTOM_RIGHT) {
            return TextBlockAnchor.BOTTOM_RIGHT;
        }
        return TextBlockAnchor.CENTER;
    }

    private float[] calculateRotationAnchorPoint(TextBlockAnchor textAnchor, Rectangle2D markerArea) {
        double x = 0.0;
        double y = 0.0;
        if (textAnchor == TextBlockAnchor.TOP_LEFT) {
            x = markerArea.getMinX();
            y = markerArea.getMinY();
        } else if (textAnchor == TextBlockAnchor.CENTER_LEFT){
            x = markerArea.getMinX();
            y = markerArea.getCenterY();
        } else if (textAnchor == TextBlockAnchor.BOTTOM_LEFT) {
            x = markerArea.getMinX();
            y = markerArea.getMaxY();
        } else if (textAnchor == TextBlockAnchor.TOP_CENTER) {
            x = markerArea.getCenterX();
            y = markerArea.getMinY();
        } else if (textAnchor == TextBlockAnchor.CENTER) {
            x = markerArea.getCenterX();
            y = markerArea.getCenterY();
        } else if (textAnchor == TextBlockAnchor.BOTTOM_CENTER) {
            x = markerArea.getCenterX();
            y = markerArea.getMaxY();
        } else if (textAnchor == TextBlockAnchor.TOP_RIGHT) {
            x = markerArea.getMaxX();
            y = markerArea.getMinY();
        } else if (textAnchor == TextBlockAnchor.CENTER_RIGHT) {
            x = markerArea.getMaxX();
            y = markerArea.getCenterY();
        } else if (textAnchor == TextBlockAnchor.BOTTOM_RIGHT) {
            x = markerArea.getMaxX();
            y = markerArea.getMaxY();
        }
        return new float[]{(float) x, (float) y};
    }

    protected Point2D calculateMarkerTextAnchorPoint(Graphics2D g2,
            RectangleEdge axisEdge,
            Rectangle2D markerArea,
            RectangleInsets markerOffset,
            LengthAdjustmentType labelOffsetType,
            RectangleAnchor anchor) {

        Rectangle2D anchorRect = null;
        if (RectangleEdge.isLeftOrRight(axisEdge)) {
            anchorRect = markerOffset.createAdjustedRectangle(markerArea,
                    LengthAdjustmentType.CONTRACT, labelOffsetType);
        } else if (RectangleEdge.isTopOrBottom(axisEdge)) {
            anchorRect = markerOffset.createAdjustedRectangle(markerArea,
                    labelOffsetType, LengthAdjustmentType.CONTRACT);
        }
        return RectangleAnchor.coordinates(anchorRect, anchor);

    }

    protected Line2D getLine(Rectangle2D dataArea, RectangleEdge axisEdge, double java2D) {
        Line2D result = null;
        if (axisEdge.equals(RectangleEdge.LEFT)) {
            result = new Line2D.Double(dataArea.getMinX(), java2D, dataArea.getMaxX(), java2D);
        } else if (axisEdge.equals(RectangleEdge.RIGHT)) {
            result = new Line2D.Double(dataArea.getMaxX(), java2D, dataArea.getMinX(), java2D);
        } else if (axisEdge.equals(RectangleEdge.TOP)) {
            result = new Line2D.Double(java2D, dataArea.getMinY(), java2D, dataArea.getMaxY());
        } else if (axisEdge.equals(RectangleEdge.BOTTOM)) {
            result = new Line2D.Double(java2D, dataArea.getMaxY(), java2D, dataArea.getMinY());
        }
        return result;
    }

    protected void addEntity(PlotRenderingInfo info,
            Shape hotspot, int rendererIndex,
            String toolTipText, String urlText) {
        if (info == null) {
            return;
        }
        EntityCollection entities = info.getOwner().getEntityCollection();
        if (entities == null) {
            return;
        }
        AxisAnnotationEntity entity = new AxisAnnotationEntity(hotspot,
                rendererIndex, toolTipText, urlText, this);
        entities.add(entity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AxisAnnotation other = (AxisAnnotation) obj;
        if (this.stroke != other.stroke && (this.stroke == null || !this.stroke.equals(other.stroke))) {
            return false;
        }
        if (this.outlineStroke != other.outlineStroke && (this.outlineStroke == null || !this.outlineStroke.equals(other.outlineStroke))) {
            return false;
        }
        if (this.paint != other.paint && (this.paint == null || !this.paint.equals(other.paint))) {
            return false;
        }
        if (this.outlinePaint != other.outlinePaint && (this.outlinePaint == null || !this.outlinePaint.equals(other.outlinePaint))) {
            return false;
        }
        if (this.labelBackgroundPaint != other.labelBackgroundPaint && (this.labelBackgroundPaint == null || !this.labelBackgroundPaint.equals(other.labelBackgroundPaint))) {
            return false;
        }
        if ((this.label == null) ? (other.label != null) : !this.label.equals(other.label)) {
            return false;
        }
        if (this.labelFont != other.labelFont && (this.labelFont == null || !this.labelFont.equals(other.labelFont))) {
            return false;
        }
        if (this.labelBackgroundVisible != other.labelBackgroundVisible) {
            return false;
        }
        if (this.labelPaint != other.labelPaint && (this.labelPaint == null || !this.labelPaint.equals(other.labelPaint))) {
            return false;
        }
        if (this.labelAnchor != other.labelAnchor && (this.labelAnchor == null || !this.labelAnchor.equals(other.labelAnchor))) {
            return false;
        }
        if (Double.doubleToLongBits(this.labelAngle) != Double.doubleToLongBits(other.labelAngle)) {
            return false;
        }
        if (this.labelTextAnchor != other.labelTextAnchor && (this.labelTextAnchor == null || !this.labelTextAnchor.equals(other.labelTextAnchor))) {
            return false;
        }
        if (this.rotationAnchor != other.rotationAnchor && (this.rotationAnchor == null || !this.rotationAnchor.equals(other.rotationAnchor))) {
            return false;
        }
        if (this.labelOffset != other.labelOffset && (this.labelOffset == null || !this.labelOffset.equals(other.labelOffset))) {
            return false;
        }
        if (this.labelClippingAllowed != other.labelClippingAllowed) {
            return false;
        }
        if (this.labelOffsetType != other.labelOffsetType && (this.labelOffsetType == null || !this.labelOffsetType.equals(other.labelOffsetType))) {
            return false;
        }
        return true;
    }

    /**
     * Provides serialization support.
     *
     * @param stream the output stream.
     *
     * @throws IOException if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.paint, stream);
        SerialUtilities.writeStroke(this.stroke, stream);
        SerialUtilities.writePaint(this.outlinePaint, stream);
        SerialUtilities.writeStroke(this.outlineStroke, stream);
        SerialUtilities.writePaint(this.labelPaint, stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream the input stream.
     *
     * @throws IOException if there is an I/O error.
     * @throws ClassNotFoundException if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.paint = SerialUtilities.readPaint(stream);
        this.stroke = SerialUtilities.readStroke(stream);
        this.outlinePaint = SerialUtilities.readPaint(stream);
        this.outlineStroke = SerialUtilities.readStroke(stream);
        this.labelPaint = SerialUtilities.readPaint(stream);
    }
}

package stocks.ui.components.charts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;

public class PriceToolTipGenerator implements XYToolTipGenerator {

	@Override
	public String generateToolTip(XYDataset dataset, int series, int item) {
		java.util.Date dateObj= new java.util.Date(((Double)dataset.getXValue(series, item)).longValue());
		DateFormat simpleFormat = new SimpleDateFormat("dd/MMM/YYYY");
		String strDate = simpleFormat.format(dateObj);
		return dataset.getYValue(series, item) +","+ strDate ;
	}

}

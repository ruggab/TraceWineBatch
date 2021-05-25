package net.mcsistemi.rfidtunnel.batch.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;


/**
 * The Class InvioStreamProcessor.
 * 
 * @author ashraf
 */
public class InvioStreamProcessor implements ItemProcessor{

	private static final Logger log = LoggerFactory.getLogger(InvioStreamProcessor.class);

//	@Override
//	public Trade process(final FxMarketEvent fxMarketEvent) throws Exception {
//
//		final String stock = fxMarketEvent.getStock();
//		final String time = fxMarketEvent.getTime();
//		final double price = Double.valueOf(fxMarketEvent.getPrice());
//		final long shares = Long.valueOf(fxMarketEvent.getShares());
//		final Trade trade = new Trade(stock, time, price, shares);
//
//		log.trace("Converting (" + fxMarketEvent + ") into (" + trade + ")");
//
//		return trade;
//	}

	@Override
	public Object process(Object item) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

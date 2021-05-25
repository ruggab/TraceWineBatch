package net.mcsistemi.rfidtunnel.batch.item;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

/**
 * The Class StockPriceAggregator.
 * 
 * @author ashraf
 */
public class StreamWriter implements ItemWriter<Object> {


	private static final Logger log = LoggerFactory.getLogger(StreamWriter.class);

	@Override
	public void write(List<? extends Object> items) throws Exception {
		log.info("*********LOG  StreamWriter start");
		System.out.println("********* StreamWriter start");
		
	}

	
}

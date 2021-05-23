package net.mcsistemi.rfidtunnel.batch.item;


import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import net.mcsistemi.rfidtunnel.db.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamAttesoRepository;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamRepository;
import net.mcsistemi.rfidtunnel.db.repository.ScannerStreamRepository;


/**
 * The Class StreamReader.
 *
 * @author ashraf
 */
public class StreamReader implements ItemReader<Object> {
	private static final Logger log = LoggerFactory.getLogger(StreamWriter.class);
	
	@Autowired
	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private ScannerStreamRepository scannerStreamRepository;

	@Autowired
	private ReaderStreamAttesoRepository readerStreamAttesoRepository;
	
	
	@Override
	public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		List<ScannerStream>  listScanner = scannerStreamRepository.findAll();
		for (ScannerStream scannerStream : listScanner) {
			log.info(scannerStream.getPackageData());
		}
		log.info("*********LOG  jobStreamReader start");
		System.out.println("********* jobStreamReader start");
		return null;
	}

	

}

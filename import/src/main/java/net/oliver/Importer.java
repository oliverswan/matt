package net.oliver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import net.oliver.database.DBManager;

// potential problems:
// 1.contactid is null
public class Importer {
	
	
	private static Logger log = LoggerFactory.getLogger(Importer.class);
	
	private static int count = 0;
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException {
		CsvParserSettings settings = new CsvParserSettings();  
		SimpleRestCaller caller = new SimpleRestCaller();
		DBManager db = new DBManager();
		settings.setRowProcessor(new RowProcessor() {  
		  
		    @Override  
		    public void processStarted(ParsingContext context) {  
		    }  
		  
		    @Override  
		    public void rowProcessed(String[] row, ParsingContext context) {  
		        if(context.currentLine() >1)
		        {
		        	count +=1;
		        	try {
//		        		log.debug("Start process Line "+ context.currentLine() +" of CSV file.");
			        	String reaction = row[SimpleRestCaller.csvSeq[2]];
			        	if(reaction == null || "".equals(reaction))
			        	{
			        		log.info("Line "+count+" : Skip for line for null reaction.");
			        		return;
			        	}
			        	String ticketid = row[SimpleRestCaller.csvSeq[1]];
			        	int contactiD;
			        	if(row[SimpleRestCaller.csvSeq[0]]==null || "".equals(row[SimpleRestCaller.csvSeq[0]]))
			        	{
			        		
			        		List<Map> cId = db.select("select contact_recid from v_rpt_Service where TicketNbr = '"+ticketid+"'");
			        		if(cId.size()>0)
			        		{
			        			contactiD = Integer.parseInt((String)cId.get(0).get("contact_recid"));
			        			log.info("get contact id from database : "+ticketid);
			        		}else {
			        			log.info("Line "+count+" : Skip for line for null contactid.");
				        		return;
			        		}
			        	}else {
			        		contactiD = Integer.parseInt(row[SimpleRestCaller.csvSeq[0]]);
			        	}
			        	
			        	
			        	String time = row[SimpleRestCaller.csvSeq[3]];
			        	DateTime dt = SimpleRestCaller.format.parseDateTime(time);
			        	String value = reaction +" result on ticket "+ticketid+" on "+dt.toString("yyyy-MM-dd HH:mm:ss");
			        	
			        	
			        	List<String>  result = caller.getSortedListofCustomfield(contactiD,dt.toDate(),value,count);
			        	
			        	log.info("Line "+count+" : start batch replace ============================================");
			        	
			        	
			        	for(int i=0;i<result.size();i++)
			        	{
			        		
			        		String path = "customFields/"+i+"/value";
			        		caller.updateContactById(contactiD, "replace", path, result.get(i),count);
			        	}
			        	
			        	log.info("Line "+count+" : end batch replace ============================================");
		        	}catch(Exception e)
		        	{
		        		e.printStackTrace();
		        		log.error("Exception occurs during processing line "+count+" Exception: "+e.getClass().getCanonicalName());
		        	}
		        }
		    }  
		  
		    @Override  
		    public void processEnded(ParsingContext context) {  
		    }  
		});  
		  
		CsvParser parser = new CsvParser(settings);  
		List<String[]> allRows = parser.parseAll(new FileReader(SimpleRestCaller.filepath));  

	}
}

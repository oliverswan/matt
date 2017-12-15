package net.oliver.app.app6;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

import net.oliver.app.app6.model.Bunch;
import net.oliver.app.app6.model.Data;
import net.oliver.database.DBManager;
import net.oliver.database.IDatabaseManager;

public class Worker {

	private static Logger log = LoggerFactory.getLogger(Worker.class);
	
	private IMessageSender sender;
	private IDatabaseManager db = DBManager.getInstance();
	private Timer timer = new Timer();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public Worker(IMessageSender sender) {
		this.sender = sender;
	}

	boolean running = true;

	private long getPeriod(String data) {

		try {
			Calendar calendar = Calendar.getInstance();
			Date date = dateFormat.parse(data);
			calendar.setTime(date);
			Calendar calendarNew = Calendar.getInstance();
			calendarNew.setTime(new Date());

			long timeOne = calendar.getTimeInMillis();
			long timeTwo = calendarNew.getTimeInMillis();
			long minute = (timeTwo - timeOne) / (1000 * 60);// ×ª»¯minute
			return minute;
		} catch (ParseException e) {
		}
		return 0;
	}

	private int internal = 0;

	public void start() {
		// loop query
		timer.schedule(new TimerTask() {
			public void run() {
//				long start = System.currentTimeMillis();
				List<Map> result;
				try {
					result = db.select(
							"select TicketNbr,Updated_By,Last_Update from dbo.v_rpt_Service With(NoLock) where Closed_Flag = 0 and status_description = 'In Progress'");

					Map<String,List<String>> data = new HashMap();
					for (Map row : result) {
						String channel = (String) row.get("Updated_By");// person
						String time = (String) row.get("Last_Update");// time

						if (channel == null)
							continue;

						List<String> tickets = data.get(channel);
						if (tickets == null) {
							tickets = new ArrayList<String>();
							data.put(channel, tickets);
						}
						// great or less?
//						if (getPeriod(time) > internal) {
							tickets.add((String) row.get("TicketNbr"));
//						}
					}
					// publish
//					for (Iterator<Map.Entry<String, Bunch>> iter = temp.entrySet().iterator(); iter.hasNext();) {
//						Entry entry = iter.next();
						sender.publish(data);
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				long c = System.currentTimeMillis() - start;
//				System.out.println("Time:    "+c);
			}
		}, 1000, 10000);// 10 seconds
	}

	public void stop() {
		timer.cancel();
	}
}

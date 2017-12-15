package net.oliver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import connectwise.client.ApiClient;
import connectwise.client.ApiException;
import connectwise.client.Response;
import connectwise.client.common.model.CustomFieldValue;
import connectwise.client.common.model.PatchOperation;
import connectwise.client.company.api.ContactsApi;
import connectwise.client.company.model.Contact;

public class SimpleRestCaller {

	private static Logger log = LoggerFactory.getLogger(SimpleRestCaller.class);

	private static ApiClient client;
	static String companyname = "evolveit";// "training";//evolveit
	static String public_key = "g9cxUUYwepM0Kf43";// "y6sR3iL1ora8fTOs";
	static String private_key = "2Eo7xySgt8fSb85I";
	static String rest_basepath = "2Eo7xySgt8fSb85I";// "moF7NvBATrfkBtXk";
	static int timePos1 = 6;
	static int timePos2 = 7;
	private static ContactsApi contactsApi;

	public static int[] csvSeq = { 6, 9, 13 };
	public static int[] fieldSeq = { 21, 22, 23, 24, 25 };
	static List fieldsList;
	
	public static DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
	public static DateTimeFormatter format2 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	public static String filepath = "./1.csv";
	
	static {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./config/config.properties")));

			companyname = prop.getProperty("rest_companyname");
			public_key = prop.getProperty("rest_publickey");
			private_key = prop.getProperty("rest_privatekey");
			rest_basepath = prop.getProperty("rest_basepath");
			timePos1 = Integer.parseInt(prop.getProperty("timePos1"));
			timePos2 = Integer.parseInt(prop.getProperty("timePos2"));
			filepath = prop.getProperty("filepath");
			String[] ar1 = prop.getProperty("csvSeq").split(",");
			String[] ar2 = prop.getProperty("fieldSeq").split(",");

			csvSeq = new int[ar1.length];
			fieldSeq = new int[ar2.length];

			for (int i = 0; i < csvSeq.length; i++) {
				csvSeq[i] = Integer.parseInt(ar1[i]);
			}
			for (int i = 0; i < fieldSeq.length; i++) {
				fieldSeq[i] = Integer.parseInt(ar2[i]);
			}
			fieldsList = Arrays.asList(fieldSeq);

		} catch (IOException e) {
			e.printStackTrace();
		}
		client = new ApiClient();
		client.setCompanyName(companyname);
		client.setPublicKey(public_key);
		client.setPrivateKey(private_key);
		client.setBasePath(rest_basepath);

		contactsApi = new ContactsApi(client);
	}

	private boolean existInArray(int[] ary,int target)
	{
		for(int x :  ary)
		{
			if(x == target)
				return true;
		}
		return false;
	}
	public void updateContactById(int id, String type, String path, String value,int count) {
		log.info("Start to update contact : " + id + "  Params: " + path + " " + value);
		List<PatchOperation> ops = new ArrayList<PatchOperation>();
		PatchOperation op = new PatchOperation(type, path, value);
		ops.add(op);

		try {
			Response<Contact> response = contactsApi.updateContactById(id, ops);
			connectwise.client.common.model.Error error = response.getError();
			if (error != null) {
				log.error("Line "+count+" : Failed to update contact : " + id + " path: "+path+ " Message: " + error.getMessage());
			} else {
				log.info("Line "+count+" : Successfully update contact : " + id + " path: "+path);
			}
		} catch (ApiException e) {
		}
	}

	public static List<Date> sortDate(List<Date> list)
	{
		 list.sort((a1, a2) -> {
	            try {
	                return a2.compareTo(a1);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            return 1;
		 }
		 );
		return list;
	}
	
	private static String getTime(String value)
	{
		String[] row = value.split(" ");
		return row[SimpleRestCaller.timePos1]+" "+row[SimpleRestCaller.timePos2];
	}
	
	public static Date getDate(String time)
	{
		DateTime dt = format2.parseDateTime(time);
		return dt.toDate();
	}
	public List<String> getSortedListofCustomfield(int id,Date newDate,String newValue,int count) {
		
		
		List<Date> dateList = new ArrayList();
		dateList.add(newDate);
		
		Map<Date,String> tempMap = new HashMap<Date,String>();
		tempMap.put(newDate, newValue);
		
		List<String> result = new ArrayList<String>();
		
		List<CustomFieldValue> list;
		try {
			
			Response<Contact> contact = contactsApi.getContactById(id);
			Contact c = contact.getResult();
			list = c.getCustomFields();
			if(list==null || list.size() == 0 )
			{
				log.info("Line "+count+" : replace one row for no contactId : "+id);
				return result;
			}
			for (int i = 0; i < list.size(); i++) {
				CustomFieldValue v = list.get(i);
				// ======================================	
				if(existInArray(fieldSeq,v.getId())) {
						if(v.getValue()!=null&&!"".equals(v.getValue()))
						{
							Date date = getDate(getTime(v.getValue()));
							dateList.add(date);
							tempMap.put(date,v.getValue());
						}
				}
				// ======================================	
			}
			
		} catch (ApiException e) {
			log.error("Exception inside findemptyField method.. "+e.getMessage());
			return result;
		}
		
		dateList =  sortDate(dateList);
		if(dateList.size()>5)
		{
			int left = dateList.size() - 5;
			for(int k=0;k<left;k++)
			{
				log.info("Replace one field for more than 5");
			}
			dateList = dateList.subList(0, 5);
			
		}
		for(int i=0;i<dateList.size();i++)
		{
			Date t = dateList.get(i);
			result.add(tempMap.get(t));
		}
		return result;
	}
}

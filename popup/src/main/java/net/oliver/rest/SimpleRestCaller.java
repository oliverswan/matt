package net.oliver.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import connectwise.client.ApiClient;
import connectwise.client.ApiException;
import connectwise.client.Response;
import connectwise.client.common.model.CustomFieldValue;
import connectwise.client.company.api.ContactsApi;
import connectwise.client.company.model.Contact;
import net.oliver.app.app6.Configuration;

public class SimpleRestCaller {

	private static Logger log = LoggerFactory.getLogger(SimpleRestCaller.class);

	private static ApiClient client;
	private static ContactsApi contactsApi;

	static {
		client = new ApiClient();
		client.setCompanyName(Configuration.companyname);
		client.setPublicKey(Configuration.public_key);
		client.setPrivateKey(Configuration.private_key);
		client.setBasePath(Configuration.rest_basepath);

		contactsApi = new ContactsApi(client);
	}

	public static void getCSATList(List<String> result,int contactId) {
		List<CustomFieldValue> list;
		try {
			Response<Contact> contact = contactsApi.getContactById(contactId);
			Contact c = contact.getResult();
			list = c.getCustomFields();
			
			for (int i = 0; i < list.size(); i++) {
				CustomFieldValue v = list.get(i);
				// ======================================	
				if(v.getValue()!=null&&v.getValue().indexOf("result on ticket")>-1)
				{
					result.add(v.getValue());
				}
				// ======================================	
			}
		} catch (ApiException e) {
			e.printStackTrace();
			log.error("");
		}
	}
	
/*	public void updateContactById(int id, String type, String path, String value) {
		log.info("Start to update contact : " + id + "  Params: " + path + " " + value);
		List<PatchOperation> ops = new ArrayList<PatchOperation>();
		PatchOperation op = new PatchOperation(type, path, value);
		ops.add(op);

		try {
			Response<Contact> response = contactsApi.updateContactById(id, ops);
			connectwise.client.common.model.Error error = response.getError();
			if (error != null) {
				log.error("Failed to update contact : " + id + " Message: " + error.getMessage());
			} else {
				log.info("Successfully update contact : " + id);
			}
		} catch (ApiException e) {
		}
	}

	private static Date findOldestDate(List<Date> list,Date d)
	{
		if (list == null || list.size() <= 0) {
			return null;
		}
		long gap = Long.MIN_VALUE;
		Date r = null;
		long time = d.getTime();
		for (Date t : list) {
			long tm = Math.abs(time - t.getTime());
			if (gap < tm) {
				gap = tm;
				r = t;
			}
		}
		return r;
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
	*/
}

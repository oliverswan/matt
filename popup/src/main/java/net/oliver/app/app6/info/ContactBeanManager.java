package net.oliver.app.app6.info;

import java.util.List;
import java.util.Map;

import net.oliver.app.app6.Configuration;
import net.oliver.database.DBManager;

public class ContactBeanManager {

	
	private static DBManager db = DBManager.getInstance();
	
	
	public static String makeLink(String display,String linkvalue,String linkVar,String link)
	{
		link = link.replaceAll(linkVar, linkvalue);
		return "<a href="+link+">"+display+"</a>";
	}
	
	
	public static ContactBean  getBean(String ticketid) {
		ContactBean bean = new ContactBean();
		bean.setTicketId(ticketid);
		
		//0.Contact Table
		String contactTableSql1 = "select contact_recid from dbo.v_rpt_Service With(NoLock) where TicketNbr = '"+ticketid+"'";
		
		List<Map> temp;
		try {
			temp = db.select(contactTableSql1);
			if(temp.size()>0)
			{
				bean.setId((String) temp.get(0).get("contact_recid"));
			}
			// ticketList
			String ticketsSql = "select TicketNbr,status_description,board_name,summary from dbo.v_rpt_Service With(NoLock) where Closed_Flag = '0' and contact_recid='"+bean.getId()+"'";
			temp = db.select(ticketsSql);
			if(temp.size()>0)
			{
				for(int j=0;j<temp.size();j++)
				{
					
					Map row = temp.get(j);
					StringBuilder sb = new StringBuilder();
					sb.append(makeLink((String)row.get("TicketNbr"),(String)row.get("TicketNbr"),"\\$\\$\\$",Configuration.getValue("ticketHref")));
					sb.append(" ");
					sb.append((String)row.get("board_name"));
					sb.append(" ");
					sb.append((String)row.get("status_description"));
					sb.append(" ");
					String summary = (String)row.get("summary");
					if(summary.length()>30)
					{
						summary = summary.substring(0,29);
					}
					sb.append(summary);
					bean.getTickets().add(sb.toString());
				}
			}
			// csats
			String csatsSql = "select User_Defined_Field_Value from Contact_User_Defined_Field_Value With(NoLock) where contact_recid = '"+bean.getId()+"'";
			temp = db.select(csatsSql);
			if(temp.size()>0)
			{
				for(int j=0;j<temp.size();j++)
				{
					Map row = temp.get(j);
					bean.getCsats().add((String)row.get("User_Defined_Field_Value"));
				}
			}
			
			// Title
			String contactTableSql2 = "select Title,company_recid,contact_type_recid,reports_to_name,userfield4,company_address_recid,first_name,last_name from Contact where Contact_RecID =  '"+bean.getId()+"'";
			temp = db.select(contactTableSql2);
			if(temp.size()>0)
			{
				bean.setTitle((String) temp.get(0).get("Title"));
				bean.setReportsTo((String) temp.get(0).get("reports_to_name"));
				bean.setPreferContact((String) temp.get(0).get("userfield4"));
				bean.setFirstName((String) temp.get(0).get("first_name"));
				bean.setLastName((String) temp.get(0).get("last_name"));
				String companyId = (String) temp.get(0).get("company_recid");
				String contact_type_recid = (String) temp.get(0).get("contact_type_recid");
				String company_address_recid = (String) temp.get(0).get("company_address_recid");
				//Company Name
				if(companyId!=null&&!"".equals(companyId))
				{
					String companyTable = "select company_name,phonenbr from company where company_RecID =  '"+companyId+"'";
					temp = db.select(companyTable);
					if(temp.size()>0)
					{
						bean.setCompanyName((String) temp.get(0).get("company_name"));
						bean.setCompanyNum((String) temp.get(0).get("phonenbr"));
					}
				}
				
				//contact Type description
				if(contact_type_recid!=null&&!"".equals(contact_type_recid))
				{
					String contactTypeDes = "select  description from contact_type where contact_type_recid =  '"+contact_type_recid+"'";
					temp = db.select(contactTypeDes);
					if(temp.size()>0)
					{
						bean.setContactType((String) temp.get(0).get("description"));
					}
				}
				
				// contact communication
				String communicationSql = "select  communication_type_recid,description from contact_communication where Contact_RecID =  '"+bean.getId()+"'";
				temp = db.select(communicationSql);
				if(temp.size()>0)
				{
					for(Map row : temp)
					{
						if(((String)row.get("communication_type_recid")).equals("14"))
						{
							bean.setEmail((String)row.get("description"));
						}else if(((String)row.get("communication_type_recid")).equals("21")){
							bean.setDirectNum((String)row.get("description"));
						}else if(((String)row.get("communication_type_recid")).equals("23")){
							bean.setMobile((String)row.get("description"));
						}
					}
				}
				if(company_address_recid!=null&&!"".equals(company_address_recid))
				{
					// company address
					String companyAddressSql = "select address_line1,address_line2,city,state_id,zip description from company_address where company_address_recid =  '"+company_address_recid+"'";
					temp = db.select(companyAddressSql);
					if(temp.size()>0)
					{
						StringBuilder sb = new StringBuilder();
						sb.append("Site: ");
						sb.append((String) temp.get(0).get("address_line1"));
						sb.append((String) temp.get(0).get("address_line2"));
						sb.append((String) temp.get(0).get("city"));
						sb.append((String) temp.get(0).get("state_id"));
						sb.append((String) temp.get(0).get("zip"));
					}
				}
			}
			
//			SimpleRestCaller.getCSATList(bean.getCsats(),Integer.parseInt(bean.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
			
			
			
}

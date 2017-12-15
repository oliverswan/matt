package net.oliver.app.app6.info;

import java.util.ArrayList;
import java.util.List;

public class ContactBean {

	private String ticketId;
	private String id;
	private String firstName;
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	private String title;
	private String companyName;
	private String contactType;
	private String reportsTo;
	private String email;
	private String directNum;
	private String mobile;
	private String companyNum;
	private String companyAddress;
	private List<String> csats = new ArrayList<String>();
	private List<String> tickets = new ArrayList<String>();
	private String preferContact;

	public String getPreferContact() {
		return preferContact;
	}

	public void setPreferContact(String preferContact) {
		this.preferContact = preferContact;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDirectNum() {
		return directNum;
	}

	public void setDirectNum(String directNum) {
		this.directNum = directNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompanyNum() {
		return companyNum;
	}

	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public List<String> getCsats() {
		return csats;
	}

	public void setCsats(List<String> csats) {
		this.csats = csats;
	}

	public List<String> getTickets() {
		return tickets;
	}

	public void setTickets(List<String> tickets) {
		this.tickets = tickets;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		sb.append(this.getFirstName()).append(" ").append(this.getLastName()).append("<br/>");
//		sb.append(this.getTitle()).append("<br/>");
		sb.append(this.getFirstName()).append(" is a ").append(this.getContactType()).append(" who reports to ")
				.append(this.getReportsTo()).append("<br/>");
		sb.append("Preferred contact method is ").append(this.getPreferContact());
		sb.append("<br/>");
		sb.append("<br/>");
		sb.append("Email: ").append(this.getEmail()).append("<br/>");
		sb.append("Direct: ").append(this.getDirectNum()).append("<br/>");
		sb.append("Mobile: ").append(this.getMobile()).append("<br/>");
		sb.append("Company: ").append(this.getCompanyNum()).append("<br/>");
		sb.append("Site: ").append(this.getCompanyAddress()).append("<br/>");
		sb.append("<br/>");
		sb.append("<br/>");
		sb.append("Recent CSAT Results").append("<br/>");
		for (String str : this.getCsats()) {
			sb.append(str).append("<br/>");
		}
		sb.append(this.getFirstName()).append(" has ").append(this.getTickets().size()).append(" Open tickets")
				.append("<br/>");
		if (this.getTickets().size() > 0) {
			sb.append("<table border='2'>");
			for (String str : this.getTickets()) {
				sb.append("<tr><td>");
				sb.append(str).append("<br/>");
				sb.append("</td></tr>");
			}
			sb.append("</table>");
		}
		return sb.toString();
	}

}

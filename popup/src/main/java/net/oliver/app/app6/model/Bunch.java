package net.oliver.app.app6.model;

import java.util.ArrayList;
import java.util.List;

public class Bunch {

	private String channel;
	private List<String> tickets = new ArrayList<String>();

	public Bunch(String channel) {
		this.channel = channel;
	}

	public void addTicket(String id)
	{
		this.tickets.add(id);
	}
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public List<String> getTickets() {
		return tickets;
	}

	public void setTickets(List<String> tickets) {
		this.tickets = tickets;
	}
}

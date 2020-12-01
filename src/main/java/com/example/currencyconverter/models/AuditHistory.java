package com.example.currencyconverter.models;

import java.util.LinkedList;

public class AuditHistory {
	
	private LinkedList<AuditEntry> auditEntries;
	
	public AuditHistory(){
		this.auditEntries = new LinkedList<>();
	}
	
	public LinkedList<AuditEntry> getAuditEntries() {
		return auditEntries;
	}

	public void setAuditEntries(LinkedList<AuditEntry> auditEntries) {
		this.auditEntries = auditEntries;
	}
	
	public void addNewAuditEntry(String queryString) {
		AuditEntry newEntry = createNewAuditEntry(queryString);
		if (this.auditEntries.size() == 10)
			this.auditEntries.removeLast();

		this.auditEntries.addFirst(newEntry);
	}
	
	private AuditEntry createNewAuditEntry(String queryString) {
		return new AuditEntry(queryString);
	}
	
	public String getFormattedString() {
		StringBuilder formatted = new StringBuilder();
		getAuditEntries().stream()
				.forEach((p) -> formatted.append(p.getQueryString())
						.append("\n"));
		return formatted.toString();

	}
}
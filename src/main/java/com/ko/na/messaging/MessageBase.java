package com.ko.na.messaging;

/**
 * A base object from which all message objects descend.
 * 
 * @author Daniel R. Creager
 */
public class MessageBase implements java.io.Serializable {
	private static final long serialVersionUID = 3789266765932384810L;

	/**
	 * The data content (payload) of a message.
	 */
	protected Object payload;

	/**
	 * Transactional metrics
	 */
	protected long created;
	protected long received;
	protected long acknowledged;

	public MessageBase() {
		setCreated();
	} // end constructor

	public long getAcknowledged() {
		return acknowledged;
	}

	public long getCreated() {
		return created;
	}

	public Object getPayload() {
		return payload;
	}

	public long getReceived() {
		return received;
	}

	public void setAcknowledged() {
		this.acknowledged = System.currentTimeMillis();
	}

	public void setCreated() {
		this.created = System.currentTimeMillis();
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public void setReceived() {
		this.received = System.currentTimeMillis();
	}

} // end class

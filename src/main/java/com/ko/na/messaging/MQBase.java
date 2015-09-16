package com.ko.na.messaging;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQObjectMessage;

/**
 * A base object from which all ActiveMQ objects descend. The default queue is
 * the SEND_QUEUE. The default configuration is to use a temporary "in-memory"
 * queue that only exists for the life of this object.
 * 
 * @author Daniel R. Creager
 */
public class MQBase {
	public static final String SEND_QUEUE = "FamilyTree.Request";
	public static final String RECV_QUEUE = "FamilyTree.Response";
	public static final long WAIT_INTVL = 1000L;
	public static final String MQ_BROKER = "tcp://localhost:61616";
	public static final String MQ_VM = "vm://localhost";

	public static final int STANDARD = 0;
	public static final int TEMP = 1;
	protected ActiveMQConnectionFactory connectionFactory;

	protected Destination stndDest;
	protected Destination tempDest;

	protected String queue;
	protected int msgCount;

	protected Request request;
	
	protected Response response;

	/**
	 * Consumer[0] Standard Message Producer/Consumer Consumer[1] Temp Message
	 * Producer/Consumer for message correlation.
	 */
	protected Connection[] connection;

	protected ActiveMQSession[] session;

	protected MessageProducer[] producer;

	protected ActiveMQMessageConsumer[] consumer;

	/**
	 * A fundamental message structure without a payload
	 */
	protected Message message;
	/**
	 * A fundamental message structure without a text payload
	 */
	protected TextMessage textMessage;
	/**
	 * A message structure with an associated java object payload
	 */
	protected ObjectMessage objectMessage;
	/**
	 * A message structure with an associated ActiveMQ object payload
	 */
	protected ActiveMQObjectMessage amqObjectMessage;

	public MQBase() {
		this(SEND_QUEUE, null);
	}

	public MQBase(String queue, String selector) {
		connection = new Connection[2];
		session = new ActiveMQSession[2];
		consumer = new ActiveMQMessageConsumer[2];
		producer = new MessageProducer[2];
		msgCount = 1;

		try {
			// Create a ConnectionFactory
			connectionFactory = new ActiveMQConnectionFactory(MQ_BROKER);

			// Create Connections
			connection[STANDARD] = connectionFactory.createConnection();
			connection[STANDARD].start();
			connection[TEMP] = connectionFactory.createConnection();
			connection[TEMP].start();

			// Create Sessions
			session[STANDARD] = (ActiveMQSession) connection[STANDARD].createSession(false,
					ActiveMQSession.AUTO_ACKNOWLEDGE);
			session[TEMP] = (ActiveMQSession) connection[TEMP].createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);

			// Set the destination queue
			this.queue = queue;

			// Create the Standard Destination Queue
			setStndDest(session[STANDARD].createQueue(queue));

			// Create a MessageProducer from the Session to a Queue
			producer[STANDARD] = session[STANDARD].createProducer(getStndDest());
			producer[STANDARD].setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create a MessageConsumer from the Session to a Queue
			consumer[STANDARD] = (ActiveMQMessageConsumer) session[STANDARD].createConsumer(getStndDest(), selector);

		} catch (javax.jms.JMSException ex1) {
			System.out.println("Caught: " + ex1 + "\nPremature termination required.");
			System.exit(-1);

		} catch (Exception ex2) {
			System.out.println("Caught: " + ex2);
			ex2.printStackTrace();
		} // end try/catch

	} // end constructor

	public ActiveMQObjectMessage getAmqObjectMessage() {
		return amqObjectMessage;
	}

	public ActiveMQMessageConsumer getConsumer(int queueType) {
		return consumer[queueType];
	}

	public Message getMessage() {
		return message;
	}

	public ObjectMessage getObjectMessage() {
		return objectMessage;
	}

	public MessageProducer getProducer(int queueType) {
		return producer[queueType];
	}

	public Request getRequest() {
		return request;
	}

	public Response getResponse() {
		return response;
	}

	public Destination getStndDest() {
		return stndDest;
	}

	public Destination getTempDest() {
		return tempDest;
	}

	public TextMessage getTextMessage() {
		return textMessage;
	}

	public void onRequestMessage(Message msg, boolean debug) {
		try {
			if (debug) System.out.println(msg);

			setMessage(msg);
			setTempQueue(msg.getJMSReplyTo(), false);
			if (msg instanceof TextMessage) {
				request = new Request();
				request.setPayload(((TextMessage) msg).getText());

			} else if (msg instanceof ActiveMQObjectMessage) {
				setAmqObjectMessage((ActiveMQObjectMessage) msg);
				request = (Request) ((ActiveMQObjectMessage) msg).getObject();

			} else if (msg instanceof ObjectMessage) {
				setObjectMessage((ObjectMessage) msg);
				request = (Request) ((ObjectMessage) msg).getObject();

			} // end if/else

		} catch (Exception ex) {
			System.out.println("Caught: " + ex);
			if (debug) ex.printStackTrace();

		} // end try/catch

	}

	public void onResponseMessage(Message msg, boolean debug) {
		try {
			if (debug) System.out.println(msg);

			setMessage(msg);
			if (msg instanceof TextMessage) {
				response = new Response();
				response.setPayload(((TextMessage) msg).getText());

			} else if (msg instanceof ActiveMQObjectMessage) {
				setAmqObjectMessage((ActiveMQObjectMessage) msg);
				response = (Response) ((ActiveMQObjectMessage) msg).getObject();

			} else if (msg instanceof ObjectMessage) {
				setObjectMessage((ObjectMessage) msg);
				response = (Response) ((ObjectMessage) msg).getObject();

			} // end if/else

		} catch (Exception ex) {
			System.out.println("Caught: " + ex);
			if (debug) ex.printStackTrace();

		} // end try/catch

	}

	public void setAmqObjectMessage(ActiveMQObjectMessage aMQobjectMessage) {
		this.amqObjectMessage = aMQobjectMessage;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public void setObjectMessage(ObjectMessage objectMessage) {
		this.objectMessage = objectMessage;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public void setStndDest(Destination stndDest) {
		this.stndDest = stndDest;
	}

	public void setTempDest(Destination stndDest) {
		this.tempDest = stndDest;
	}

	public Destination setTempQueue() throws JMSException {
		Destination tempDest = session[TEMP].createTemporaryQueue();
		setTempQueue(tempDest, true);
		return tempDest;
	}

	public void setTempQueue(Destination dest, boolean consumerFlg) throws JMSException {
		setTempDest(dest);
		if (consumerFlg) {
			consumer[TEMP] = (ActiveMQMessageConsumer) session[TEMP].createConsumer(dest);
		}
		producer[TEMP] = session[TEMP].createProducer(dest);
		producer[TEMP].setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}

	public void setTextMessage(TextMessage textMessage) {
		this.textMessage = textMessage;
	}

	public void terminate() {
		try {
			if (producer[STANDARD] != null) {
				producer[STANDARD].close();
				producer[STANDARD] = null;
			}
			if (producer[TEMP] != null) {
				producer[TEMP].close();
				producer[TEMP] = null;
			}
			if (consumer[STANDARD] != null) {
				consumer[STANDARD].close();
				consumer[STANDARD] = null;
			}
			if (consumer[TEMP] != null) {
				consumer[TEMP].close();
				consumer[TEMP] = null;
			}
			if (session[STANDARD] != null) {
				session[STANDARD].close();
				session[STANDARD] = null;
			}
			if (session[TEMP] != null) {
				session[TEMP].close();
				session[TEMP] = null;
			}
			if (connection[STANDARD] != null) {
				connection[STANDARD].stop();
				connection[STANDARD].close();
				connection[STANDARD] = null;
			}
			if (connection[TEMP] != null) {
				connection[TEMP].stop();
				connection[TEMP].close();
				connection[TEMP] = null;
			}
			connectionFactory = null;

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		} // end try/catch
	} // end terminate() method

} // end MQBase class

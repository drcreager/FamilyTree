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
	public static final long WAIT_INTVL = 1000L;
	public static final String MQ_BROKER = "tcp://localhost:61616";
	public static final String MQ_VM = "vm://localhost";

	public static final String[] CLIENT_QUEUE_NAMES = {"FamilyTree.Request", "FamilyTree.Response"};
	public static final String[] PROVDR_QUEUE_NAMES = {"FamilyTree.Response", "FamilyTree.Request"};
	public static final int PRIMARY = 0;
	public static final int SECONDARY = 1;
	
	protected ActiveMQConnectionFactory connectionFactory;

	protected Destination priDest;
	protected Destination secDest;

	protected String[] queueName;
	protected int msgCount;

	protected Request request;
	protected Response response;

	/**
	 * Consumer[0] Standard Message Producer/Consumer Consumer[1] Temp Message
	 * Producer/Consumer for message correlation.
	 */
	protected Connection                connection;
	protected ActiveMQSession           session;
	protected MessageProducer[]         producer;
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
		this(CLIENT_QUEUE_NAMES, null);
	}

	public MQBase(String[] queue, String selector) {
		consumer = new ActiveMQMessageConsumer[2];
		producer = new MessageProducer[2];
		queueName = new String[2];
		msgCount = 1;
		

		try {
			// Create a ConnectionFactory
			connectionFactory = new ActiveMQConnectionFactory(MQ_BROKER);

			// Create Connections
			connection = connectionFactory.createConnection();
			connection.start();

			// Create Sessions
			session = (ActiveMQSession) connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);

			// Set the request queue name
			queueName[0] = queue[0];
			queueName[1] = queue[1];

			// Create the Standard Destination Queue
			setPriDest(session.createQueue(queueName[PRIMARY]));
			setSecDest(session.createQueue(queueName[SECONDARY]));

			// Create a MessageProducer from the Session to a Queue
			producer[PRIMARY] = session.createProducer(getPriDest());
			producer[PRIMARY].setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create a MessageConsumer from the Session to a Queue
			consumer[SECONDARY] = (ActiveMQMessageConsumer) session.createConsumer(getSecDest(), selector);
			
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

	public Destination getPriDest() {
		return priDest;
	}

	public Destination getSecDest() {
		return secDest;
	}

	public TextMessage getTextMessage() {
		return textMessage;
	}

	public void onRequestMessage(Message msg, boolean debug) {
		try {
			if (debug) System.out.println(msg);

			setMessage(msg);
			//setTempQueue(msg.getJMSReplyTo(), false);
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

	public void setPriDest(Destination dest) {
		this.priDest = dest;
	}

	public void setSecDest(Destination dest) {
		this.secDest = dest;
	}

	public void setTextMessage(TextMessage textMessage) {
		this.textMessage = textMessage;
	}

	public void terminate() {
		try {
			if (producer[PRIMARY] != null) {
				producer[PRIMARY].close();
				producer[PRIMARY] = null;
			}
			if (producer[SECONDARY] != null) {
				producer[SECONDARY].close();
				producer[SECONDARY] = null;
			}
			if (consumer[PRIMARY] != null) {
				consumer[PRIMARY].close();
				consumer[PRIMARY] = null;
			}
			if (consumer[SECONDARY] != null) {
				consumer[SECONDARY].close();
				consumer[SECONDARY] = null;
			}
			if (session != null) {
				session.close();
				session = null;
			}

			if (connection != null) {
				connection.stop();
				connection.close();
				connection = null;
			}
			connectionFactory = null;

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		} // end try/catch
	} // end terminate() method

} // end MQBase class

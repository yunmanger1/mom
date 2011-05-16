package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;

import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISimpleEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.RISimpleEJB;

import org.apache.log4j.Logger;

@Stateless
public class SimpleEJB implements RISimpleEJB, LISimpleEJB {
	public static final String CONNECTION_FACTORY_NAME = "java:/ConnectionFactory";
	public static final String DESTINATION_NAME = "/queue/EventQueue";
	private final Logger log = Logger.getLogger(getClass());

	// @PersistenceContext(unitName = "mom")
	// EntityManager em;

	@Resource(mappedName = CONNECTION_FACTORY_NAME)
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = DESTINATION_NAME)
	private Queue destination;

	protected Connection connection;

	@PostConstruct
	public void initialize() {
		try {
			connection = connectionFactory.createConnection();
		} catch (JMSException e) {
			log.error("JMS init crash");
			log.trace(e);
		}
	}

	@PreDestroy
	public void destroy() {
		try {
			connection.close();
		} catch (Exception e) {
			log.trace(e);
		}
	}

	@Override
	public void sayHello(String message) {
		System.out.println(message);
	}

	// @Override
	// public void sayHello() {
	// String msg = "Hello, world!";
	// log.debug(msg);
	// System.out.println(msg);
	// }
	//
	// @Override
	// public boolean sendHelloMessage() {
	// try {
	// Session session = connection.createSession(false,
	// Session.AUTO_ACKNOWLEDGE);
	// try {
	// MessageProducer producer = session.createProducer(destination);
	//
	// TextMessage message = session.createTextMessage();
	// message.setText("Hello, world!");
	// message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
	// producer.send(message);
	// producer.close();
	// log.debug("Success sending");
	// System.out.println("Success sending");
	// } finally {
	// session.close();
	// }
	// return true;
	// } catch (JMSException e) {
	// log.error("JMS crash");
	// log.trace(e);
	// }
	// return false;
	// }
	//
	// public boolean sendObjectMessage(Serializable o) {
	// try {
	// Session session = connection.createSession(false,
	// Session.AUTO_ACKNOWLEDGE);
	// try {
	// MessageProducer producer = session.createProducer(destination);
	//
	// ObjectMessage message = session.createObjectMessage();
	// message.setStringProperty(AppProps.MESSAGE_TYPE,
	// MessageType.USER_MESSAGE.toString());
	// message.setObject(o);
	// message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
	// producer.send(message);
	// producer.close();
	// log.debug("Success sending");
	// System.out.println("Success sending");
	// } finally {
	// session.close();
	// }
	// return true;
	// } catch (JMSException e) {
	// log.error("JMS crash");
	// log.trace(e);
	// }
	// return false;
	// }
	//
	// @Override
	// public UserEntity getUser(String username) {
	// Query q = em.createQuery(
	// "SELECT u FROM UserEntity u WHERE u.username = :username")
	// .setParameter("username", username);
	// UserEntity user = null;
	// try {
	// user = (UserEntity) q.getSingleResult();
	// } catch (Exception e) {
	// log.debug("no such user: " + username);
	// }
	// return user;
	// }
	//
	// @Override
	// public boolean sendMessage(UserEntity from, UserEntity to, String text) {
	// if (from != null && to != null) {
	// MessageDTO dto = new MessageDTO();
	// dto.setFromUsername(from.getUsername());
	// dto.setToUsername(to.getUsername());
	// dto.setText(text);
	// return sendObjectMessage(dto);
	// }
	// return false;
	// }
	//
	// @Override
	// public boolean sendMessageDirectly(UserEntity from, UserEntity to,
	// String text) {
	// try {
	// MessageEntity m = new MessageEntity();
	// m.setFrom(from);
	// m.setTo(to);
	// m.setText(text);
	// m.setDate(new Date(System.currentTimeMillis()));
	// em.persist(m);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }
	//
	// @Override
	// public boolean sendMessageFromAdmin(UserEntity user, String text) {
	// UserEntity admin = getUser("admin");
	// return sendMessageDirectly(admin, user, text);
	// }
	//
	// @Override
	// public List<MessageEntity> getAllMessages() {
	// try {
	// return em.createQuery(
	// "select m from MessageEntity m order by m.date desc")
	// .getResultList();
	// } catch (Exception e) {
	// return new ArrayList<MessageEntity>();
	// }
	// }
	//
	// @Override
	// public List<MessageEntity> getAllUserMessages(String username) {
	// try {
	// return em
	// .createQuery(
	// "select m from MessageEntity m where m.from.username = ?1 or m.to.username = ?1 order by m.date desc")
	// .setParameter(1, username).getResultList();
	// } catch (Exception e) {
	// return new ArrayList<MessageEntity>();
	// }
	// }
	//
	// @Override
	// public List<UserEntity> getAllUsers() {
	// return em.createQuery(
	// "select m from UserEntity m order by m.username asc")
	// .getResultList();
	// }
	//
	// @Override
	// public UserEntity createUser(String username, String email) {
	// if (username != null && email != null) {
	// UserEntity user = new UserEntity();
	// user.setUsername(username);
	// user.setEmail(email);
	// em.persist(user);
	// em.flush();
	// return user;
	// }
	// return null;
	// }

}

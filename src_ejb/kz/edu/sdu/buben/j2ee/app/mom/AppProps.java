package kz.edu.sdu.buben.j2ee.app.mom;

public class AppProps {
	public static final String MESSAGE_TYPE = "MESSAGE_TYPE";

	public static final String CHANGE_BALANCE_MT = "CHANGE_BALANCE";
	public static final String CHANGE_ACCOUNT_MT = "CHANGE_ACCOUNT";

	public static final String BALANCE_QUEUE_NAME = "/queue/BalanceQueue";
	public static final String EVENT_QUEUE_NAME = "/queue/EventQueue";
	public static final String CHANGES_QUEUE_NAME = "/queue/ChangesQueue";
	public static final String NONE_QUEUE_NAME = "/queue/NoneQueue";
	public static final String CONNECTION_FACTORY_NAME = "java:/ConnectionFactory";

	public static final String FORWARD_FROM = "FORWARD_FROM";

}

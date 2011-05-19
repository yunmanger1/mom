package kz.edu.sdu.buben.j2ee.app.mom;

public class AppProps {
	public static final String MESSAGE_TYPE = "MESSAGE_TYPE";

	public static final String CHANGE_BALANCE_MT = "CHANGE_BALANCE";
	public static final String CHANGE_ACCOUNT_MT = "CHANGE_ACCOUNT";
	public static final String CHARGE_SESSION_MT = "CHARGE_SESSION";

	public static final String BALANCE_QUEUE_NAME = "/queue/BalanceQueue";
	public static final String EVENT_QUEUE_NAME = "/queue/EventQueue";
	public static final String CHANGES_QUEUE_NAME = "/queue/ChangesQueue";
	public static final String NONE_QUEUE_NAME = "/queue/NoneQueue";
	public static final String CONNECTION_FACTORY_NAME = "java:/ConnectionFactory";

	public static final String FORWARD_FROM = "FORWARD_FROM";

	public static final int BEFORE_SESSION_STATUS = 0;
	public static final int ACTIVE_SESSION_STATUS = 1;
	public static final int OVER_SESSION_STATUS = 2;
	public static final int DELETED_SESSION_STATUS = 3;
	public static final int HISTORY_SESSION_STATUS = 4;

	public static final String BALANCE_ACCOUNT_CHANGE = "BALANCE_CHANGE";
	public static final String CREATED_ACCOUNT_CHANGE = "CREATED_ACCOUNT";

}

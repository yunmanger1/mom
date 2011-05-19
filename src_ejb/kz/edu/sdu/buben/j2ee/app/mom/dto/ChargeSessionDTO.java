package kz.edu.sdu.buben.j2ee.app.mom.dto;

public class ChargeSessionDTO {
	public static final int CALL_SESSION = 0;
	public static final int G3_SESSION = 1;

	protected int session_type;
	protected int session_id;

	public int getSession_type() {
		return session_type;
	}

	public void setSession_type(int sessionType) {
		session_type = sessionType;
	}

	public int getSession_id() {
		return session_id;
	}

	public void setSession_id(int sessionId) {
		session_id = sessionId;
	}

}

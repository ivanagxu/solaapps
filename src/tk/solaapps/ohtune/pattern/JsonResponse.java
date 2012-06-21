package tk.solaapps.ohtune.pattern;

public class JsonResponse {
	private String msg;
	private boolean success;
	private Object data;
	public JsonResponse(boolean success, String msg, Object data)
	{
		this.msg = msg;
		this.success = success;
		this.data = data;
	}
}

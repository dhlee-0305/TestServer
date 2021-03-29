import com.sun.net.httpserver.HttpExchange;

public class HttpRequestHandler {

	HttpExchange request = null;
	
	public HttpRequestHandler(HttpExchange request) {
		this.request = request;
	}
	
	public String responseBody(){
		StringBuilder response = new StringBuilder();
		HttpService httpService;
		
		System.out.println("\nMethod:"+request.getRequestMethod());
		System.out.println("Path:"+request.getRequestURI().getPath());
		
		// echo 서비스
		if("/echo".equals(request.getRequestURI().getPath())) {
			httpService = new EchoService(request);
		}
		else // JsonFile 서비스 
		{
			httpService = new JsonFileService(request);
		}

		return httpService.handle();
	}
	
}

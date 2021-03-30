import com.sun.net.httpserver.HttpExchange;

public class HttpRequestHandler {

	HttpExchange request = null;
	
	public HttpRequestHandler(HttpExchange request) {
		this.request = request;
	}
	
	public String responseBody(){
		HttpService httpService = null;
		
		System.out.println("\n----------------------------------------------------------------");
		System.out.println("Method:"+request.getRequestMethod());
		System.out.println("Path:"+request.getRequestURI().getPath());
		
		// echo 서비스
		if("/echo".equals(request.getRequestURI().getPath())) {
			httpService = new EchoService(request);
		}
		else if("/favicon.ico".equals(request.getRequestURI().getPath())) { // favicon 예외 처리
			System.out.println("/favicon.ico IGNOR!!");
			return "{\n\"errorMessage\": \"favicon.ico ignored\"\n}";
		}
		else // JsonFile 서비스 
		{
			httpService = new JsonFileService(request);
		}

		return httpService.handle();
	}
	
}

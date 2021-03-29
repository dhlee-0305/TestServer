import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.sun.net.httpserver.HttpExchange;

public class EchoService implements HttpService{
	HttpExchange request = null;
	
	public EchoService(HttpExchange request) {
		this.request = request;
	}
	
	public String handle() {
		System.out.println("ECHO SERVICE!!");
		
		StringBuilder response = new StringBuilder();
		
		try {
			InputStreamReader isr = new InputStreamReader(request.getRequestBody(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while((line = br.readLine()) != null){
				response.append(line);
			}
			System.out.println(response.toString());
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			response.append("{\n\"errorMessage\": \"No Message\"\n}" );
		}
		
		return response.toString();
	}
}

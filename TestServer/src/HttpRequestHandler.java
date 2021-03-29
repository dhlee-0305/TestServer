import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

public class HttpRequestHandler {
	private static final String WORKDIR = "C:/Downloads/json/";
	HttpExchange request = null;
	
	public HttpRequestHandler(HttpExchange request) {
		this.request = request;
	}
	
	public String responseBody(){
		StringBuilder response = new StringBuilder();
		
		System.out.println("\nMethod:"+request.getRequestMethod());
		System.out.println("Path:"+request.getRequestURI().getPath());
		
		// echo 처리
		if("/echo".equals(request.getRequestURI().getPath())) {
			System.out.println("ECHO SERVICE!!");
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
		
		// file json 읽어와 반환
		String jsonFilePath = WORKDIR+request.getRequestURI().getPath()+".json";
		System.out.println("File Path:"+jsonFilePath);
		
		try {
		    Path path = Paths.get(jsonFilePath);
		    List<String> lines = Files.readAllLines(path);
		    for(String line : lines) {
		        System.out.println(line);
		    	response.append(line+'\n');
		    }
		} catch (IOException e) {
			System.out.println(e.getMessage());
			response.append("{\n\"errorMessage\": \"No File\"\n}" );
		}

		return response.toString();
	}
	
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

public class JsonFileService implements HttpService {
	HttpExchange request = null;
	private static final String WORKDIR = "C:/Downloads/json/";
	
	public JsonFileService(HttpExchange request) {
		this.request = request;
	}
	
	public String handle() {
		
		System.out.println("JSONFILE SERVICE!!");
		
		StringBuilder response = new StringBuilder();
		
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

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
 
public class TestServer {
    private static String DEFAULT_HOSTNAME = "localhost";
    private static int DEFAULT_PORT = 3002;
    private int DEFAULT_BACKLOG = 0;
    private HttpServer server = null;
    
    public TestServer(String host, int port) throws IOException {
        createServer(host, port);
    }
    
    private void createServer(String host, int port) throws IOException {
        // HTTP Server 생성
        this.server = HttpServer.create(new InetSocketAddress(host, port), DEFAULT_BACKLOG);
        // HTTP Server Context 설정
        server.createContext("/", new CustomHttpHandler());
    }
    
    public void start() {
        server.start();
    }
    
    public void stop(int delay) {
        server.stop(delay);
    }
    
    public static void main(String[] args) {
        
    	TestServer httpServerManager = null;
        
        try {
        	System.out.println("TEST HTTP SERVER Start!!!");
            
            // 서버 생성
            httpServerManager = new TestServer(DEFAULT_HOSTNAME, DEFAULT_PORT);
            httpServerManager.start();
            
            // Shutdown Hook
            Runtime.getRuntime().addShutdownHook(
            		new Thread(
            				new Runnable() {
            					@Override
            					public void run() {
            						//종료 로그
            						System.out.println("TEST HTTP SERVER STOP!!!");
            					}
            			}
            		)
            );
            
            // Enter를 입력하면 종료
            System.out.print("Please press 'Enter' to stop the server.");
            System.in.read();
            
        } catch (IOException e) {
        	System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            // 종료
            // 0초 대기후  종료
            httpServerManager.stop(0);
        }
    }

}
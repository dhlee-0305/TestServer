import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
        server.createContext("/", new RootHandler());
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
 
    /**
     * Sub Class
     */
    class RootHandler implements HttpHandler {    
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            
            // Initialize Response Body
            OutputStream respBody = exchange.getResponseBody();
            
            HttpRequestHandler requestHandler = new HttpRequestHandler(exchange);
            
            try {

            	// responseBody Encoding to UTF-8
                ByteBuffer responseByteBuffer = Charset.forName("UTF-8").encode(requestHandler.responseBody());
                int contentLength = responseByteBuffer.limit();
                byte[] content = new byte[contentLength];
                responseByteBuffer.get(content, 0, contentLength);
                
                // Set Response Headers
                Headers headers = exchange.getResponseHeaders();
                headers.add("Content-Type", "text/json;charset=UTF-8");
                headers.add("Content-Length", String.valueOf(contentLength));
                
                // Send Response
                exchange.sendResponseHeaders(200, contentLength);
                respBody.write(content);
                
                // Close Stream
                // 반드시, Response Header를 보낸 후에 닫아야함
                respBody.close();
                
            } catch ( IOException e ) {
                e.printStackTrace();
                
                if( respBody != null ) {
                    respBody.close();
                }
            } finally {
                exchange.close();
            }
        }
    }
}
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CustomHttpHandler implements HttpHandler {
        
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

		} catch (IOException e) {
			e.printStackTrace();

			if (respBody != null) {
				respBody.close();
			}
		} finally {
			exchange.close();
		}
	}

}

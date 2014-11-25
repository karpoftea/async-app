package org.me.web;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.me.App;

import java.io.IOException;

public class MyAsyncServlet2Test {

	private static App app = new App();

	@BeforeClass
	public static void setUp() throws Exception {
		app.start();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		app.stop();
	}

	@Test
	public void testDoPost() throws IOException {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try (CloseableHttpResponse response = httpClient.execute(new HttpPost("http://localhost:8080/asyncservlet2"))) {
			System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
		}
	}
}

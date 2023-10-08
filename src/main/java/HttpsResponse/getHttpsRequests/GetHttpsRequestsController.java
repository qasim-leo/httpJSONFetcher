package HttpsResponse.getHttpsRequests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class GetHttpsRequestsController {

	public static void main(String[] args) {
		System.out.println(getDataFromApi());
	}

	public static ResponseEntity<String> getDataFromApi() {
		String url = "https://rdap.markmonitor.com/rdap/domain/google.beer";
		final Logger log = LoggerFactory.getLogger(GetHttpsRequestsController.class);

		try {
			URL apiUrl = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
			connection.setRequestMethod("GET");

			if (connection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder responseBuilder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					responseBuilder.append(line);
				}
				reader.close();

				String responseData = responseBuilder.toString();
				connection.disconnect();

				return ResponseEntity.ok(responseData);
			} else {
				log.error("Failed to retrieve data. Response code: " + connection.getResponseCode());
				connection.disconnect();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Failed to retrieve data. Response code: " + connection.getResponseCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred: " + e.getMessage());
		}
	}
}

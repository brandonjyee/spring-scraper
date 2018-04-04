package bjy;

import java.lang.Exception;
import lombok.extern.slf4j.Slf4j;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Slf4j
public class WebScraper {

	public void scrape(String pageUrl) throws Exception {
		// Note: WebClient is not thread-safe
		try (final WebClient webClient = new WebClient()) {
			final HtmlPage page = webClient.getPage(pageUrl);

			final String pageAsXml = page.asXml();
			log.info("pageAsXml: " + pageAsXml);

			final String pageAsText = page.asText();
			log.info("pageAsText: " + pageAsText);
		}
	}
}
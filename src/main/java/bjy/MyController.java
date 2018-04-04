package bjy;

import java.lang.StringBuilder;
import java.lang.Exception;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("htmlunit-test")
public class MyController {

	@GetMapping("/form")
	public String input(Model model) {
		model.addAttribute("mymodel", new MyModel());
		// Return the name of the view template. Ex: return "form" if template is "form.html"
		return "form";
	}

	@PostMapping("/form")
	public String formSubmit(@ModelAttribute("mymodel") MyModel mymodel) {
		return "result";
	}

	@RequestMapping("/test")
	public String htmlunitTester(@RequestParam(value="url", defaultValue="google.com") String url,
				@RequestParam(value="xpath", defaultValue="//a[contains(@class, 'jobtitle')]") String xpath) {
		String ret = scrapeWithXPath(url, xpath);
		return ret;
	}

	public String scrapeWithXPath(String url, String xpath) {
		StringBuilder sb = new StringBuilder();
		try {
			String fullUrl = url;
			if (!url.startsWith("http")) {
				fullUrl = "http://" + url;
			}
			HtmlPage page = scrape(fullUrl);
			String divider = "*******************************************************************";
			sb.append(asLine("Retrieved page: " + fullUrl));
			sb.append(asLine("Using XPath expresion: " + xpath));

			List<DomNode> resListByXPath = page.getByXPath(xpath);
			if (resListByXPath == null) {
				sb.append(asLine("page.getByXPath(xpath) returned null"));
			} else {
				sb.append(asLine("page.getByXPath(xpath) list length: " + resListByXPath.size()));
				sb.append(asLine("List contents:"));
				sb.append("<xmp>");
				int childCount = 1;
				for (DomNode node : resListByXPath) {
					sb.append("Child " + childCount + " asXml():\n");
					sb.append(node.asXml());
					sb.append(divider);
					sb.append("\n");
					childCount++;
				}
				sb.append("</xmp>");
			}

			//DomNode firstByXPath = page.getFirstByXPath("");


			//sb.append(asLine("View the source code to see what's returned."));
			sb.append(asLine("page.asText()"));
			sb.append(page.asText());

			sb.append(asLine(divider));

			sb.append(asLine("page.asXml()"));
			sb.append("<xmp>");
			sb.append(page.asXml());
			sb.append("</xmp>");
		} catch (Exception e) {
			log.error("error", e);
		}
		return sb.toString();
	}

	public HtmlPage scrape(String pageUrl) throws Exception {
		// Note: WebClient is not thread-safe
		try (final WebClient webClient = new WebClient()) {
			final HtmlPage page = webClient.getPage(pageUrl);
			return page;
		}
	}

	public static String asLine(String str) {
		return "<p>" + str + "</p>\n";
	}
}
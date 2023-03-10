import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.UserAgent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();


//        JBrowserDriver driver = new JBrowserDriver(Settings
//                .builder().timezone(Timezone.ASIA_DHAKA).build());

        WebDriver driver = new JBrowserDriver(Settings.builder().headless(true).userAgent(UserAgent.CHROME).build());
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


        driver.get("http://onlinesim.io/price-list");

        Document document = Jsoup.parse( driver.getPageSource());

        Elements elements = document.select("div.country-block a");

        for (Element element : elements) {
            String id = element.id();
            if (id.equals(""))
                continue;
            id = id.substring(8);
            String countryName = element.getElementsByClass("country-name").text();
            System.out.println(countryName);

            driver.get("http://onlinesim.io/price-list?country="+ id +"&type=receive");
            Document doc = null;
            try {
                doc = Jsoup.parse(driver.getPageSource());
            }catch (Exception e){
                System.out.println(id);
                System.out.println(e.getMessage());
                continue;
            }

            //System.out.println(doc);

            Elements priceElements = doc.select("div.service-block");

            Map<String, String> serviceMap = new HashMap<String, String>();
            for (Element priceElement : priceElements) {
                serviceMap.put(priceElement.getElementsByClass("price-name").text(), priceElement.getElementsByClass("price-text").text());
            }
            result.put(countryName,serviceMap);

        }


        System.out.println(result);


    }
}

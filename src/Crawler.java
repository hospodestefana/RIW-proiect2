import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;

class Crawler {

    private LinkedList<String> queue = new LinkedList<>();
    private HashSet<String> url_visit = new HashSet<>();

    private void getLinks(Document doc) {
        Elements links = doc.select("a[href]");

        for (Element link : links) {
            if (!url_visit.contains(link.attr("href"))) {
                queue.add(link.attr("abs:href"));
            }
        }

    }

    public void Crawl() throws IOException {

        long startTime = System.currentTimeMillis();
        HttpClient httpClient = new HttpClient();
        int savedPages = 0;
        queue.add("http://riweb.tibeica.com/crawl/");

            while (!queue.isEmpty() && savedPages < 100) {
                String url = queue.remove();
                if (url_visit.contains(url)) {
                    continue;
                }

                try {
                    URL urlCurent = new URL(url);
                    String host = urlCurent.getHost();
                    String path = urlCurent.getPath();
                    if (path.equals("")) {
                        path = "/";
                    }

                    if (!urlCurent.getProtocol().equals("http")) {
                        continue;

                    }
                    httpClient.saveHtml(host, path);

                    savedPages++;
                    System.out.println(url);
                    Document doc = Jsoup.connect(url).get();
                    getLinks(doc);
                    url_visit.add(url);

                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
            long stopTime = System.currentTimeMillis();
            System.out.println("Timp: " + (stopTime - startTime)/1000);
        }
    }


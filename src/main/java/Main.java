import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        final Document originDoc = Jsoup.parse(new String(Files.readAllBytes(Paths.get(args[0]))));
        final Document targetDoc = Jsoup.parse(new String(Files.readAllBytes(Paths.get(args[1]))));
        final String id = args.length == 3 ? args[2] : "make-everything-ok-button";

        final Element elementToFind = originDoc.getElementById(id);

        System.out.println(new HtmlElementFinder().findSimilarElementPath(targetDoc, elementToFind));
    }
}

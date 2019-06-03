import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class which prints path to html element using html file and id given as program arguments.
 */
public class HtmlElementFinder {

    public static void main(String[] args) throws IOException {

        final Document doc = Jsoup.parse(new String(Files.readAllBytes(Paths.get(args[0]))));
        final String id = args[1];

        final List<String> pathElements = new ArrayList<>();

        final Element htmlElement = doc.getAllElements().first();

        System.out.println(findHtmlElementById(pathElements, htmlElement, id));
    }

    /**
     * Recursive method which finds element with given {@code id} in the {@code element} and collects parts of path to {@code pathElements}.
     *
     * @param pathElements list which recursively generated with path elements.
     * @param element      root element for current iteration of recursion.
     * @param id           attribute of element to find.
     * @return path to element with given id.
     */
    private static String findHtmlElementById(final List<String> pathElements, final Element element, final String id) {
        final List<Element> currentElements = new ArrayList<>();

        for (final Element currentElement : element.children()) {
            currentElements.add(currentElement);

            if (currentElement.getElementById(id) == null) {
                continue;
            }

            final long number = currentElements.stream()
                    .map(Element::nodeName)
                    .filter(elementName -> elementName.equals(currentElement.nodeName()))
                    .count();

            pathElements.add(currentElement.nodeName() + "[" + number + "]");

            findHtmlElementById(pathElements, currentElement, id);
        }
        return String.join(" -> ", pathElements);
    }
}

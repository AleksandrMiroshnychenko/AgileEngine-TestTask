import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

/**
 * Util class to find similar html element in a html page.
 *
 * @author A.Miroshnychenko
 */
public class HtmlElementFinder {

    /**
     * Finds the xml path to the most similar html {@code element} in {@code document}.
     *
     * @param document html document to look for a similar element.
     * @param element  html element to find.
     * @return xml path to the most similar element or message if there are no such elements.
     */
    String findSimilarElementPath(final Document document, final Element element) {
        final Map<String, String> originElementAttributes = extractElementAttributes(element);

        final Map<String, Element> foundElements = findHtmlElementByMatchingAttributes(new HashMap<>(), new ArrayList<>(), document.getAllElements().first(), originElementAttributes);

        String result = "No similar elements found.";
        long maxOfMatches = 0;

        // Choose the most similar element
        for (final String path : foundElements.keySet()) {
            final Map<String, String> foundElementAttributes = extractElementAttributes(foundElements.get(path));

            final long amountOfMatches = foundElementAttributes.keySet().stream()
                    .filter(attributeName -> foundElementAttributes.get(attributeName).equals(originElementAttributes.get(attributeName)))
                    .count();

            if (amountOfMatches > maxOfMatches) {
                maxOfMatches = amountOfMatches;
                result = path;
            }
        }

        return result;
    }

    /**
     * Extracts element attributes and the content of element as element properties.
     *
     * @param element the element from which to extract attributes.
     * @return the map with element attributes and content.
     */
    private Map<String, String> extractElementAttributes(final Element element) {
        final Map<String, String> elementAttributes = new HashMap<>();
        elementAttributes.put("content", element.text());
        element.attributes().forEach(attribute -> elementAttributes.put(attribute.getKey(), attribute.getValue()));
        return elementAttributes;
    }

    /**
     * Recursive method finds elements with similar {@code attributes} in the given {@code element}.
     *
     * @param foundElements elements which have similar attributes.
     * @param pathElements  list with path elements to remember the path to the current element in a recursive iteration.
     * @param element       current element for iteration.
     * @param attributes    element attributes to compare.
     * @return a map with found elements.
     */
    private Map<String, Element> findHtmlElementByMatchingAttributes(final Map<String, Element> foundElements, final List<String> pathElements, final Element element, final Map<String, String> attributes) {
        final List<Element> currentElements = new ArrayList<>();

        for (final Element currentElement : element.children()) {
            currentElements.add(currentElement);

            final long number = currentElements.stream()
                    .map(Element::nodeName)
                    .filter(elementName -> elementName.equals(currentElement.nodeName()))
                    .count();

            pathElements.add(currentElement.nodeName() + "[" + number + "]");

            if (hasSpecificAttributes(currentElement, attributes)) {
                foundElements.put(String.join(" > ", new ArrayList<>(pathElements)), currentElement);
            }

            findHtmlElementByMatchingAttributes(foundElements, pathElements, currentElement, attributes);

            pathElements.remove(pathElements.size() - 1);
        }
        return foundElements;
    }

    /**
     * Checks if the {@code element} contains at least one of given {@code attributes}.
     *
     * @param element    the element to check.
     * @param attributes the properties to compare.
     * @return true if the {@code element} has at least one of given {@code attributes} and false otherwise.
     */
    private boolean hasSpecificAttributes(final Element element, final Map<String, String> attributes) {
        final Attributes attrs = element.attributes();
        for (final String s : attributes.keySet()) {
            if (attrs.get(s).equals(attributes.get(s))) {
                return true;
            }
        }
        return element.text().equals(attributes.get("content"));
    }
}
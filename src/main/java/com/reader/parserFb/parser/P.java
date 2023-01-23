package com.reader.parserFb.parser;

import com.reader.parserFb.parser.fonts.Emphasis;
import com.reader.parserFb.parser.fonts.StrikeThrough;
import com.reader.parserFb.parser.fonts.Strong;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class P extends Element {

    protected ArrayList<Image> images;
    protected ArrayList<Emphasis> emphasis;
    protected ArrayList<Strong> strong;
    protected ArrayList<StrikeThrough> strikeThrough;

    public P() {
        super();
    }

    public P(Image image) {
        super();
        if (images == null) images = new ArrayList<>();
        images.add(image);
    }

    public P(Node p) {
        super(p);
        NodeList nodeList = p.getChildNodes();
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            switch (nodeList.item(index).getNodeName()) {
                case "image":
                    if (images == null) images = new ArrayList<>();
                    images.add(new Image(node));
                    break;
                case "strikethrough":
                    if (strikeThrough == null) strikeThrough = new ArrayList<>();
                    strikeThrough.add(new StrikeThrough(node.getTextContent(), p.getTextContent()));
                    break;
                case "strong":
                    if (strong == null) strong = new ArrayList<>();
                    strong.add(new Strong(node.getTextContent(), p.getTextContent()));
                    break;
                case "emphasis":
                    if (emphasis == null) emphasis = new ArrayList<>();
                    emphasis.add(new Emphasis(node.getTextContent(), p.getTextContent()));
                    break;
                case "subtitle":
                    if (emphasis == null) emphasis = new ArrayList<>();
                    emphasis.add(new Emphasis(node.getTextContent(), p.getTextContent()));
                    break;
            }
        }
    }

    public P(String p) {
        super(p);
    }

    public ArrayList<Image> getImages() {
        return images;
    }
}

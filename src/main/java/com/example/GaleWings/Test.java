package com.example.GaleWings;

import be.ceau.opml.OpmlParseException;
import be.ceau.opml.OpmlParser;
import be.ceau.opml.entity.Opml;
import be.ceau.opml.entity.Outline;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        String filePath = "C:\\Users\\ikarashi\\Desktop\\test2.opml";
        try(var fio = new FileInputStream(filePath)) {
            Opml opml = new OpmlParser().parse(fio);
            importOpml(opml.getBody().getOutlines());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OpmlParseException e) {
            e.printStackTrace();
        }

    }

    private static void importOpml(List<Outline> outlines) {
        outlines.stream().forEach(outline -> {
            if ("rss".equals(outline.getAttribute("type"))) {
                System.out.println(outline);
            } else {
                if (0 < outline.getSubElements().size()) {
                    importOpml(outline.getSubElements());
                }
            }
        });
    }
}

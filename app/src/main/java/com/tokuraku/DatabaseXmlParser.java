package com.tokuraku;

import android.util.Xml;

import com.tokuraku.models.Word;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseXmlParser {

    // We don't use namespaces
    private static final String ns = null;

    public List<Word> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Word> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Word> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "JMdict");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Word readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");

        String kanji = null;
        String furigana = null;
        String translation = null;
        String[] exemple = null;

        while (parser.next() != XmlPullParser.END_TAG && !parser.getName().equals("entry")) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "k_ele":
                    kanji = readKanji(parser);
                    break;
                case "r_ele":
                    furigana = readFurigana(parser);
                    break;
                case "sense":
                    readSense(parser, translation, exemple);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Word(kanji, furigana, translation, exemple[0], exemple[1]);
    }

    private String readKanji(XmlPullParser parser) throws IOException, XmlPullParserException {
        String kanji = "";
        int eventType = parser.getEventType();

        while(eventType != XmlPullParser.END_TAG && !parser.getName().equals("k_ele")){

           if( eventType == XmlPullParser.START_TAG) {
               if (parser.getName().equals("keb")) {
                   kanji += readText(parser) + " ";
               } else if (parser.getName().equals("reb")) {
                   kanji += readText(parser) + " ";
               }
           }

            eventType = parser.nextTag();

        }

        return kanji;
    }

    private String readFurigana(XmlPullParser parser) throws IOException, XmlPullParserException {
        String furigana = "";
        int eventType = parser.getEventType();

        while(eventType != XmlPullParser.END_TAG && !parser.getName().equals("r_ele")){

            if( eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("keb")) {
                   furigana += readText(parser) + ", ";
                } else if (parser.getName().equals("reb")) {
                    furigana += readText(parser) + ", ";
                }
            }

            eventType = parser.nextTag();

        }

        return furigana;
    }

    private String readTranslation(XmlPullParser parser) throws IOException, XmlPullParserException {
        String translation = "";
        while(parser.getName().equals("gloss")) {
            parser.require(XmlPullParser.START_TAG, ns, "gloss");
            translation += readText(parser) + "/n";
            parser.require(XmlPullParser.END_TAG, ns, "gloss");
        }

        return translation;
    }

    private String[] readExemple(XmlPullParser parser) throws IOException, XmlPullParserException {
        String[] exemple = new String[2];
        int eventType = parser.getEventType();

        while(eventType != XmlPullParser.END_TAG && !parser.getName().equals("exemple")){

            if( eventType == XmlPullParser.START_TAG && parser.getName().equals("ex_sent")) {
                if (parser.getAttributeValue(null,"lang").equals("jp")) {
                    exemple[0] += readText(parser);
                } else if (parser.getAttributeValue(null,"lang").equals("eng")) {
                    exemple[1] += readText(parser);
                }
            }

            eventType = parser.nextTag();

        }

        return exemple;
    }

    private void readSense(XmlPullParser parser, String translation, String[] exemple ) throws IOException, XmlPullParserException {
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_TAG && !parser.getName().equals("sense")){
            if(eventType == XmlPullParser.START_TAG){
                if(parser.getName().equals("gloss")){
                    translation = readTranslation(parser);
                }
                if(parser.getName().equals("exemple")){
                    exemple = readExemple(parser);
                }
            }
        }
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}

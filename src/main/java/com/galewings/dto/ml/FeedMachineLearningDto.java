package com.galewings.dto.ml;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.jsoup.Jsoup;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeedMachineLearningDto {

    private List<Token> tokens;

    public enum TokenAttribute {
        SURFACE("surface"),
        KNOWN("known"),
        USER("user"),
        POSITION("position"),
        PART_OF_SPEECH_LEVEL_1("partOfSpeechLevel1"),
        PART_OF_SPEECH_LEVEL_2("partOfSpeechLevel2"),
        PART_OF_SPEECH_LEVEL_3("partOfSpeechLevel3"),
        PART_OF_SPEECH_LEVEL_4("partOfSpeechLevel4"),
        CONJUGATION_TYPE("conjugationType"),
        CONJUGATION_FORM("conjugationForm"),
        BASE_FORM("baseForm"),
        READING("reading"),
        PRONUNCIATION("pronunciation"),

        ;
        String name;

        private TokenAttribute(String name) {
            this.name = name;
        }
    }

    public FeedMachineLearningDto(String url) throws IOException {
        String allText = Jsoup.connect(url).get().text();

        Tokenizer tokenizer = new Tokenizer();
        tokens = tokenizer.tokenize(allText);
    }

    private Map<String, Attribute> getAttributeMap() {

        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put(TokenAttribute.SURFACE.name, new Attribute(TokenAttribute.SURFACE.name,
                tokens.stream().map(Token::getSurface).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.KNOWN.name, new Attribute(TokenAttribute.KNOWN.name,
                Stream.of(Boolean.TRUE, Boolean.FALSE).map(bl -> bl.toString()).collect(Collectors.toList())));
        attributes.put(TokenAttribute.USER.name, new Attribute(TokenAttribute.USER.name,
                Stream.of(Boolean.TRUE, Boolean.FALSE).map(bl -> bl.toString()).collect(Collectors.toList())));
        attributes.put(TokenAttribute.POSITION.name, new Attribute(TokenAttribute.POSITION.name));
        attributes.put(TokenAttribute.PART_OF_SPEECH_LEVEL_1.name, new Attribute(TokenAttribute.PART_OF_SPEECH_LEVEL_1.name,
                tokens.stream().map(Token::getPartOfSpeechLevel1).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.PART_OF_SPEECH_LEVEL_2.name, new Attribute(TokenAttribute.PART_OF_SPEECH_LEVEL_2.name,
                tokens.stream().map(Token::getPartOfSpeechLevel2).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.PART_OF_SPEECH_LEVEL_3.name, new Attribute(TokenAttribute.PART_OF_SPEECH_LEVEL_3.name,
                tokens.stream().map(Token::getPartOfSpeechLevel3).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.PART_OF_SPEECH_LEVEL_4.name, new Attribute(TokenAttribute.PART_OF_SPEECH_LEVEL_4.name,
                tokens.stream().map(Token::getPartOfSpeechLevel4).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.CONJUGATION_TYPE.name, new Attribute(TokenAttribute.CONJUGATION_TYPE.name,
                tokens.stream().map(Token::getConjugationType).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.CONJUGATION_FORM.name, new Attribute(TokenAttribute.CONJUGATION_FORM.name,
                tokens.stream().map(Token::getConjugationForm).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.BASE_FORM.name, new Attribute(TokenAttribute.BASE_FORM.name,
                tokens.stream().map(Token::getBaseForm).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.READING.name, new Attribute(TokenAttribute.READING.name,
                tokens.stream().map(Token::getReading).distinct().collect(Collectors.toList())));
        attributes.put(TokenAttribute.PRONUNCIATION.name, new Attribute(TokenAttribute.PRONUNCIATION.name,
                tokens.stream().map(Token::getPronunciation).distinct().collect(Collectors.toList())));

        return attributes;
    }

    public Instances createInstance() {
        Map<String, Attribute> map = getAttributeMap();

        Instances instances = new Instances("Kuromoji", new ArrayList<>(map.values()), tokens.size());
        for (Token token : tokens) {
            Instance instance = new DenseInstance(14);
            instance.setValue(map.get(TokenAttribute.SURFACE.name), token.getSurface());
            instance.setValue(map.get(TokenAttribute.KNOWN.name), String.valueOf(token.isKnown()));
            instance.setValue(map.get(TokenAttribute.USER.name), String.valueOf(token.isUser()));
            instance.setValue(map.get(TokenAttribute.POSITION.name), token.getPosition());
            instance.setValue(map.get(TokenAttribute.PART_OF_SPEECH_LEVEL_1.name), token.getPartOfSpeechLevel1());
            instance.setValue(map.get(TokenAttribute.PART_OF_SPEECH_LEVEL_2.name), token.getPartOfSpeechLevel2());
            instance.setValue(map.get(TokenAttribute.PART_OF_SPEECH_LEVEL_3.name), token.getPartOfSpeechLevel3());
            instance.setValue(map.get(TokenAttribute.PART_OF_SPEECH_LEVEL_4.name), token.getPartOfSpeechLevel4());
            instance.setValue(map.get(TokenAttribute.CONJUGATION_TYPE.name), token.getConjugationType());
            instance.setValue(map.get(TokenAttribute.CONJUGATION_FORM.name), token.getConjugationForm());
            instance.setValue(map.get(TokenAttribute.BASE_FORM.name), token.getBaseForm());
            instance.setValue(map.get(TokenAttribute.READING.name), token.getReading());
            instances.add(instance);
        }
        return instances;
    }
}

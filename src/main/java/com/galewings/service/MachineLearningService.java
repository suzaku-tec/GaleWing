package com.galewings.service;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.*;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MachineLearningService {

    @Autowired
    GwDateService gwDateService;

    public CompletableFuture<String> createArff(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document doc = Jsoup.connect(url).get();

                String allText = doc.text();

                Tokenizer tokenizer = new Tokenizer();
                List<Token> tokens = tokenizer.tokenize(allText);

                Attribute surface =
                        new Attribute("Surface", tokens.stream().map(Token::getSurface).distinct().collect(Collectors.toList()));
                Attribute known =
                        new Attribute("Known",
                                Stream.of(Boolean.TRUE, Boolean.FALSE).map(bl -> bl.toString()).collect(Collectors.toList()));
                Attribute user = new Attribute("User", Stream.of(Boolean.TRUE, Boolean.FALSE).map(bl -> bl.toString()).collect(Collectors.toList()));
                Attribute position = new Attribute("Position");
                Attribute partOfSpeechLevel1 = new Attribute("PartOfSpeechLevel1",
                        tokens.stream().map(Token::getPartOfSpeechLevel1).distinct().collect(Collectors.toList()));
                Attribute partOfSpeechLevel2 = new Attribute("PartOfSpeechLevel2",
                        tokens.stream().map(Token::getPartOfSpeechLevel2).distinct().collect(Collectors.toList()));
                Attribute partOfSpeechLevel3 = new Attribute("PartOfSpeechLevel3",
                        tokens.stream().map(Token::getPartOfSpeechLevel3).distinct().collect(Collectors.toList()));
                Attribute partOfSpeechLevel4 = new Attribute("PartOfSpeechLevel4",
                        tokens.stream().map(Token::getPartOfSpeechLevel4).distinct().collect(Collectors.toList()));
                Attribute conjugationType = new Attribute("ConjugationType",
                        tokens.stream().map(Token::getConjugationType).distinct().collect(Collectors.toList()));
                Attribute conjugationForm = new Attribute("ConjugationForm",
                        tokens.stream().map(Token::getConjugationForm).distinct().collect(Collectors.toList()));
                Attribute baseForm = new Attribute("BaseForm", tokens.stream().map(Token::getBaseForm).distinct().collect(Collectors.toList()));
                Attribute reading = new Attribute("Reading", tokens.stream().map(Token::getReading).distinct().collect(Collectors.toList()));
                Attribute pronunciation = new Attribute("Pronunciation",
                        tokens.stream().map(Token::getPronunciation).distinct().collect(Collectors.toList()));


                FastVector attributes = new FastVector(14);
                attributes.addElement(surface);
                attributes.addElement(known);
                attributes.addElement(user);
                attributes.addElement(position);
                attributes.addElement(partOfSpeechLevel1);
                attributes.addElement(partOfSpeechLevel2);
                attributes.addElement(partOfSpeechLevel3);
                attributes.addElement(partOfSpeechLevel4);
                attributes.addElement(conjugationType);
                attributes.addElement(conjugationForm);
                attributes.addElement(baseForm);
                attributes.addElement(reading);
                attributes.addElement(pronunciation);

                // Wekaのインスタンスを定義
                Instances instances = new Instances("Kuromoji", attributes, tokens.size());
                for (Token token : tokens) {
                    Instance instance = new DenseInstance(14);
                    instance.setValue(surface, token.getSurface());
                    instance.setValue(known, String.valueOf(token.isKnown()));
                    instance.setValue(user, String.valueOf(token.isUser()));
                    instance.setValue(position, token.getPosition());
                    instance.setValue(partOfSpeechLevel1, token.getPartOfSpeechLevel1());
                    instance.setValue(partOfSpeechLevel2, token.getPartOfSpeechLevel2());
                    instance.setValue(partOfSpeechLevel3, token.getPartOfSpeechLevel3());
                    instance.setValue(partOfSpeechLevel4, token.getPartOfSpeechLevel4());
                    instance.setValue(conjugationType, token.getConjugationType());
                    instance.setValue(conjugationForm, token.getConjugationForm());
                    instance.setValue(baseForm, token.getBaseForm());
                    instance.setValue(reading, token.getReading());
                    instances.add(instance);
                }

                String timestampStr = GwDateService.DateFormat.FILE_TIME_FORMAT.dtf.format(LocalDateTime.now());

                // ARFFファイルに書き出す
                ArffSaver saver = new ArffSaver();
                saver.setInstances(instances);
                saver.setFile(new File("arff/output_" + timestampStr + ".arff"));
                saver.writeBatch();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "処理完了";
        });
    }
}

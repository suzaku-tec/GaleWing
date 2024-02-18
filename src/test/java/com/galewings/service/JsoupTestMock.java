package com.galewings.service;

import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieStore;
import java.net.Proxy;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class JsoupTestMock {

    public static Connection getConnectionMockInstance(Document doc) {
        return new MockResponse(doc);
    }

    static class MockResponse implements Connection {

        private final Document doc;

        public MockResponse(Document doc) {
            this.doc = doc;
        }

        @Override
        public Connection newRequest() {
            return null;
        }

        @Override
        public Connection url(URL url) {
            return null;
        }

        @Override
        public Connection url(String s) {
            return null;
        }

        @Override
        public Connection proxy(@Nullable Proxy proxy) {
            return null;
        }

        @Override
        public Connection proxy(String s, int i) {
            return null;
        }

        @Override
        public Connection userAgent(String s) {
            return null;
        }

        @Override
        public Connection timeout(int i) {
            return null;
        }

        @Override
        public Connection maxBodySize(int i) {
            return null;
        }

        @Override
        public Connection referrer(String s) {
            return null;
        }

        @Override
        public Connection followRedirects(boolean b) {
            return null;
        }

        @Override
        public Connection method(Method method) {
            return null;
        }

        @Override
        public Connection ignoreHttpErrors(boolean b) {
            return null;
        }

        @Override
        public Connection ignoreContentType(boolean b) {
            return null;
        }

        @Override
        public Connection sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            return null;
        }

        @Override
        public Connection data(String s, String s1) {
            return null;
        }

        @Override
        public Connection data(String s, String s1, InputStream inputStream) {
            return null;
        }

        @Override
        public Connection data(String s, String s1, InputStream inputStream, String s2) {
            return null;
        }

        @Override
        public Connection data(Collection<KeyVal> collection) {
            return null;
        }

        @Override
        public Connection data(Map<String, String> map) {
            return null;
        }

        @Override
        public Connection data(String... strings) {
            return null;
        }

        @Nullable
        @Override
        public KeyVal data(String s) {
            return null;
        }

        @Override
        public Connection requestBody(String s) {
            return null;
        }

        @Override
        public Connection header(String s, String s1) {
            return null;
        }

        @Override
        public Connection headers(Map<String, String> map) {
            return null;
        }

        @Override
        public Connection cookie(String s, String s1) {
            return null;
        }

        @Override
        public Connection cookies(Map<String, String> map) {
            return null;
        }

        @Override
        public Connection cookieStore(CookieStore cookieStore) {
            return null;
        }

        @Override
        public CookieStore cookieStore() {
            return null;
        }

        @Override
        public Connection parser(Parser parser) {
            return null;
        }

        @Override
        public Connection postDataCharset(String s) {
            return null;
        }

        @Override
        public Document get() throws IOException {
            return doc;
        }

        @Override
        public Document post() throws IOException {
            return null;
        }

        @Override
        public Response execute() throws IOException {
            return null;
        }

        @Override
        public Request request() {
            return null;
        }

        @Override
        public Connection request(Request request) {
            return null;
        }

        @Override
        public Response response() {
            return null;
        }

        @Override
        public Connection response(Response response) {
            return null;
        }
    }

}

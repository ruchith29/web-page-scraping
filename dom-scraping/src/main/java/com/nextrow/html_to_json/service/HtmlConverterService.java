package com.nextrow.html_to_json.service;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class HtmlConverterService {

    public Map<String,List<Map<String,String>>> generateHtmlToJson(Document document,String url) throws URISyntaxException {

        Map<String,List<Map<String,String>>> totalData=new LinkedHashMap<>();

        URI uri = new URI(url);
        // url structure

        Map<String,String> urlInfo=new LinkedHashMap<>();

        urlInfo.put("Original URL", url);
        urlInfo.put("URL Scheme", uri.getScheme());
        urlInfo.put("Domain", uri.getHost());
        urlInfo.put("Path", uri.getPath());
        urlInfo.put("Query", uri.getQuery());
        urlInfo.put("Fragment", uri.getFragment());
        urlInfo.put("User Info", uri.getUserInfo());


        ArrayList<Map<String,String>> urlData=new ArrayList<>();
        urlData.add(urlInfo);


        // header tags section
        Elements headers = document.select("h1,h2,h3,h4,h5,h6");

        ArrayList<Map<String,String>> headData=new ArrayList<>();

        for(Element head:headers){
            Map<String,String> headings=new LinkedHashMap<>();
            headings.put("tag",head.tagName());
            headings.put("tag-content",head.text());

            headData.add(headings);
        }

        // internal and external tags
        Elements linksContent = document.select("a");

        ArrayList<Map<String,String>> linksData=new ArrayList<>();

        for(Element link:linksContent){
            Map<String,String> headings=new LinkedHashMap<>();
            headings.put("tag",link.tagName());
            headings.put("tag-content",link.text());

            for (Attribute attribute : link.attributes()) {
                headings.put(attribute.getKey(), attribute.getValue());
            }
            linksData.add(headings);
        }

        totalData.put("URL Structure",urlData);
        totalData.put("Headers",headData);
        totalData.put("Internal / External Links Data",linksData);

        return totalData;
    }
}

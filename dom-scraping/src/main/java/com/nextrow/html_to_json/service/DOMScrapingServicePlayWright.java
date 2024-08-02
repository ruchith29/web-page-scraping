package com.nextrow.html_to_json.service;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class DOMScrapingServicePlayWright {

    public Map<String,Map<String, List<Map<String, String>>>> domScrapping(String url,Page page) throws IOException, URISyntaxException {

        page.navigate(url);

        Map<String,Map<String, List<Map<String, String>>>> totalData=new HashMap<>();

        Map<String, List<Map<String, String>>> headingsData = new HashMap<>();
        Map<String, List<Map<String, String>>> linksData = new HashMap<>();

        List<String> headingTags = Arrays.asList("h1","h2", "h3", "h4", "h5", "h6");
        List<String> headingAttributes = Arrays.asList("id","class","style","title","lang","dir","tabindex","accesskey","role");

        for(String tag: headingTags){
            headingsData.put(tag,addData(page,tag,headingAttributes));
        }

        List<String> anchorAttributes = Arrays.asList("id","class","style","title","lang","dir","tabindex","accesskey","role","img","href","target","rel","type","hreflang","download","ping","referrerpolicy");
        linksData.put("a",addData(page,"a",anchorAttributes));

        totalData.put("heading-tags",headingsData);
        totalData.put("internal/external links",linksData);

        return totalData;
    }


    public List<Map<String, String>> addData(Page page, String tagName,List<String> attributes){

        Locator locator=page.locator(tagName);
        int count=locator.count();
        List<Map<String, String>> headingData = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Locator element = locator.nth(i);
            if(element.isHidden()){
                continue;
            }

            Map<String, String> attributesData = new HashMap<>();
            String textContent = element.textContent();
            attributesData.put("text", textContent.trim().replaceAll("\n",""));

            for (String attribute : attributes) {
                String attributeValue = element.getAttribute(attribute);
                if (attributeValue != null) {
                    attributesData.put(attribute, attributeValue);
                }
            }
            headingData.add(attributesData);
        }

        return headingData;
    }
}
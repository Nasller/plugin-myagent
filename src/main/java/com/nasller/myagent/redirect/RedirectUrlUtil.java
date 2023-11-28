package com.nasller.myagent.redirect;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.models.FilterRule;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RedirectUrlUtil {
    private static final Map<FilterRule, LazyUrl> ruleMap = new HashMap<>();

    public static void setRules(List<FilterRule> rules) {
        for (FilterRule rule : rules) {
            String[] split = rule.getRule().split(",",2);
            rule.setRule(split[0]);
            ruleMap.put(rule, new LazyUrl(split[1]));
        }
    }

    public static URL testURL(URL url) {
        if (null == url || ruleMap.isEmpty() || (!"http".equals(url.getProtocol()) &&
                !"https".equals(url.getProtocol()))) {
            return url;
        }
        Iterator<Entry<FilterRule, LazyUrl>> iterator = ruleMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<FilterRule, LazyUrl> entry = iterator.next();
            if (entry.getKey().test(url.toString())) {
                try {
                    return entry.getValue().getURL();
                } catch (Exception e) {
                    DebugInfo.error("RedirectUrlUtil testURL error: " + url, e);
                    iterator.remove();
                }
            }
        }
        return url;
    }

    private static class LazyUrl{
        private final String url;
        private volatile URL urlObj;

        public LazyUrl(String url) {
            this.url = url;
        }

        public URL getURL() throws Exception{
            if(urlObj == null){
                synchronized (this){
                    if(urlObj == null){
                        urlObj = new URL(url);
                    }
                }
            }
            return urlObj;
        }
    }
}

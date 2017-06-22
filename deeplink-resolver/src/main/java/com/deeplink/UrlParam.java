package com.deeplink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HunkDeng
 * @since 2017/6/19
 */
public class UrlParam implements Serializable {
    public final String first; // first or single value
    public final List<String> list;

    private UrlParam(String value) {
        this.first = value;
        this.list = new ArrayList<>(1);
        this.list.add(first);
    }

    public UrlParam(List<String> stringList) {
        this.first = stringList.get(0);
        this.list = stringList;
    }

    public static UrlParam from(String value) {
        return new UrlParam(value);
    }

    public static UrlParam from(List<String> stringList) {
        return new UrlParam(stringList);
    }
}

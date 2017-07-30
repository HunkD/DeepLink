package com.example.eventmatch;

import java.util.Stack;

/**
 * Created by HunkDeng on 2017/7/30.
 */
public class Page {
    public final String name;
    public final Stack<String> events = new Stack<>();

    public Page(String name) {
        this.name = name;
    }

    public void click(String clickTarget) {
        this.events.push("click-" + clickTarget);
    }

    public void show() {
        this.events.push("show");
    }

    public boolean match(Page expectPage) {
        if (!name.equals(expectPage.name)) {
            System.out.println("page name not match in page compare");
            return false;
        }
        if (events.size() < expectPage.events.size()) {
            System.out.println("event size not match in page compare");
            return false;
        }
        if (events.size() == 0 && expectPage.events.size() == 0) {
            return true;
        }
        Stack<String> selfCopy = (Stack<String>) events.clone();
        Stack<String> expectCopy = (Stack<String>) expectPage.events.clone();
        String matchEvent = expectCopy.pop();
        boolean lastMatch = false;
        for (int i = selfCopy.size() - 1; i >= 0; i --) {
            if (matchEvent.equals(selfCopy.pop())) {
                lastMatch = true;
                if (expectCopy.size() > 0) {
                    matchEvent = expectCopy.pop();
                    lastMatch = false;
                } else {
                    break;
                }
            }
        }

        if (expectCopy.size() > 0 || !lastMatch) {
            System.out.println("last match didn't find =" + matchEvent);
            return false;
        }
        return true;
    }
}

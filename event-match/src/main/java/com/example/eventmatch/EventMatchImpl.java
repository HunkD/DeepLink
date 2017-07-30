package com.example.eventmatch;

import java.util.Stack;

/**
 * Created by HunkDeng on 2017/7/30.
 */
public class EventMatchImpl implements EventMatch {
    @Override
    public boolean match(Chain actual, Chain expected) {
        if (actual.pageStack.size() == 0 || actual.pageStack.size() < expected.pageStack.size()) {
            System.out.print("size not match");
            return false;
        }
        Stack<Page> actualPageStackCopy = (Stack<Page>) actual.pageStack.clone();
        Stack<Page> expectPageStackCopy = (Stack<Page>) expected.pageStack.clone();
        // reverse search
        for (int i = actual.pageStack.size() - 1; i >= actual.pageStack.size() - expected.pageStack.size(); i--) {
            Page actualPage = actualPageStackCopy.pop();
            Page expectPage = expectPageStackCopy.pop();
            if (!actualPage.match(expectPage)) {
                return false;
            }
        }
        return true;
    }
}

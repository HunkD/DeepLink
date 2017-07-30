package com.example.eventmatch;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by HunkDeng on 2017/7/30.
 */
public class EventMatchUnitTest {
    /**
     * all match, actual's size == expected's size
     */
    @Test
    public void test_match_success() {
        Chain actual = new Chain();
        Page cart = new Page("cart");
        cart.show();
        cart.click("checkout");
        Page signIn = new Page("signIn");
        signIn.show();
        signIn.click("login");
        actual.pageStack.push(cart);
        actual.pageStack.push(signIn);

        Chain expect = new Chain();
        Page cart1 = new Page("cart");
        cart1.click("checkout");
        expect.pageStack.push(cart1);

        Page signIn1 = new Page("signIn");
        signIn1.click("login");
        expect.pageStack.push(signIn1);

        EventMatch target = getTestObj();
        assertTrue(target.match(actual, expect));
    }

    /**
     * all match, actual's size > expected's size
     * expected: cart.checkout -> signIn.login
     * actual: home.show -> cart.checkout -> cart.show -> signIn.show -> signIn.login
     */
    @Test
    public void test_match_success2() {
        Chain actual = new Chain();
        Page home = new Page("home");
        home.show();
        actual.pageStack.push(home);

        Page cart = new Page("cart");
        cart.click("checkout");
        cart.show();
        actual.pageStack.push(cart);

        Page signIn = new Page("signIn");
        signIn.show();
        signIn.click("login");
        actual.pageStack.push(signIn);

        Chain expect = new Chain();
        Page cart1 = new Page("cart");
        cart1.click("checkout");
        expect.pageStack.push(cart1);

        Page signIn1 = new Page("signIn");
        signIn1.click("login");
        expect.pageStack.push(signIn1);

        EventMatch target = getTestObj();
        assertTrue(target.match(actual, expect));
    }

    private EventMatch getTestObj() {
        return new EventMatchImpl();
    }

    /**
     * Page's events didn't match in chain
     * expected: cart.checkout -> signIn.login
     * actual: cart.checkout -> signIn.show
     */
    @Test
    public void test_match_failed4() {
        Chain actual = new Chain();
        Page cart = new Page("cart");
        cart.show();
        cart.click("checkout");
        Page signIn = new Page("signIn");
        signIn.show();
        actual.pageStack.push(cart);
        actual.pageStack.push(signIn);

        Chain expect = new Chain();
        Page cart1 = new Page("cart");
        cart1.click("checkout");
        expect.pageStack.push(cart1);

        Page signIn1 = new Page("signIn");
        signIn1.click("login");
        expect.pageStack.push(signIn1);

        EventMatch target = getTestObj();
        assertFalse(target.match(actual, expect));
    }

    /**
     * page sequence in chain didn't match
     * expected: cart.checkout -> signIn.login
     * actual: signIn.show -> cart.show
     */
    @Test
    public void test_match_failed1() {
        Chain actual = new Chain();
        Page signIn = new Page("signIn");
        signIn.show();
        signIn.click("login");
        actual.pageStack.push(signIn);
        Page cart = new Page("cart");
        cart.show();
        cart.click("checkout");
        actual.pageStack.push(cart);

        Chain expect = new Chain();
        Page cart1 = new Page("cart");
        cart1.click("checkout");
        expect.pageStack.push(cart1);

        Page signIn1 = new Page("signIn");
        signIn1.click("login");
        expect.pageStack.push(signIn1);

        EventMatch target = getTestObj();
        assertFalse(target.match(actual, expect));
    }

    /**
     * chain size didn't match
     * expected: cart.checkout -> signIn.login
     * actual: signIn.show
     */
    @Test
    public void test_match_failed2() {
        Chain actual = new Chain();
        Page signIn = new Page("signIn");
        signIn.show();
        signIn.click("login");
        actual.pageStack.push(signIn);

        Chain expect = new Chain();
        Page cart1 = new Page("cart");
        cart1.click("checkout");
        expect.pageStack.push(cart1);

        Page signIn1 = new Page("signIn");
        signIn1.click("login");
        expect.pageStack.push(signIn1);

        EventMatch target = getTestObj();
        assertFalse(target.match(actual, expect));
    }

    /**
     * chain order didn't match
     * expected: home.show -> cart.checkout -> signIn.login
     * actual: cart.checkout -> home.show -> signIn.login
     */
    @Test
    public void test_match_failed5() {
        Chain actual = new Chain();

        Page cart = new Page("cart");
        cart.click("checkout");
        cart.show();
        actual.pageStack.push(cart);

        Page home = new Page("home");
        home.show();
        actual.pageStack.push(home);

        Page signIn = new Page("signIn");
        signIn.show();
        signIn.click("login");
        actual.pageStack.push(signIn);

        Chain expect = new Chain();
        Page home1 = new Page("home");
        home1.show();
        actual.pageStack.push(home1);

        Page cart1 = new Page("cart");
        cart1.click("checkout");
        expect.pageStack.push(cart1);

        Page signIn1 = new Page("signIn");
        signIn1.click("login");
        expect.pageStack.push(signIn1);

        EventMatch target = getTestObj();
        assertFalse(target.match(actual, expect));
    }
}

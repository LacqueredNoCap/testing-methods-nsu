package org.nsu.fit.shared;

import org.nsu.fit.services.browser.Browser;

import java.io.Closeable;

public class Screen implements Closeable {
    protected Browser browser;

    public Screen(Browser browser) {
        this.browser = browser;
    }

    @Override
    public void close() {
        browser.close();
    }
}

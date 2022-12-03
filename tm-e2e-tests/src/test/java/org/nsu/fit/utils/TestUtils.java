package org.nsu.fit.utils;

import org.nsu.fit.services.rest.data.CustomerPojo;

public final class TestUtils {

    private TestUtils() {
    }

    public static boolean isEqual(CustomerPojo one, CustomerPojo another) {
        return one.id.equals(another.id) &&
                one.firstName.equals(another.firstName) &&
                one.lastName.equals(another.lastName) &&
                one.login.equals(another.login) &&
                one.pass.equals(another.pass) &&
                one.balance == another.balance;
    }
}

package org.nsu.fit.utils;

import com.github.javafaker.Faker;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;

import java.util.Locale;

public final class TestUtils {

    private static final Faker FAKER = Faker.instance(Locale.ENGLISH);

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

    public static ContactPojo randomCustomer() {
        return new ContactPojo(
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.internet().emailAddress(),
                FAKER.internet().password(6, 12, true, true, true),
                0
        );
    }
}

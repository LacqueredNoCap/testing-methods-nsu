package org.nsu.fit.tm_backend.service.impl;

import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.nsu.fit.tm_backend.repository.CustomerRepository;
import org.nsu.fit.tm_backend.repository.data.ContactPojo;
import org.nsu.fit.tm_backend.repository.data.CustomerPojo;
import org.nsu.fit.tm_backend.service.CustomerService;
import org.nsu.fit.tm_backend.service.impl.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Globals;

@Service
@Singleton
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    // matches: arkady.novokhatskiy@gmail.com
    // doesn't match: a.novokhatskii@g.nsu.ru
    private static final String EMAIL_PATTERN = "[\\w-_.]{2,40}@[\\w-_.]{2,20}";

    private static final Set<String> EASY_PASSWORDS = Set.of(
            "123qwe", "1q2w3e", "qwerty"
    );

    private final CustomerRepository customerRepository;

    @Inject
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerPojo createCustomer(CustomerPojo newCustomer) {
        if (newCustomer == null) {
            throw new IllegalArgumentException("Argument 'customer' is null.");
        }

        if (newCustomer.pass == null) {
            throw new IllegalArgumentException("Field 'customer.pass' is null.");
        }

        if (newCustomer.pass.length() < 6 || newCustomer.pass.length() > 12) {
            throw new IllegalArgumentException("Password's length out of range [6, 12].");
        }

        if (EASY_PASSWORDS.contains(newCustomer.pass)) {
            throw new IllegalArgumentException("Password is very easy.");
        }

        // Лабораторная 2: добавить код который бы проверял, что нет customer'а c таким же login (email'ом).
        // Попробовать добавить другие ограничения, посмотреть как быстро растет кодовая база тестов.

        if (!newCustomer.getLogin().matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException(
                    String.format("Login \"%s\" doesn't match email pattern", newCustomer.getLogin())
            );
        }

        if (customerRepository.getCustomers().stream()
                .map(CustomerPojo::getLogin)
                .anyMatch(login -> login.equalsIgnoreCase(newCustomer.getLogin()))) {
            throw new IllegalArgumentException(
                    String.format("Customer \"%s\" already exists", newCustomer.getLogin())
            );
        }

        return customerRepository.createCustomer(newCustomer);
    }

    @Override
    public Set<CustomerPojo> getCustomers() {
        return customerRepository.getCustomers();
    }

    @Override
    public Set<UUID> getCustomerIds() {
        return customerRepository.getCustomerIds();
    }

    @Override
    public CustomerPojo getCustomer(UUID customerId) {
        return customerRepository.getCustomer(customerId);
    }

    @Override
    public CustomerPojo lookupCustomer(UUID customerId) {
        return customerRepository.getCustomers().stream()
            .filter(customer -> customer.id.equals(customerId))
            .findAny()
            .orElse(null);
    }

    @Override
    public CustomerPojo lookupCustomer(String login) {
        return customerRepository.getCustomers().stream()
                .filter(customer -> customer.login.equalsIgnoreCase(login))
                .findAny()
                .orElse(null);
    }

    @Override
    public ContactPojo me(AuthenticatedUserDetails authenticatedUserDetails) {
        if (authenticatedUserDetails.isAdmin()) {
            ContactPojo contactPojo = new ContactPojo();
            contactPojo.login = Globals.ADMIN_LOGIN;

            return contactPojo;
        }

        // Лабораторная 2: обратите внимание, что вернули данных больше чем надо.
        // Т.е. getCustomerByLogin честно возвратит все что есть в базе данных по этому customer'у.
        // Необходимо написать такой unit тест, который бы отлавливал данное поведение.
        return customerRepository.getCustomerByLogin(authenticatedUserDetails.getName());
    }

    @Override
    public void deleteCustomer(UUID id) {
        customerRepository.deleteCustomer(id);
    }

    @Override
    public CustomerPojo topUpBalance(UUID customerId, Integer money) {
        var customer = this.getCustomer(customerId);

        customer.balance += money;

        customerRepository.editCustomer(customer);

        return customer;
    }
}

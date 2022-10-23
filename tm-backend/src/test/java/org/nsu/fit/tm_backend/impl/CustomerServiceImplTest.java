package org.nsu.fit.tm_backend.impl;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.nsu.fit.tm_backend.repository.CustomerRepository;
import org.nsu.fit.tm_backend.repository.data.ContactPojo;
import org.nsu.fit.tm_backend.repository.data.CustomerPojo;
import org.nsu.fit.tm_backend.service.impl.CustomerServiceImpl;
import org.nsu.fit.tm_backend.service.impl.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Globals;

// Лабораторная 2: покрыть unit тестами класс CustomerServiceImpl на 100%.
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private CustomerRepository customerRepositoryMock;
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerRepositoryMock = Mockito.mock(CustomerRepository.class);
        customerService = new CustomerServiceImpl(customerRepositoryMock);
    }

    @Test
    void createCustomer_validCustomer_success() {
        // arrange: готовим входные аргументы и настраиваем mock'и.
        CustomerPojo createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@example.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@example.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        Mockito.when(customerRepositoryMock.createCustomer(ArgumentMatchers.any(CustomerPojo.class)))
                .thenReturn(createCustomerOutput);

        // act: вызываем метод, который хотим протестировать.
        CustomerPojo customer = customerService.createCustomer(createCustomerInput);

        // assert: проверяем результат выполнения метода.
        Assertions.assertEquals(customer.id, createCustomerOutput.id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        Mockito.verify(customerRepositoryMock, Mockito.times(1))
                .createCustomer(createCustomerInput);

    }

    @Test
    void createCustomer_nullAsCustomer_error() {
        // act-assert
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(null)
        );
        Assertions.assertEquals(
                "Argument 'customer' is null.",
                exception.getMessage()
        );
    }

    @Test
    void createCustomer_nullAsPassword_error() {
        // arrange
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = null;
        customer.balance = 0;

        // act-assert
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );
        Assertions.assertEquals(
                "Field 'customer.pass' is null.",
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "abcdefghijklm"})
    void createCustomer_passwordIsOutOfRange_error(String password) {
        // arrange
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = password;
        customer.balance = 0;

        // act-assert
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );
        Assertions.assertEquals(
                "Password's length out of range [6, 12].",
                exception.getMessage()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"123qwe" , "1q2w3e", "qwerty"})
    void createCustomer_easyPassword_error(String password) {
        // arrange
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = password;
        customer.balance = 0;

        // act-assert
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );
        Assertions.assertEquals(
                "Password is very easy.",
                exception.getMessage()
        );
    }

    @Test
    void createCustomer_emailDoesntMatchPattern_error() {
        // arrange
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john$wick@example.com";
        customer.pass = "password";
        customer.balance = 0;

        // act-assert
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );
        Assertions.assertEquals(
                String.format("Login \"%s\" doesn't match email pattern", customer.getLogin()),
                exception.getMessage()
        );
    }

    @Test
    void createCustomer_customerAlreadyExists_error() {
        // arrange
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "password";
        customer.balance = 0;

        Mockito.when(customerRepositoryMock.getCustomers())
                .thenReturn(Set.of(customer));

        // act-assert
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> customerService.createCustomer(customer)
        );
        Assertions.assertEquals(
                String.format("Customer \"%s\" already exists", customer.getLogin()),
                exception.getMessage()
        );
    }

    @Test
    void getCustomers_success() {
        customerService.getCustomers();

        Mockito.verify(customerRepositoryMock, Mockito.only())
                .getCustomers();
    }

    @Test
    void getCustomerIds_success() {
        customerService.getCustomerIds();

        Mockito.verify(customerRepositoryMock, Mockito.only())
                .getCustomerIds();
    }

    @Test
    void getCustomer_success() {
        customerService.getCustomer(UUID.randomUUID());

        Mockito.verify(customerRepositoryMock, Mockito.only())
                .getCustomer(ArgumentMatchers.any(UUID.class));
    }

    @Test
    void lookupCustomerById_existentCustomer_returnCustomer() {
        CustomerPojo customer = new CustomerPojo();
        customer.id = UUID.randomUUID();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "password";
        customer.balance = 0;

        Mockito.when(customerRepositoryMock.getCustomers())
                        .thenReturn(Set.of(customer));

        Assertions.assertEquals(
                customer.id,
                customerService.lookupCustomer(customer.id).getId()
        );
    }

    @Test
    void lookupCustomerById_nonExistentCustomer_returnNull() {
        Mockito.when(customerRepositoryMock.getCustomers())
                .thenReturn(Collections.emptySet());

        Assertions.assertNull(customerService.lookupCustomer(UUID.randomUUID()));
    }

    @Test
    void lookupCustomerByLogin_existentCustomer_returnCustomer() {
        CustomerPojo customer = new CustomerPojo();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "password";
        customer.balance = 0;

        Mockito.when(customerRepositoryMock.getCustomers())
                .thenReturn(Set.of(customer));

        Assertions.assertEquals(
                customer.login,
                customerService.lookupCustomer(customer.login).getLogin()
        );
    }

    @Test
    void lookupCustomerByLogin_nonExistentCustomer_returnNull() {
        Mockito.when(customerRepositoryMock.getCustomers())
                .thenReturn(Collections.emptySet());

        Assertions.assertNull(customerService.lookupCustomer(""));
    }

    @Test
    void me_adminRole_success() {
        AuthenticatedUserDetails authenticatedUserDetails
                = Mockito.mock(AuthenticatedUserDetails.class);

        Mockito.when(authenticatedUserDetails.isAdmin())
                .thenReturn(true);

        Assertions.assertEquals(
                customerService.me(authenticatedUserDetails).getLogin(),
                Globals.ADMIN_LOGIN
        );
    }

    @Test
    void me_customerRole_success() {
        CustomerPojo customer = new CustomerPojo();
        customer.id = UUID.randomUUID();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "password";
        customer.balance = 0;

        Mockito.when(customerRepositoryMock.getCustomerByLogin(ArgumentMatchers.anyString()))
                .thenReturn(customer);

        AuthenticatedUserDetails authenticatedUserDetails
                = Mockito.mock(AuthenticatedUserDetails.class);

        Mockito.when(authenticatedUserDetails.isAdmin())
                .thenReturn(false);
        Mockito.when(authenticatedUserDetails.getName())
                        .thenReturn(customer.login);

        Assertions.assertEquals(
                customerService.me(authenticatedUserDetails).getLogin(),
                customer.login
        );
    }

    @Test
    void me_customerRole_returnExactNeededType() {
        CustomerPojo customer = new CustomerPojo();
        customer.id = UUID.randomUUID();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "password";
        customer.balance = 0;

        Mockito.when(customerRepositoryMock.getCustomerByLogin(ArgumentMatchers.anyString()))
                .thenReturn(customer);

        AuthenticatedUserDetails authenticatedUserDetails
                = Mockito.mock(AuthenticatedUserDetails.class);

        Mockito.when(authenticatedUserDetails.isAdmin())
                .thenReturn(false);
        Mockito.when(authenticatedUserDetails.getName())
                .thenReturn(customer.login);

        Assertions.assertEquals(
                ContactPojo.class,
                customerService.me(authenticatedUserDetails).getClass()
        );
    }

    @Test
    void deleteCustomer_success() {
        customerService.deleteCustomer(UUID.randomUUID());

        Mockito.verify(customerRepositoryMock, Mockito.only())
                .deleteCustomer(ArgumentMatchers.any(UUID.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, 100})
    void topUpBalance_success(int upBalance) {
        CustomerPojo customer = new CustomerPojo();
        customer.id = UUID.randomUUID();
        customer.firstName = "John";
        customer.lastName = "Wick";
        customer.login = "john_wick@example.com";
        customer.pass = "password";
        customer.balance = 0;

        Mockito.when(customerService.getCustomer(ArgumentMatchers.any(UUID.class)))
                .thenReturn(customer);

        Assertions.assertEquals(
                customer.balance + upBalance,
                customerService.topUpBalance(customer.id, upBalance).getBalance()
        );

        Mockito.verify(customerRepositoryMock, Mockito.times(1))
                .editCustomer(ArgumentMatchers.any(CustomerPojo.class));
    }

}

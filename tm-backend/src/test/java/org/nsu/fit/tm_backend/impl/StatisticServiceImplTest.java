package org.nsu.fit.tm_backend.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.nsu.fit.tm_backend.repository.data.CustomerPojo;
import org.nsu.fit.tm_backend.repository.data.SubscriptionPojo;
import org.nsu.fit.tm_backend.service.CustomerService;
import org.nsu.fit.tm_backend.service.StatisticService;
import org.nsu.fit.tm_backend.service.SubscriptionService;
import org.nsu.fit.tm_backend.service.data.StatisticBO;
import org.nsu.fit.tm_backend.service.data.StatisticPerCustomerBO;
import org.nsu.fit.tm_backend.service.impl.StatisticServiceImpl;

// Лабораторная 2: покрыть unit тестами класс StatisticServiceImpl на 100%.
// Чтобы протестировать метод calculateOverall() используйте Mockito.spy(statisticService) и переопределите метод
// calculateOverall(UUID customerId), чтобы использовать стратегию "разделяй и властвуй".
public class StatisticServiceImplTest {

    private StatisticService statisticServiceSpy;
    private CustomerService customerServiceMock;
    private SubscriptionService subscriptionServiceMock;

    @BeforeEach
    void setUp() {
        customerServiceMock = Mockito.mock(CustomerService.class);
        subscriptionServiceMock = Mockito.mock(SubscriptionService.class);
        
        StatisticService statisticService = new StatisticServiceImpl(
                customerServiceMock,
                subscriptionServiceMock
        );
        statisticServiceSpy = Mockito.spy(statisticService);
    }

    @Test
    void calculateStatisticPerCustomer_customerNotFound_returnNull() {
        Mockito.when(customerServiceMock.lookupCustomerById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(null);

        Assertions.assertNull(statisticServiceSpy.calculate(UUID.randomUUID()));
    }

    @Test
    void calculateStatisticPerCustomer_customerWithNoSubscriptions_success() {
        CustomerPojo customer = new CustomerPojo();
        customer.id = UUID.randomUUID();
        customer.balance = 100;

        Mockito.when(customerServiceMock.lookupCustomerById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(customer);

        Mockito.when(subscriptionServiceMock.getSubscriptions(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Collections.emptyList());

        StatisticPerCustomerBO expected = StatisticPerCustomerBO.builder()
                .overallBalance(100)
                .overallFee(0)
                .subscriptionIds(Collections.emptySet())
                .build();

        Assertions.assertEquals(
                expected,
                statisticServiceSpy.calculate(UUID.randomUUID())
        );
    }

    @Test
    void calculateStatisticPerCustomer_customerWithTwoSubscriptions_success() {
        CustomerPojo customer = new CustomerPojo();
        customer.id = UUID.randomUUID();
        customer.balance = 100;

        List<SubscriptionPojo> subscriptionPojoList = List.of(
                SubscriptionPojo.builder()
                        .id(customer.id)
                        .planFee(500)
                        .build(),
                SubscriptionPojo.builder()
                        .id(customer.id)
                        .planFee(-100)
                        .build()
        );

        Mockito.when(customerServiceMock.lookupCustomerById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(customer);

        Mockito.when(subscriptionServiceMock.getSubscriptions(ArgumentMatchers.any(UUID.class)))
                .thenReturn(subscriptionPojoList);

        StatisticPerCustomerBO expected = StatisticPerCustomerBO.builder()
                .overallBalance(100)
                .overallFee(400)
                .subscriptionIds(subscriptionPojoList.stream()
                        .map(SubscriptionPojo::getId)
                        .collect(Collectors.toSet())
                )
                .build();

        Assertions.assertEquals(
                expected,
                statisticServiceSpy.calculate(customer.id)
        );
    }

    @Test
    void calculateOverallStatistic_noCustomersWereFound_returnEmptyStatisticBO() {
        Mockito.when(customerServiceMock.getCustomerIds())
                .thenReturn(Collections.emptySet());

        StatisticBO expected = StatisticBO.builder()
                .customers(Collections.emptySet())
                .overallBalance(0)
                .overallFee(0)
                .build();

        Assertions.assertEquals(
                expected,
                statisticServiceSpy.calculateOverall()
        );
    }

    @Test
    void calculateOverallStatistic_twoCustomersWereFound_success() {
        StatisticPerCustomerBO statistic = StatisticPerCustomerBO.builder()
                .overallBalance(1000)
                .overallFee(250)
                .customerId(UUID.randomUUID())
                .subscriptionIds(Collections.emptySet())
                .build();

        UUID id = UUID.randomUUID();
        Set<UUID> customerIds = Set.of(UUID.randomUUID(), UUID.randomUUID(), id);

        Mockito.when(customerServiceMock.getCustomerIds())
                .thenReturn(customerIds);

        Mockito.doReturn(statistic)
                .when(statisticServiceSpy)
                .calculate(ArgumentMatchers.any(UUID.class));

        Mockito.doReturn(null)
                .when(statisticServiceSpy)
                .calculate(id);

        StatisticBO expected = StatisticBO.builder()
                .customers(Set.of(statistic))
                .overallBalance(2 * 1000)
                .overallFee(2 * 250)
                .build();

        Assertions.assertEquals(
                expected,
                statisticServiceSpy.calculateOverall()
        );
    }
}

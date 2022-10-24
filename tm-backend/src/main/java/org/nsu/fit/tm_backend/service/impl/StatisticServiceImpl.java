package org.nsu.fit.tm_backend.service.impl;

import javax.inject.Inject;

import org.jvnet.hk2.annotations.Service;

import java.util.HashSet;
import java.util.UUID;

import org.nsu.fit.tm_backend.service.CustomerService;
import org.nsu.fit.tm_backend.service.StatisticService;
import org.nsu.fit.tm_backend.service.SubscriptionService;
import org.nsu.fit.tm_backend.service.data.StatisticBO;
import org.nsu.fit.tm_backend.service.data.StatisticPerCustomerBO;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final CustomerService customerService;
    private final SubscriptionService subscriptionService;

    @Inject
    public StatisticServiceImpl(
            CustomerService customerService,
            SubscriptionService subscriptionService) {
        this.customerService = customerService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public StatisticBO calculate() {
        var customers = new HashSet<StatisticPerCustomerBO>();

        int overallBalance = 0;
        int overallFee = 0;
        var customerIds = customerService.getCustomerIds();
        for (var customerId : customerIds) {
            var customerStatistic = this.calculate(customerId);

            if (customerStatistic == null) {
                continue;
            }

            overallBalance += customerStatistic.getOverallBalance();
            overallFee += customerStatistic.getOverallFee();

            customers.add(customerStatistic);
        }

        return StatisticBO.builder()
            .customers(customers)
            .overallBalance(overallBalance)
            .overallFee(overallFee)
            .build();
    }

    @Override
    public StatisticPerCustomerBO calculate(UUID customerId) {
        var customer = customerService.lookupCustomer(customerId);

        if (customer == null) {
            return null;
        }

        var subscriptions = subscriptionService.getSubscriptions(customerId);
        var subscriptionIds = new HashSet<UUID>();
        int overallFee = 0;
        for (var subscription : subscriptions) {
            subscriptionIds.add(subscription.id);
            overallFee += subscription.planFee;
        }

        return StatisticPerCustomerBO.builder()
                .overallBalance(customer.balance)
                .overallFee(overallFee)
                .subscriptionIds(subscriptionIds)
                .build();
    }
}

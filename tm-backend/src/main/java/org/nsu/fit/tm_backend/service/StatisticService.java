package org.nsu.fit.tm_backend.service;

import java.util.UUID;
import org.jvnet.hk2.annotations.Contract;
import org.nsu.fit.tm_backend.service.data.StatisticBO;
import org.nsu.fit.tm_backend.service.data.StatisticPerCustomerBO;

@Contract
public interface StatisticService {
    StatisticBO calculateOverall();

    StatisticPerCustomerBO calculate(UUID customerId);
}

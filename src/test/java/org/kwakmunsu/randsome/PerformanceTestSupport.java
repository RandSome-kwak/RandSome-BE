package org.kwakmunsu.randsome;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("performance")
public abstract class PerformanceTestSupport {

}
package org.vignesh.quarkus.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vignesh.quarkus.Counter;
import org.vignesh.quarkus.MonitorActiveMQ;

import javax.inject.Inject;

@Service
public class ExampleRoute extends RouteBuilder {

    @Value("timer.period")
    String period = "10s";

    /**
     * An injected bean
     */
    @Autowired
    Counter counter;

    @Override
    public void configure() throws Exception {
        fromF("timer:foo?period=%s", period)
                .setBody(exchange -> "Incremented the counter: " + counter.increment())
                .to("log:example?showExchangePattern=false&showBodyType=false")
                .to("direct:startAMQRoute");

        from("direct:startAMQRoute").bean(new MonitorActiveMQ(),"test");

    }
}

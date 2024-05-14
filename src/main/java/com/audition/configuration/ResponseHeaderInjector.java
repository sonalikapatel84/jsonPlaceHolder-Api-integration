package com.audition.configuration;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class ResponseHeaderInjector {

    // TODO Inject openTelemetry trace and span Ids in the response headers.

    public void doFilter(
        ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        Span span = Span.current();
        SpanContext spanContext = span.getSpanContext();

        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            httpServletResponse.setHeader(
                "X-TraceId",
                spanContext.getTraceId());

            httpServletResponse.setHeader(
                "X-SpanId",
                spanContext.getSpanId());
        }

        chain.doFilter(request, response);
    }
}

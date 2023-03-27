package co.sohamds.spring.todo;


import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;

import java.util.List;

import static co.sohamds.spring.todo.SpringBootTodoAppApplication.*;

public class CustomSampler implements Sampler {
    @Override
    public SamplingResult shouldSample(Context context, String s, String s1, SpanKind spanKind, Attributes attributes, List<LinkData> list) {
        if (filter.getKey() == sampleFilter) {
            return shouldSample(context,s,s1,spanKind,attributes,list);}
        return null;
    }




    @Override
    public String getDescription() {
        return null;
    }
}
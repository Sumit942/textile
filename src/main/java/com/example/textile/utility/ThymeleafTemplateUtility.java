package com.example.textile.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class ThymeleafTemplateUtility {

    @Autowired
    TemplateEngine templateEngine;

    public String getProcessedTemplate(String source, String variable, Object object) {
        Context context = new Context();
        context.setVariable(variable,object);
        return templateEngine.process(source,context);
    }
}

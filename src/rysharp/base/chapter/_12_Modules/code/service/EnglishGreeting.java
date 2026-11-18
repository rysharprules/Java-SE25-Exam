
package rysharp.base.chapter._12_Modules.code.service;

/*
Service Provider
Implements the SPI and provides English greetings.

module greeting.provider {
    requires greeting.api;

    provides rysharp.base.chapter._12_Modules.code.service.GreetingService
        with rysharp.base.chapter._12_Modules.code.service.EnglishGreeting,
             rysharp.base.chapter._12_Modules.code.service.SpanishGreeting;
}
*/
public class EnglishGreeting implements GreetingService {

    public EnglishGreeting() {
        System.out.println("EnglishGreeting constructed");
    }

    @Override
    public String language() {
        return "English";
    }

    @Override
    public String greet() {
        return "Hello";
    }
}

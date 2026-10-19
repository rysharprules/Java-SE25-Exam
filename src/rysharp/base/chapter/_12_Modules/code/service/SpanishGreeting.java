
package rysharp.base.chapter._12_Modules.code.service;

/*
Service Provider
Implements the SPI and provides Spanish greetings.

module greeting.provider {
    requires greeting.api;

    provides rysharp.base.chapter._12_Modules.code.service.GreetingService
        with rysharp.base.chapter._12_Modules.code.service.EnglishGreeting,
             rysharp.base.chapter._12_Modules.code.service.SpanishGreeting;
}
*/
public class SpanishGreeting implements GreetingService {

    public SpanishGreeting() {
        System.out.println("SpanishGreeting constructed");
    }

    @Override
    public String language() {
        return "Spanish";
    }

    @Override
    public String greet() {
        return "Hola";
    }
}

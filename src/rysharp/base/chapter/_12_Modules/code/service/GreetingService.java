
package rysharp.base.chapter._12_Modules.code.service;

/*
Service Provider Interface (SPI)
Defines the contract that providers implement.

module greeting.api {
    exports rysharp.base.chapter._12_Modules.code.service;
}
*/
public interface GreetingService {
    String language();
    String greet();
}

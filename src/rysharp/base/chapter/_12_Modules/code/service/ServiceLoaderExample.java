
package rysharp.base.chapter._12_Modules.code.service;

import java.util.ServiceLoader;

/*
Service Consumer
Discovers and uses available service providers.

module greeting.consumer {
    requires greeting.api;

    uses rysharp.base.chapter._12_Modules.code.service.GreetingService;
}

Service Locator - java.util.ServiceLoader
Java's built-in mechanism for discovering and loading providers.
*/
public class ServiceLoaderExample {

    public static void main(String[] args) {
        example1_load();
        example2_findFirst();
        example3_iterator();
        example4_stream();
        example5_reload();
        example6_loadInstalled();
        example7_moduleLayer();
        example8_examTraps();
    }

    // 1. Standard service discovery
    private static void example1_load() {
        System.out.println("\n1. ServiceLoader.load()");

        ServiceLoader<GreetingService> loader =
                ServiceLoader.load(GreetingService.class);

        // load() creates the loader.
        // Providers are discovered/instantiated lazily.

        for (GreetingService service : loader) {
            System.out.println(service.language() + ": " + service.greet());
        }

        // English: Hello
        // Spanish: Hola
    }

    // 2. Find the first available provider
    private static void example2_findFirst() {
        System.out.println("\n2. findFirst()");

        ServiceLoader<GreetingService> loader =
                ServiceLoader.load(GreetingService.class);

        loader.findFirst().ifPresent(service ->
                System.out.println(service.greet()));

        // Returns Optional<GreetingService>.
        // Empty if no provider is available.
        //
        // Do not rely on a particular provider order.
    }

    // 3. Iterator of service instances
    private static void example3_iterator() {
        System.out.println("\n3. iterator()");

        ServiceLoader<GreetingService> loader =
                ServiceLoader.load(GreetingService.class);

        var iterator = loader.iterator();

        while (iterator.hasNext()) {
            GreetingService service = iterator.next();
            System.out.println(service.greet());
        }

        // iterator() returns Iterator<GreetingService>.
        // Providers are instantiated as iteration progresses.
    }

    // 4. Stream of provider descriptors
    private static void example4_stream() {
        System.out.println("\n4. stream()");

        ServiceLoader<GreetingService> loader =
                ServiceLoader.load(GreetingService.class);

        loader.stream().forEach(provider -> {
            System.out.println(provider.type().getSimpleName());

            GreetingService service = provider.get();
            System.out.println(service.greet());
        });

        // IMPORTANT EXAM TRAP:
        //
        // stream() returns Stream<ServiceLoader.Provider<S>>
        // NOT Stream<S>.
        //
        // provider.type() -> Class<? extends S>
        // provider.get()  -> S
    }

    // 5. Clear cached providers
    private static void example5_reload() {
        System.out.println("\n5. reload()");

        ServiceLoader<GreetingService> loader =
                ServiceLoader.load(GreetingService.class);

        loader.findFirst();

        loader.reload();

        // Clears the loader's provider cache.
        // Later traversal locates/instantiates providers again.
        // Does NOT automatically install new providers.

        loader.findFirst().ifPresent(service ->
                System.out.println(service.greet()));
    }

    // 6. Installed-provider lookup
    private static void example6_loadInstalled() {
        System.out.println("\n6. loadInstalled()");

        ServiceLoader<GreetingService> loader =
                ServiceLoader.loadInstalled(GreetingService.class);

        System.out.println(loader.findFirst().isPresent());

        // Usually false for our classpath example.
        //
        // Uses the platform class loader.
        // Does not search ordinary application classpath providers.
    }

    // 7. Module-layer lookup
    private static void example7_moduleLayer() {
        System.out.println("\n7. load(ModuleLayer, Class)");

        ServiceLoader<GreetingService> loader =
                ServiceLoader.load(
                        ModuleLayer.boot(),
                        GreetingService.class
                );

        loader.stream().forEach(provider ->
                System.out.println(provider.type().getName()));

        // Searches providers in the supplied module layer
        // and its ancestors.
        //
        // This example uses the boot module layer.
        // Our classpath-only providers are not discovered here.
    }

    // 8. Certification traps
    private static void example8_examTraps() {
        System.out.println("\n8. Exam traps");

        // Main factory methods:
        //
        // ServiceLoader.load(Service.class)
        //
        // ServiceLoader.load(Service.class, classLoader)
        //
        // ServiceLoader.load(ModuleLayer.boot(), Service.class)
        //
        // ServiceLoader.loadInstalled(Service.class)
        //
        // No public ServiceLoader constructor.

        // Module declarations:
        //
        // Consumer:
        // uses com.example.GreetingService;
        //
        // Provider:
        // provides com.example.GreetingService
        //     with com.example.EnglishGreeting;

        // "uses" is declared by the consumer module.
        // "provides ... with" is declared by the provider module.
        //
        // exports is NOT required merely to provide a service.
        //
        // A provider in a named module may have a provider
        // constructor or a public static provider() factory
        // method, subject to the module system's rules.
        //
        // Classpath provider configuration normally requires
        // an accessible public no-argument constructor.

        // ServiceLoader is lazy.
        // findFirst() returns Optional<S>.
        // iterator() returns Iterator<S>.
        // stream() returns Stream<Provider<S>>.
        // reload() clears cached providers.
    }
}

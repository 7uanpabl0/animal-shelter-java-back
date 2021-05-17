package com.shelter.animalback.contract.api.provider;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import com.shelter.animalback.controller.AnimalController;
import com.shelter.animalback.domain.Animal;
import com.shelter.animalback.service.interfaces.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PactBroker(host = "7uanpablo.pactflow.io", scheme = "https",
        authentication = @PactBrokerAuth(token = "h9sgPDIPP7F50LN5JAM5rA"))
@Provider("AnimalShelterBack")
@ExtendWith(MockitoExtension.class)
public class AnimalShelterProviderTest {

    @Mock
    private AnimalService animalService;

    @InjectMocks
    private AnimalController animalController;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    public void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    public void changeContext(PactVerificationContext context) {
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setControllers(animalController);
        context.setTarget(testTarget);
    }


    @State("has animals")
    public void addAnimals() {
        Animal animal = new Animal();
        animal.setName("Catto");
        animal.setGender("Male");
        animal.setBreed("Siames");
        animal.setVaccinated(false);

        ArrayList<Animal> animals = new ArrayList<Animal>();
        animals.add(animal);

        Mockito.when(animalService.getAll()).thenReturn(animals);
    }

    @State("create animal")
    public void newAnimal() {
        Animal animal = new Animal();
        animal.setName("Miau");
        animal.setGender("Male");
        animal.setBreed("Catico");
        animal.setVaccinated(true);

        Mockito.when(animalService.save(Mockito.any(Animal.class))).thenReturn(animal);
    }

    @State("deleting an animal")
    public void whithoutAnimal() {
        String name = "Clark Kent";
        Mockito.doAnswer((i) -> {
            assertEquals(name, i.getArgument(0));
            return null;
        }).when(animalService).delete(name);
    }

}
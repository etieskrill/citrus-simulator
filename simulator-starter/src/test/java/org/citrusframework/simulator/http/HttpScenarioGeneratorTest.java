package org.citrusframework.simulator.http;

import io.swagger.models.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Christoph Deppisch
 */
@ExtendWith(MockitoExtension.class)
class HttpScenarioGeneratorTest {

    @Mock
    private ConfigurableListableBeanFactory beanFactoryMock;

    @Mock
    private DefaultListableBeanFactory beanRegistryMock;

    private HttpScenarioGenerator fixture;

    @BeforeEach
    void beforeEachSetup() {
        fixture = new HttpScenarioGenerator(new ClassPathResource("swagger/swagger-api.json"));
    }

    @Test
    void generateHttpScenarios() {
        doAnswer(invocation -> {
            HttpOperationScenario scenario = (HttpOperationScenario) invocation.getArguments()[1];

            assertNotNull(scenario.getOperation());
            assertEquals(scenario.getPath(), "/v2/pet");
            assertEquals(scenario.getMethod(), RequestMethod.POST);

            return null;
        }).when(beanFactoryMock).registerSingleton(eq("addPet"), any(HttpOperationScenario.class));

        doAnswer(invocation -> {
            HttpOperationScenario scenario = (HttpOperationScenario) invocation.getArguments()[1];

            assertNotNull(scenario.getOperation());
            assertEquals(scenario.getPath(), "/v2/pet/{petId}");
            assertEquals(scenario.getMethod(), RequestMethod.GET);

            return null;
        }).when(beanFactoryMock).registerSingleton(eq("getPetById"), any(HttpOperationScenario.class));

        doAnswer(invocation -> {
            HttpOperationScenario scenario = (HttpOperationScenario) invocation.getArguments()[1];

            assertNotNull(scenario.getOperation());
            assertEquals(scenario.getPath(), "/v2/pet/{petId}");
            assertEquals(scenario.getMethod(), RequestMethod.DELETE);

            return null;
        }).when(beanFactoryMock).registerSingleton(eq("deletePet"), any(HttpOperationScenario.class));

        fixture.postProcessBeanFactory(beanFactoryMock);

        verify(beanFactoryMock).registerSingleton(eq("addPet"), any(HttpOperationScenario.class));
        verify(beanFactoryMock).registerSingleton(eq("getPetById"), any(HttpOperationScenario.class));
        verify(beanFactoryMock).registerSingleton(eq("deletePet"), any(HttpOperationScenario.class));
    }

    @Test
    void testGenerateScenariosWithBeandDefinitionRegistry() {
        doAnswer(invocation -> {
            BeanDefinition scenario = (BeanDefinition) invocation.getArguments()[1];

            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(0, String.class).getValue(), "/v2/pet");
            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(1, RequestMethod.class).getValue(), RequestMethod.POST);
            assertNotNull(scenario.getConstructorArgumentValues().getArgumentValue(2, Operation.class).getValue());
            assertNull(scenario.getPropertyValues().get("inboundDataDictionary"));
            assertNull(scenario.getPropertyValues().get("outboundDataDictionary"));

            return null;
        }).when(beanRegistryMock).registerBeanDefinition(eq("addPet"), any(BeanDefinition.class));

        doAnswer(invocation -> {
            BeanDefinition scenario = (BeanDefinition) invocation.getArguments()[1];

            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(0, String.class).getValue(), "/v2/pet/{petId}");
            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(1, RequestMethod.class).getValue(), RequestMethod.GET);
            assertNotNull(scenario.getConstructorArgumentValues().getArgumentValue(2, Operation.class).getValue());
            assertNull(scenario.getPropertyValues().get("inboundDataDictionary"));
            assertNull(scenario.getPropertyValues().get("outboundDataDictionary"));

            return null;
        }).when(beanRegistryMock).registerBeanDefinition(eq("getPetById"), any(BeanDefinition.class));

        doAnswer(invocation -> {
            BeanDefinition scenario = (BeanDefinition) invocation.getArguments()[1];

            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(0, String.class).getValue(), "/v2/pet/{petId}");
            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(1, RequestMethod.class).getValue(), RequestMethod.DELETE);
            assertNotNull(scenario.getConstructorArgumentValues().getArgumentValue(2, Operation.class).getValue());
            assertNull(scenario.getPropertyValues().get("inboundDataDictionary"));
            assertNull(scenario.getPropertyValues().get("outboundDataDictionary"));

            return null;
        }).when(beanRegistryMock).registerBeanDefinition(eq("deletePet"), any(BeanDefinition.class));

        fixture.postProcessBeanFactory(beanRegistryMock);

        verify(beanRegistryMock).registerBeanDefinition(eq("addPet"), any(BeanDefinition.class));
        verify(beanRegistryMock).registerBeanDefinition(eq("getPetById"), any(BeanDefinition.class));
        verify(beanRegistryMock).registerBeanDefinition(eq("deletePet"), any(BeanDefinition.class));
    }

    @Test
    void testGenerateScenariosWithDataDictionaries() {
        when(beanRegistryMock.containsBeanDefinition("inboundJsonDataDictionary")).thenReturn(true);
        when(beanRegistryMock.containsBeanDefinition("outboundJsonDataDictionary")).thenReturn(true);

        doAnswer(invocation -> {
            BeanDefinition scenario = (BeanDefinition) invocation.getArguments()[1];

            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(0, String.class).getValue(), "/v2/pet");
            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(1, RequestMethod.class).getValue(), RequestMethod.POST);
            assertNotNull(scenario.getConstructorArgumentValues().getArgumentValue(2, Operation.class).getValue());
            assertNotNull(scenario.getPropertyValues().get("inboundDataDictionary"));
            assertNotNull(scenario.getPropertyValues().get("outboundDataDictionary"));

            return null;
        }).when(beanRegistryMock).registerBeanDefinition(eq("addPet"), any(BeanDefinition.class));

        doAnswer(invocation -> {
            BeanDefinition scenario = (BeanDefinition) invocation.getArguments()[1];

            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(0, String.class).getValue(), "/v2/pet/{petId}");
            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(1, RequestMethod.class).getValue(), RequestMethod.GET);
            assertNotNull(scenario.getConstructorArgumentValues().getArgumentValue(2, Operation.class).getValue());
            assertNotNull(scenario.getPropertyValues().get("inboundDataDictionary"));
            assertNotNull(scenario.getPropertyValues().get("outboundDataDictionary"));

            return null;
        }).when(beanRegistryMock).registerBeanDefinition(eq("getPetById"), any(BeanDefinition.class));

        doAnswer(invocation -> {
            BeanDefinition scenario = (BeanDefinition) invocation.getArguments()[1];

            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(0, String.class).getValue(), "/v2/pet/{petId}");
            assertEquals(scenario.getConstructorArgumentValues().getArgumentValue(1, RequestMethod.class).getValue(), RequestMethod.DELETE);
            assertNotNull(scenario.getConstructorArgumentValues().getArgumentValue(2, Operation.class).getValue());
            assertNotNull(scenario.getPropertyValues().get("inboundDataDictionary"));
            assertNotNull(scenario.getPropertyValues().get("outboundDataDictionary"));

            return null;
        }).when(beanRegistryMock).registerBeanDefinition(eq("deletePet"), any(BeanDefinition.class));

        fixture.postProcessBeanFactory(beanRegistryMock);

        verify(beanRegistryMock).registerBeanDefinition(eq("addPet"), any(BeanDefinition.class));
        verify(beanRegistryMock).registerBeanDefinition(eq("getPetById"), any(BeanDefinition.class));
        verify(beanRegistryMock).registerBeanDefinition(eq("deletePet"), any(BeanDefinition.class));
    }
}

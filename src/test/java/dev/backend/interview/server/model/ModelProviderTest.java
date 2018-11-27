package dev.backend.interview.server.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModelProviderTest {

    @Test
    public void getModel() {
        final Model model1 = ModelProvider.getModel();
        final Model model2 = ModelProvider.getModel();
        assertEquals(model1, model2);
    }
}
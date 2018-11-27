package dev.backend.interview.server.model;

public class ModelProvider {
    private static Model model;

    public static synchronized Model getModel() {
        if (model == null)
            model = new ModelImpl();
        return model;
    }
}

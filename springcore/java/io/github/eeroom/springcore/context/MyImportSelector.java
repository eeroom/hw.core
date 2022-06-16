package io.github.eeroom.springcore.context;

import io.github.eeroom.springcore.RootConfig2;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{RootConfig2.class.getName()};
    }
}

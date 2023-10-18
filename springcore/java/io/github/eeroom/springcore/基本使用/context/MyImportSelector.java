package io.github.eeroom.springcore.基本使用.context;

import io.github.eeroom.springcore.基本使用.RootConfig2;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{RootConfig2.class.getName()};
    }
}

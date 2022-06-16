package javax.servlet;

import java.util.Set;

public interface ServletContainerInitializer {
    void onStartup(Object servletcontext);
}

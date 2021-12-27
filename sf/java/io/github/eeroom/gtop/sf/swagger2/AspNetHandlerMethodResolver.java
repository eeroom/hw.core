package io.github.eeroom.gtop.sf.swagger2;

import com.fasterxml.classmate.*;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spring.web.readers.operation.HandlerMethodResolver;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AspNetHandlerMethodResolver extends HandlerMethodResolver {

    private static final String SPRING4_DISCOVERER = "org.springframework.core.DefaultParameterNameDiscoverer";
    private final ParameterNameDiscoverer parameterNameDiscover = this.parameterNameDiscoverer();
    private final TypeResolver typeResolver;
    private Map<Class, List<ResolvedMethod>> methodsResolvedForHostClasses = new HashMap();

    public AspNetHandlerMethodResolver(TypeResolver typeResolver) {
        super(typeResolver);
        this.typeResolver = typeResolver;
    }

    @Override
    public List<ResolvedMethodParameter> methodParameters(HandlerMethod methodToResolve) {
        return (List)this.resolvedMethod(methodToResolve).transform(this.toParameters(methodToResolve)).or(Lists.newArrayList());
    }

    boolean contravariant(ResolvedType candidateMethodReturnValue, Type returnValueOnMethod) {
        return this.isSubClass(candidateMethodReturnValue, returnValueOnMethod) || this.isGenericTypeSubclass(candidateMethodReturnValue, returnValueOnMethod);
    }

    @VisibleForTesting
    static Ordering<ResolvedMethod> byArgumentCount() {
        return Ordering.from(new Comparator<ResolvedMethod>() {
            public int compare(ResolvedMethod first, ResolvedMethod second) {
                return Ints.compare(first.getArgumentCount(), second.getArgumentCount());
            }
        });
    }

    @VisibleForTesting
    boolean bothAreVoids(ResolvedType candidateMethodReturnValue, Type returnType) {
        return (Void.class == candidateMethodReturnValue.getErasedType() || Void.TYPE == candidateMethodReturnValue.getErasedType()) && (Void.TYPE == returnType || Void.class == returnType);
    }

    @VisibleForTesting
    boolean isGenericTypeSubclass(ResolvedType candidateMethodReturnValue, Type returnValueOnMethod) {
        return returnValueOnMethod instanceof ParameterizedType && candidateMethodReturnValue.getErasedType().isAssignableFrom((Class)((ParameterizedType)returnValueOnMethod).getRawType());
    }

    @VisibleForTesting
    boolean isSubClass(ResolvedType candidateMethodReturnValue, Type returnValueOnMethod) {
        return returnValueOnMethod instanceof Class && candidateMethodReturnValue.getErasedType().isAssignableFrom((Class)returnValueOnMethod);
    }

    @VisibleForTesting
    boolean covariant(ResolvedType candidateMethodArgument, Type argumentOnMethod) {
        return this.isSuperClass(candidateMethodArgument, argumentOnMethod) || this.isGenericTypeSuperClass(candidateMethodArgument, argumentOnMethod);
    }

    @VisibleForTesting
    boolean isGenericTypeSuperClass(ResolvedType candidateMethodArgument, Type argumentOnMethod) {
        return argumentOnMethod instanceof ParameterizedType && ((Class)((ParameterizedType)argumentOnMethod).getRawType()).isAssignableFrom(candidateMethodArgument.getErasedType());
    }

    @VisibleForTesting
    boolean isSuperClass(ResolvedType candidateMethodArgument, Type argumentOnMethod) {
        return argumentOnMethod instanceof Class && ((Class)argumentOnMethod).isAssignableFrom(candidateMethodArgument.getErasedType());
    }

    private Optional<ResolvedMethod> resolvedMethod(HandlerMethod handlerMethod) {
        if (handlerMethod == null) {
            return Optional.absent();
        } else {
            Class hostClass = (Class)useType(handlerMethod.getBeanType()).or(handlerMethod.getMethod().getDeclaringClass());
            Iterable<ResolvedMethod> filtered = Iterables.filter(this.getMemberMethods(hostClass), methodNamesAreSame(handlerMethod.getMethod()));
            return this.resolveToMethodWithMaxResolvedTypes(filtered, handlerMethod.getMethod());
        }
    }

    private List<ResolvedMethod> getMemberMethods(Class hostClass) {
        if (!this.methodsResolvedForHostClasses.containsKey(hostClass)) {
            ResolvedType beanType = this.typeResolver.resolve(hostClass, new Type[0]);
            MemberResolver resolver = new MemberResolver(this.typeResolver);
            resolver.setIncludeLangObject(false);
            ResolvedTypeWithMembers typeWithMembers = resolver.resolve(beanType, (AnnotationConfiguration)null, (AnnotationOverrides)null);
            this.methodsResolvedForHostClasses.put(hostClass, Lists.newArrayList(typeWithMembers.getMemberMethods()));
        }

        return (List)this.methodsResolvedForHostClasses.get(hostClass);
    }

    private static Function<ResolvedMethod, ResolvedType> toReturnType(final TypeResolver resolver) {
        return new Function<ResolvedMethod, ResolvedType>() {
            public ResolvedType apply(ResolvedMethod input) {
                return (ResolvedType)Optional.fromNullable(input.getReturnType()).or(resolver.resolve(Void.TYPE, new Type[0]));
            }
        };
    }

    private Function<ResolvedMethod, List<ResolvedMethodParameter>> toParameters(final HandlerMethod methodToResolve) {
        return new Function<ResolvedMethod, List<ResolvedMethodParameter>>() {
            public List<ResolvedMethodParameter> apply(ResolvedMethod input) {
                List<ResolvedMethodParameter> parameters = Lists.newArrayList();
                MethodParameter[] methodParameters = methodToResolve.getMethodParameters();

                for(int i = 0; i < input.getArgumentCount(); ++i) {
                    parameters.add(new AspNetResolvedMethodParameter((String)AspNetHandlerMethodResolver.this.discoveredName(methodParameters[i]).or(String.format("param%s", i)), methodParameters[i], input.getArgumentType(i)));
                }

                return parameters;
            }
        };
    }

    private static Iterable<ResolvedMethod> methodsWithSameNumberOfParams(Iterable<ResolvedMethod> filtered, final Method methodToResolve) {
        return Iterables.filter(filtered, new Predicate<ResolvedMethod>() {
            public boolean apply(ResolvedMethod input) {
                return input.getArgumentCount() == methodToResolve.getParameterTypes().length;
            }
        });
    }

    private static Predicate<ResolvedMethod> methodNamesAreSame(final Method methodToResolve) {
        return new Predicate<ResolvedMethod>() {
            public boolean apply(ResolvedMethod input) {
                return ((Method)input.getRawMember()).getName().equals(methodToResolve.getName());
            }
        };
    }

    private Optional<ResolvedMethod> resolveToMethodWithMaxResolvedTypes(Iterable<ResolvedMethod> filtered, Method methodToResolve) {
        if (Iterables.size(filtered) > 1) {
            Iterable<ResolvedMethod> covariantMethods = this.covariantMethods(filtered, methodToResolve);
            if (Iterables.size(covariantMethods) == 0) {
                return FluentIterable.from(filtered).firstMatch(this.sameMethod(methodToResolve));
            } else {
                return Iterables.size(covariantMethods) == 1 ? FluentIterable.from(covariantMethods).first() : Optional.of(byArgumentCount().max(covariantMethods));
            }
        } else {
            return FluentIterable.from(filtered).first();
        }
    }

    private Predicate<ResolvedMethod> sameMethod(final Method methodToResolve) {
        return new Predicate<ResolvedMethod>() {
            public boolean apply(ResolvedMethod input) {
                return methodToResolve.equals(input.getRawMember());
            }
        };
    }

    private Iterable<ResolvedMethod> covariantMethods(Iterable<ResolvedMethod> filtered, Method methodToResolve) {
        return Iterables.filter(methodsWithSameNumberOfParams(filtered, methodToResolve), this.onlyCovariantMethods(methodToResolve));
    }

    private Predicate<ResolvedMethod> onlyCovariantMethods(final Method methodToResolve) {
        return new Predicate<ResolvedMethod>() {
            public boolean apply(ResolvedMethod input) {
                for(int index = 0; index < input.getArgumentCount(); ++index) {
                    if (!AspNetHandlerMethodResolver.this.covariant(input.getArgumentType(index), methodToResolve.getGenericParameterTypes()[index])) {
                        return false;
                    }
                }

                ResolvedType candidateMethodReturnValue = AspNetHandlerMethodResolver.this.returnTypeOrVoid(input);
                return AspNetHandlerMethodResolver.this.bothAreVoids(candidateMethodReturnValue, methodToResolve.getGenericReturnType()) || AspNetHandlerMethodResolver.this.contravariant(candidateMethodReturnValue, methodToResolve.getGenericReturnType());
            }
        };
    }

    private ResolvedType returnTypeOrVoid(ResolvedMethod input) {
        ResolvedType returnType = input.getReturnType();
        if (returnType == null) {
            returnType = this.typeResolver.resolve(Void.class, new Type[0]);
        }

        return returnType;
    }

    private Optional<String> discoveredName(MethodParameter methodParameter) {
        String[] discoveredNames = this.parameterNameDiscover.getParameterNames(methodParameter.getMethod());
        int discoveredNameCount = ((String[])Optional.fromNullable(discoveredNames).or(new String[0])).length;
        return methodParameter.getParameterIndex() < discoveredNameCount ? Optional.fromNullable(Strings.emptyToNull(discoveredNames[methodParameter.getParameterIndex()])) : Optional.fromNullable(methodParameter.getParameterName());
    }

    private ParameterNameDiscoverer parameterNameDiscoverer() {
        Object discoverer;
        try {
            discoverer = (ParameterNameDiscoverer)Class.forName("org.springframework.core.DefaultParameterNameDiscoverer").newInstance();
        } catch (Exception var3) {
            discoverer = new LocalVariableTableParameterNameDiscoverer();
        }

        return (ParameterNameDiscoverer)discoverer;
    }
}

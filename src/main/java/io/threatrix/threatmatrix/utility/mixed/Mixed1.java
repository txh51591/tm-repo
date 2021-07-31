package io.threatrix.threatmatrix.utility.mixed;

public class Mixed1 {

    /*
       github@spring-projects/spring-boot:ServletContextInitializerBeans.java:v2.1.0.RC1
       modifications: none
     */
    @SafeVarargs
    public ServletContextInitializerBeans(ListableBeanFactory beanFactory,
                                          Class<? extends ServletContextInitializer>... initializerTypes) {
        this.initializers = new LinkedMultiValueMap<>();
        this.initializerTypes = (initializerTypes.length != 0)
            ? Arrays.asList(initializerTypes)
            : Collections.singletonList(ServletContextInitializer.class);
        addServletContextInitializerBeans(beanFactory);
        addAdaptableBeans(beanFactory);
        List<ServletContextInitializer> sortedInitializers = this.initializers.values()
            .stream()
            .flatMap((value) -> value.stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE))
            .collect(Collectors.toList());
        this.sortedList = Collections.unmodifiableList(sortedInitializers);
    }

    /*
       github@spring-projects/spring-hateoas:WebFluxLinkBuilder.java:1.3.2
       modifications: none
    */
    public static class WebFluxLink {

        private final Mono<Link> link;

        public WebFluxLink(Mono<Link> link) {
            this.link = link;
        }

        /**
         * Adds the affordance created by the given virtual method invocation.
         *
         * @param invocation must not be {@literal null}.
         * @return
         * @see WebFluxLinkBuilder#methodOn(Class, Object...)
         */
        public WebFluxLink andAffordance(Object invocation) {

            Assert.notNull(invocation, "Invocation must not be null!");

            Mono<WebFluxLinkBuilder> builder = linkToInternal(invocation);

            return new WebFluxLink(link.flatMap(it -> builder //
                .flatMapIterable(WebFluxLinkBuilder::getAffordances) //
                .singleOrEmpty() //
                .map(it::andAffordance)));
        }

        /**
         * Creates a new {@link WebFluxLink} with the current {@link Link} instance transformed using the given mapper.
         *
         * @param mapper must not be {@literal null}.
         * @return
         */
        public WebFluxLink map(Function<Link, Link> mapper) {

            Assert.notNull(mapper, "Function must not be null!");

            return new WebFluxLink(link.map(mapper));
        }

        /**
         * Returns the underlying {@link Mono} of {@link Link} for further handling within a reactive pipeline.
         *
         * @return
         */
        public Mono<Link> toMono() {
            return link;
        }

        /**
         * Returns a {@link Mono} of {@link Link} with the current one augmented by the given {@link Function}. Allows
         * immediate customization of the {@link Link} instance and immediately return to a general reactive API.
         *
         * @param finisher must not be {@literal null}.
         * @return
         */
        public Mono<Link> toMono(Function<Link, Link> finisher) {

            Assert.notNull(finisher, "Function must not be null!");

            return link.map(finisher);
        }
    }


    /*
       github@spring-projects/spring-boot:ServletContextInitializerBeans.java:v2.1.0.RC1
       modifications: none
    */
    private static class ServletRegistrationBeanAdapter
        implements RegistrationBeanAdapter<Servlet> {

        private final MultipartConfigElement multipartConfig;

        ServletRegistrationBeanAdapter(MultipartConfigElement multipartConfig) {
            this.multipartConfig = multipartConfig;
        }

        @Override
        public RegistrationBean createRegistrationBean(String name, Servlet source,
                                                       int totalNumberOfSourceBeans) {
            String url = (totalNumberOfSourceBeans != 1) ? "/" + name + "/" : "/";
            if (name.equals(DISPATCHER_SERVLET_NAME)) {
                url = "/"; // always map the main dispatcherServlet to "/"
            }
            ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(source,
                url);
            bean.setName(name);
            bean.setMultipartConfig(this.multipartConfig);
            return bean;
        }

    }

    /*
        github@spring-projects/spring-hateoas:WebFluxLinkBuilder.java:1.3.2
        modifications: none
    */
    private static class CurrentRequest {

        private static final ConversionService FALLBACK_CONVERSION_SERVICE = new DefaultConversionService();

        private UriComponentsBuilder builder;
        private ConversionService conversionService;

        public static Mono<CurrentRequest> of(@Nullable ServerWebExchange exchange) {

            CurrentRequest result = new CurrentRequest();

            if (exchange == null) {

                result.builder = UriComponentsBuilder.fromPath("/");
                result.conversionService = FALLBACK_CONVERSION_SERVICE;

                return Mono.just(result);
            }

            ServerHttpRequest request = exchange.getRequest();
            PathContainer contextPath = request.getPath().contextPath();

            result.builder = UriComponentsBuilder.fromHttpRequest(request) //
                .replacePath(contextPath.toString()) //
                .replaceQuery("");

            ApplicationContext context = exchange.getApplicationContext();

            result.conversionService = context != null && context.containsBean("webFluxConversionService")
                ? context.getBean("webFluxConversionService", ConversionService.class)
                : FALLBACK_CONVERSION_SERVICE;

            return Mono.just(result);
        }
    }

}

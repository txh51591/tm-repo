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


    /*
       github@spring-projects/spring-framework:PathMatchingBenchmark.java:v5.3.9
       modifications: none
    */
    static class RouteGenerator {

        static List<Route> staticRoutes() {
            return Arrays.asList(
                new Route("/"),
                new Route("/why-spring"),
                new Route("/microservices"),
                new Route("/reactive"),
                new Route("/event-driven"),
                new Route("/cloud"),
                new Route("/web-applications"),
                new Route("/serverless"),
                new Route("/batch"),
                new Route("/community/overview"),
                new Route("/community/team"),
                new Route("/community/events"),
                new Route("/community/support"),
                new Route("/some/other/section"),
                new Route("/blog.atom")
            );
        }

        static List<Route> captureRoutes() {
            return Arrays.asList(
                new Route("/guides"),
                new Route("/guides/gs/{repositoryName}",
                    "/guides/gs/rest-service", "/guides/gs/scheduling-tasks",
                    "/guides/gs/consuming-rest", "/guides/gs/relational-data-access"),
                new Route("/projects"),
                new Route("/projects/{name}",
                    "/projects/spring-boot", "/projects/spring-framework",
                    "/projects/spring-data", "/projects/spring-security", "/projects/spring-cloud"),
                new Route("/blog/category/{category}.atom",
                    "/blog/category/releases.atom", "/blog/category/engineering.atom",
                    "/blog/category/news.atom"),
                new Route("/tools/{name}", "/tools/eclipse", "/tools/vscode"),
                new Route("/team/{username}",
                    "/team/jhoeller", "/team/bclozel", "/team/snicoll", "/team/sdeleuze", "/team/rstoyanchev"),
                new Route("/api/projects/{projectId}",
                    "/api/projects/spring-boot", "/api/projects/spring-framework",
                    "/api/projects/reactor", "/api/projects/spring-data",
                    "/api/projects/spring-restdocs", "/api/projects/spring-batch"),
                new Route("/api/projects/{projectId}/releases/{version}",
                    "/api/projects/spring-boot/releases/2.3.0", "/api/projects/spring-framework/releases/5.3.0",
                    "/api/projects/spring-boot/releases/2.2.0", "/api/projects/spring-framework/releases/5.2.0")
            );
        }

        static List<Route> regexRoute() {
            return Arrays.asList(
                new Route("/blog/{year:\\\\d+}/{month:\\\\d+}/{day:\\\\d+}/{slug}",
                    "/blog/2020/01/01/spring-boot-released", "/blog/2020/02/10/this-week-in-spring",
                    "/blog/2020/03/12/spring-one-conference-2020", "/blog/2020/05/17/spring-io-barcelona-2020",
                    "/blog/2020/05/17/spring-io-barcelona-2020", "/blog/2020/06/06/spring-cloud-release"),
                new Route("/user/{name:[a-z]+}",
                    "/user/emily", "/user/example", "/user/spring")
            );
        }

        static List<Route> allRoutes() {
            List<Route> routes = new ArrayList<>();
            routes.addAll(staticRoutes());
            routes.addAll(captureRoutes());
            routes.addAll(regexRoute());
            routes.add(new Route("/static/**", "/static/image.png", "/static/style.css"));
            routes.add(new Route("/**", "/notfound", "/favicon.ico"));
            return routes;
        }

    }

    /*
        github@spring-projects/spring-framework:JmsInvokerClientInterceptor.java:v5.0.4.RELEASE
        modifications: none
    */
    protected RemoteInvocationResult executeRequest(RemoteInvocation invocation) throws JMSException {
        Connection con = createConnection();
        Session session = null;
        try {
            session = createSession(con);
            Queue queueToUse = resolveQueue(session);
            Message requestMessage = createRequestMessage(session, invocation);
            con.start();
            Message responseMessage = doExecuteRequest(session, queueToUse, requestMessage);
            if (responseMessage != null) {
                return extractInvocationResult(responseMessage);
            } else {
                return onReceiveTimeout(invocation);
            }
        } finally {
            JmsUtils.closeSession(session);
            ConnectionFactoryUtils.releaseConnection(con, getConnectionFactory(), true);
        }

    }
}

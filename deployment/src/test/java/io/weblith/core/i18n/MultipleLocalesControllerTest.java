package io.weblith.core.i18n;

import io.quarkus.test.QuarkusUnitTest;
import io.weblith.core.config.WeblithConfig;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import test.controllers.RequestContextAwareController;

import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class MultipleLocalesControllerTest {

    private final static int OK = Status.OK.getStatusCode();

    @Inject
    WeblithConfig config;

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClasses(RequestContextAwareController.class)
                    .addAsResource(new StringAsset("quarkus.locales=en,fr,de\nquarkus.default-locale=de\nquarkus.http.test-port=0"), "application.properties"));

    @Test
    public void testGetDefaultLocale() {
        when().get("/Ctx/locale")
                .then().statusCode(OK).body(is("de"));
    }

    @Test
    public void testGetHeaderLocale() {
        given().header("Accept-Language", "en")
                .when().get("/Ctx/locale")
                .then().statusCode(OK).body(is("en"));
    }

    @Test
    public void testGetQueryParamLocale() {
        given().queryParam(config.switchLanguageParam, "fr")
                .when().get("/Ctx/locale")
                .then().statusCode(OK).body(is("fr"));
    }
}

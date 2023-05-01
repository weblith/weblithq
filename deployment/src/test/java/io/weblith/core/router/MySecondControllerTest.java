package io.weblith.core.router;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusUnitTest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import test.controllers.MySecondController;

@QuarkusTest
public class MySecondControllerTest {

    private final static int OK = Status.OK.getStatusCode();

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClasses(MySecondController.class)
                    .addAsResource(new StringAsset("quarkus.weblith.csrf-protected=false\nquarkus.http.test-port=0"), "application.properties"));

    @Test
    public void testMyPage() {
        when().get("/Controller/page").then().statusCode(OK);
    }

    @Test
    public void testMyPage2() {
        when().get("/Controller/page2/data").then().statusCode(OK).body(is("data"));
    }

    @Test
    public void testMyAction() {
        when().post("/Controller/action").then().statusCode(OK);
    }

}

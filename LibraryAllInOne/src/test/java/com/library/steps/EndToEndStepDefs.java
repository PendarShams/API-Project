package com.library.steps;


import com.library.pages.BasePage;
import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class EndToEndStepDefs extends BasePage {

    RequestSpecification givenPart = RestAssured.given().log().uri();
    Response response;
    ValidatableResponse thenPart;

    LoginPage loginPage = new LoginPage();
    BookPage bookPage = new BookPage();

    Map<String, Object> randomBook;
    Map<String, Object> randomUser;
    Map<String, String> tokenMap;
    String tokenValue;
    String baseURI = ConfigurationReader.getProperty("library.baseUri");

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
        givenPart.header("x-library-token", LibraryAPIUtil.getToken(userType));
    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        givenPart.accept(acceptHeader);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint);

        thenPart = response.then();

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        // OPT 1
        thenPart.statusCode(expectedStatusCode);

        // OPT 2
        Assert.assertEquals(expectedStatusCode, response.statusCode());

    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        // OPT 1
        thenPart.contentType(contentType);

        // OPT 2
        Assert.assertEquals(contentType, response.contentType());
    }

    @Then("Each {string} field should not be null")
    public void each_field_should_not_be_null(String path) {
        thenPart.body(path, everyItem(notNullValue()));

        /*
        JsonPath jp = thenPart.extract().jsonPath();
        List<Object> allData = jp.getList(path);
        for (Object eachData : allData) {
            Assert.assertNotNull(eachData);
        }
         */
    }

    //us02
    @Given("Path param is {string}")
    public void path_param_is(String pathPar) {
        // Write code here that turns the phrase above into concrete actions
        givenPart.pathParam("id", pathPar);
    }

    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String fieldName) {
        // Write code here that turns the phrase above into concrete actions
        JsonPath jp = response.jsonPath();
        String pathParamValue = jp.getString("id");
        thenPart.body(fieldName, is(pathParamValue));
    }

    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        // For other transformations you can register a DataTableType.
        List<String> fields = dataTable.asList(String.class);
        for (String field : fields) {
            thenPart.body(field, notNullValue());
        }
    }


    //us03-sc1

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String expectedContentType) {
        // Write code here that turns the phrase above into concrete actions
        givenPart.header("Content-Type", expectedContentType);
    }

    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String module) {
        // Write code here that turns the phrase above into concrete actions
        if (module.equalsIgnoreCase("book")) {
            randomBook = LibraryAPIUtil.getRandomBookMap();
            System.out.println("randomBook = " + randomBook);
        } else if (module.equalsIgnoreCase("user")) {
            randomUser = LibraryAPIUtil.getRandomUserMap();
            System.out.println("randomUser = " + randomUser);
        } else {
            System.out.println("Invalid module. Please provide book or user.");
        }

    }

    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        // Write code here that turns the phrase above into concrete actions
        RequestSpecification request;
        if (endpoint.equalsIgnoreCase("/add_book")) {
            request = givenPart.accept(ContentType.JSON).and().contentType(ContentType.JSON).body(randomBook);
            response = request.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint);
            //response=givenPart.when().post(baseURI + endpoint);
           // JsonPath jsonPath = response.jsonPath();
           // jsonPath.getString("book_id");

            //thenPart = response.then();
        } else if (endpoint.equalsIgnoreCase("/add_user")) {
        request = givenPart.accept(ContentType.JSON).and().contentType(ContentType.JSON).body(randomUser);
            response = request.post(baseURI + endpoint);

            //thenPart = response.then();
        } else if (endpoint.equalsIgnoreCase("/decode")) {
          // request = givenPart.accept(ContentType.JSON).and().contentType(ContentType.JSON).body(tokenValue);
            response = givenPart.when().post(baseURI + endpoint);
            //thenPart = response.then();
        }
        thenPart = response.then();
        //randomBook = {name=The House of Mirth9, isbn=1037932439, year=1308, author=Apryl Kub,
        // book_category_id=1, description=Chuck Norris doesn't pair program.}
    }

    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String path, String expectedValue) {
        // Write code here that turns the phrase above into concrete actions
        JsonPath jp = response.jsonPath();
        String actualValue = jp.getString(path);
        thenPart.body(path, is(expectedValue));
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String fieldName) {
        // Write code here that turns the phrase above into concrete actions
        thenPart.body(fieldName, notNullValue());
    }

    //us03-sc2
    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String userType) {
        // Write code here that turns the phrase above into concrete actions
// You can implement the login process for the Library UI here
        // For example, you can use Selenium or another
        // UI testing framework to automate the login process
        loginPage.login(userType);
        WebElement librarian10Login = Driver.getDriver().findElement(By.xpath("//*[text()='Test Librarian 10']"));
        Assert.assertTrue(librarian10Login.isDisplayed());
    }

    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String module) {
        // Write code here that turns the phrase above into concrete actions
        // You can implement the navigation process to the specified page in the Library UI here
        navigateModule(module);
        WebElement moduleHeader = Driver.getDriver().findElement(By.xpath("//*[text()='Book Management']"));
        Assert.assertTrue(moduleHeader.isDisplayed());
    }

    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {
        // Write code here that turns the phrase above into concrete actions
        //ISBN is unique
        //getting new book infos & UI ISBN from UI
        bookPage.retrieveBookInfo();
        String authorStr = bookPage.getValueMap(randomBook, "author");
        String UIIsbn = bookPage.findTheISBNByAuthor(authorStr);
//getting ISBN from API
        String APIIsbn = (String) randomBook.get("isbn");
        System.out.println("API isbn : " + APIIsbn);
//getting ISBN from DB
        String query = "select isbn from books where isbn = '" + APIIsbn + "'";
        DBUtil.runQuery(query);
        String DBIsbn = DBUtil.getCellValue(1, "isbn");
        System.out.println("DBIsbn = " + DBIsbn);
//Assertions
        Assert.assertEquals(DBIsbn, UIIsbn);
        Assert.assertEquals(DBIsbn, APIIsbn);

    }

    //us04
    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {
//API and DB comparaison
        String APIID = (String) randomUser.get("user_id");
        System.out.println("API user id : " + APIID);

        String query = "select id from users where id='" + APIID + "'";
        DBUtil.runQuery(query);
        String DBID = DBUtil.getCellValue(1, "id");
        System.out.println("DBID = " + DBID);

    }

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {
        String APIEmail = (String) randomUser.get("email");
        String APIPassword = (String) randomUser.get("password");
        loginPage.login(APIEmail, APIPassword);
        BrowserUtil.waitFor(3);
    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {
        BrowserUtil.waitFor(3);
        String APIFullName = (String) randomUser.get("full_name");
        WebElement dashBoardName = Driver.getDriver().findElement(By.xpath("//*[text()='" + APIFullName + "']"));
        Assert.assertTrue(dashBoardName.isDisplayed());

    }

    //us05

    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {
        tokenValue = LibraryAPIUtil.getToken(email, password);
        givenPart.header("Authorization", tokenValue);
    }

    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {
        givenPart
                .formParam("token", tokenValue);
    }


}





package com.library.steps;


import com.library.pages.BasePage;
import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.Driver;
import com.library.utility.LibraryAPI_Util;
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
import org.openqa.selenium.support.FindBy;


import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class APIStepDefs extends BasePage {

    RequestSpecification givenPart= RestAssured.given().log().uri();
    Response response;
    ValidatableResponse thenPart;

    LoginPage loginPage=new LoginPage();
    BookPage bookPage=new BookPage();
    Map<String, Object> randomBook;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
        givenPart.header("x-library-token", LibraryAPI_Util.getToken(userType));
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
        Assert.assertEquals(expectedStatusCode,response.statusCode());

    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        // OPT 1
        thenPart.contentType(contentType);

        // OPT 2
        Assert.assertEquals(contentType,response.contentType());
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
        public void following_fields_should_not_be_null(io.cucumber.datatable.DataTable dataTable){
            // Write code here that turns the phrase above into concrete actions
            // For automatic transformation, change DataTable to one of
            // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
            // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
            // Double, Byte, Short, Long, BigInteger or BigDecimal.
            //
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
    public void i_create_a_random_as_request_body(String string) {
        // Write code here that turns the phrase above into concrete actions
randomBook = LibraryAPI_Util.getRandomBookMap();
        System.out.println("randomBook = " + randomBook);
    }
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        // Write code here that turns the phrase above into concrete actions
        RequestSpecification request = givenPart.accept(ContentType.JSON).and().contentType(ContentType.JSON).body(randomBook);
       // RequestSpecification request = givenPart.header("Content-Type", "application/json").body(randomBook);
        response = request.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint);
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
//getting new book infos & UI ISBN from UI
        bookPage.retrieveBookInfo();
        String authorStr = bookPage.getValueMap(randomBook, "author");
        String UIIsbn = bookPage.findTheISBNByAuthor(authorStr);
//getting ISBN from API
        String APIIsbn = (String)randomBook.get("isbn");
        System.out.println("API isbn : "+ APIIsbn);
//getting ISBN from DB
        String query = "select isbn from books where isbn = '"+ APIIsbn +"'";
        DB_Util.runQuery(query);
        String DBIsbn = DB_Util.getCellValue(1, "isbn");
        System.out.println("DBIsbn = " + DBIsbn);
//Assertions
        Assert.assertEquals(DBIsbn, UIIsbn);
        Assert.assertEquals(DBIsbn, APIIsbn);

    }
    }





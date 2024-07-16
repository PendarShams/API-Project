package com.library.pages;

import com.library.utility.BrowserUtil;
import com.library.utility.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookPage extends BasePage {

    @FindBy(xpath = "//table/tbody/tr")
    public List<WebElement> allRows;

    @FindBy(xpath = "//input[@type='search']")
    public WebElement search;

    @FindBy(id = "book_categories")
    public WebElement mainCategoryElement;

    @FindBy(name = "name")
    public WebElement bookName;


    @FindBy(xpath = "(//input[@type='text'])[4]")
    public WebElement author;

    @FindBy(xpath = "//div[@class='portlet-title']//a")
    public WebElement addBook;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement saveChanges;

    @FindBy(xpath = "//div[@class='toast-message']")
    public WebElement toastMessage;

    @FindBy(name = "year")
    public WebElement year;

    @FindBy(name = "isbn")
    public WebElement isbn;

    @FindBy(id = "book_group_id")
    public WebElement categoryDropdown;


    @FindBy(id = "description")
    public WebElement description;

    @FindBy(xpath = "//input[@type='search']")
    public WebElement searchBookInput;



    public WebElement editBook(String book) {
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }

    public WebElement borrowBook(String book) {
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }


    public String findTheISBNByAuthor(String author) {
        BrowserUtil.waitFor(3);
    searchBookInput.sendKeys(author);
        BrowserUtil.waitFor(3);
        return Driver.getDriver().findElement(By.xpath("//td[2]")).getText();
//td[2]
    }
public void retrieveBookInfo() {
        Map<String, Object> bookInfo = new HashMap<>();
        bookInfo.put("name", Driver.getDriver().findElement(By.xpath("//td[3]")).getText());
    bookInfo.put("isbn", Driver.getDriver().findElement(By.xpath("//td[2]")).getText());
    bookInfo.put("year", Driver.getDriver().findElement(By.xpath("//td[6]")).getText());
    bookInfo.put("author", Driver.getDriver().findElement(By.xpath("//td[4]")).getText());
    System.out.println("bookInfo in UI = " + bookInfo);
}

public String getValueMap(Map<String, Object> map, String name) {
    Object authorObj = map.get("author");
    if (authorObj instanceof String) {
        return (String) authorObj;
    } else {
        System.out.println("Author not found or not a string.");
    }
    return (String) authorObj;
}




}

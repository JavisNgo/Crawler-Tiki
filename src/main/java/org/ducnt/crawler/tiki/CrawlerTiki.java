/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.ducnt.crawler.tiki;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ducnt.dto.ProductDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author Admin
 */
public class CrawlerTiki {

    public static void main(String[] args) {
        List<ProductDTO> list = crawlerDataOfTiki();
        storeData(list);
    }

    private static List<ProductDTO> crawlerDataOfTiki() {
        List<ProductDTO> list = new ArrayList<>();
        try {
            WebDriver myBrowser;
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

            ChromeOptions opt = new ChromeOptions();
            opt.addArguments("--incognito");

            myBrowser = new ChromeDriver();

            myBrowser.get("https://tiki.vn");

            myBrowser.manage().window().maximize();
            By selector = By.cssSelector("a.product-item");
            //By selector = By.className("style__ProductLink-sc-7xd6qw-2");

            for (WebElement element : myBrowser.findElements(selector)) {
                String name = element.findElement(By.cssSelector("div[class='name']")).getText();
                String price = element.findElement(By.cssSelector("div[class='price-discount__price']")).getText();
                String link = element.getAttribute("href");
                try {
                    String discount = element.findElement(By.cssSelector("div[class='price-discount__discount']")).getText();
//                    System.out.println(name +" "+price);
//                    System.out.println(discount);
                    list.add(new ProductDTO(name, price, discount, link));
                } catch (Exception e) {
                    //System.out.println(name +" "+price);
                    list.add(new ProductDTO(name, price, null, link));
                }
            }
            Thread.sleep(5000);
            //myBrowser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void storeData(List<ProductDTO> list) {
        try {
            String filePath = "data.json";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            //định dạng 
            try (FileWriter writer = new FileWriter(filePath)) {
                Type productListType = new TypeToken<List<ProductDTO>>() {
                }.getType();
                //String json = gson.toJson(list, productListType);
                gson.toJson(list, productListType, writer);
                //writer.write(json);
            }
            System.out.println("Dữ liệu đã được lưu vào tệp JSON.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class avatar_read_write {

    public static WebDriver driver ;
    private static String input_file = "/home/raghunathant/Avatar/Automation/resource/v0.91-general-qa-phase2.csv";
    private static String write_file = "/home/raghunathant/Avatar/Automation/resource/v0.91-general-qa-phase2-result.csv";
    private static int start_index =2;
    private static int end_index = 350;
    private static  int ques_column = 21;
    //private static  int ques_column = 0;
    private static  int ticket_column = 3;

    private static BufferedWriter write_output ;

    private static List<String> ques_list = new ArrayList<String>();
    private static List<String> ticket_list = new ArrayList<String>();

    public static void main(String[] args) throws MalformedURLException, InterruptedException
    {

        try {
            read_csv(start_index, end_index, ques_column, ticket_column);

            initialize();

            //Test();

            SendQuestionsToAvatar();

            write_output.close();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void read_csv(int start_index, int end_index, int ques_column, int ticket_column)
    {
        String line;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(input_file));
            int line_count = 0;
            while((line=br.readLine()) != null)
            {
                line_count++;

                if(line_count >= start_index & line_count < end_index) {
                    String[] read_line = line.split(",");
                    ques_list.add(read_line[ques_column]);
                    ticket_list.add(read_line[ticket_column]);
                }

                if(line_count > end_index)
                {
                    break;
                }
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void write_csv(String ticket, String steps, String result)
    {
        try
        {
            write_output.write(ticket +"," + steps +","+ result);
            //write_output.write(steps +","+ result);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void initialize() throws InterruptedException
    {

        try {
            //
            write_output = new BufferedWriter(new FileWriter(write_file, true));

            driver = new ChromeDriver();

            driver.get("http://localhost:8000");

            //driver.get("https://www.google.com");
            driver.manage().window().maximize();
            WebDriverWait wait = new WebDriverWait(driver, 40);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@class,'app-message-input')]")));

            Thread.sleep(20000);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //driver.quit();
    }

    public static boolean EnterText(String xpath, String input)
    {

        boolean blnRes = false;

        try
        {
            WebElement webElement = driver.findElement(By.xpath(xpath));
            webElement.sendKeys(input.replace("\"",""));
            blnRes = true;

        }catch(Exception ex)
        {
            //System.out.println(ex.getMessage());

            ex.printStackTrace();
        }

        return blnRes;
    }

    public static boolean clickButton(String xpath)
    {
        boolean blnRes = false;

        try
        {
            WebElement webElement = driver.findElement(By.xpath(xpath));
            webElement.click();

            blnRes = true;

        }catch(Exception ex)
        {
            //System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return blnRes;
    }

    public static String lastResponsefromAvatar()
    {
        String result = "";
        try
        {
            String chat_msg_path = "//*[contains(@class,'app-message')]//*[contains(@class,'chat-message')]//*[contains(@class,'msg')]";
            String human_msg_path = "//*[contains(@class,'app-message human')]//*[contains(@class,'chat-message')]//*[contains(@class,'msg')]";
            String chat_msg_label = "//*[contains(@class,'app-message')]//*[contains(@class,'chat-message-label')]";

            String human_msg = driver.findElement(By.xpath(human_msg_path)).getText();

            var chat_msg_elements = driver.findElements(By.xpath(chat_msg_path));
            var chat_label_elements = driver.findElements(By.xpath(chat_msg_label));

            var size = chat_msg_elements.size();

            for(int i=0; i<size; i++)
            {
                var element = chat_msg_elements.get(i);
                var msg = element.getText();
                if(i >= chat_label_elements.size())
                {
                    break;
                }
                var rule = chat_label_elements.get(i).getText();
                if(human_msg.equals(msg))
                {
                    break;
                }
                result += "Rule : " + rule + " Message : " + msg + System.lineSeparator();

            }


        }
        catch (Exception ex)
        {
            result += "Exception - " + ex.getMessage();
            //System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return result;
    }

    public static void SendQuestionsToAvatar()
    {
        try {

            //for(String input_text : ques_list)
            for(int index = 0; index <= ques_list.size(); index++)
            {
                String input_text = ques_list.get(index);
                //1. Enter Text
                EnterText("//*[contains(@class,'app-message-input')]", input_text);

                //2. Send to Avatar
                clickButton("//*[contains(@class,'app-send-button')]");

                //3. Wait
                Thread.sleep(15000);

                //4. Read Last Response from Avatar
                String result = lastResponsefromAvatar();

                //System.out.println(result);
                write_csv(ticket_list.get(index) , input_text, result);
                //write_csv("" , input_text, result);
            }
        }catch (Exception ex){
            //System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }


    public static void Test()
    {
        try {
            //1. Enter Text
            EnterText("//*[contains(@class,'app-message-input')]", "Hi Grace");

            //2. Send to Avatar
            clickButton("//*[contains(@class,'app-send-button')]");

            //3. Wait
            Thread.sleep(15000);

            //4. Read Last Response from Avatar
            String result = lastResponsefromAvatar();

        }catch (Exception ex){
            //System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}

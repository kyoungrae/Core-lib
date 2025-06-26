package com.system.common.util.pageredirect;

import com.system.common.util.message.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/common/redirectPage")
public class PageRedirectController {

    private final MessageService messageService;
    public PageRedirectController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/redirect")
    @ResponseBody
    public String redirectToNewPage(@RequestParam("url") String param) {
        String resourcePath = "templates/page" + param;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return loadErrorPage();
            }
            var content = messageMatcher(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),"ko");
            return content;
        } catch (Exception e) {
            return loadErrorPage();
        }
    }
    private String messageMatcher(String content,String lang){

        Pattern pattern = Pattern.compile("\\[Page\\.Message\\]\\.Message\\.Label\\.Array\\[\"(.*?)\"\\]");
        Matcher matcher = pattern.matcher(content);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            String message = messageService.getMessage(key, lang);
            matcher.appendReplacement(result, message);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private String loadErrorPage() {
        String errorPagePath = "templates/page/common/404.html";
        try (InputStream errorStream = getClass().getClassLoader().getResourceAsStream(errorPagePath)) {
            if (errorStream == null) {
                return "<div>404 Page Not Found</div>";
            }
            return new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "<div>404 Page Not Found</div>";
        }
    }
}
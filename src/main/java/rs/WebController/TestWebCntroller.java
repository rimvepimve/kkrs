package rs.WebController;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
public class TestWebCntroller {

    @RequestMapping(value="/test/string")
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String returnTestString(){
        return "Veikia!";
    }

    @RequestMapping(value="/test/main")
    //@ResponseStatus(value=HttpStatus.OK)
    public String returnMainPage(){
        return "main";
    }

}

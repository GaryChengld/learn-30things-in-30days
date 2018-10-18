package io.examples.keycloak;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author Gary Cheng
 */
@Controller
public class MovieController {

    @GetMapping(path = "/movies")
    public String getMovies(Model model){
        model.addAttribute("movies", Arrays.asList("STAR WARS: EPISODE IV A NEW HOPE", "STAR WARS: EPISODE V THE EMPIRE STRIKES BACK","STAR WARS: EPISODE VI RETURN OF THE JEDI"));
        return "movies";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/";
    }
}

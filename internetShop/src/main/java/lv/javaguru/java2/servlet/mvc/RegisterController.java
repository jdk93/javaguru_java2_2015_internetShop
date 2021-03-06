package lv.javaguru.java2.servlet.mvc;

import com.sun.corba.se.impl.io.TypeMismatchException;
import lv.javaguru.java2.AccessLevel;
import lv.javaguru.java2.PasswordHash;
import lv.javaguru.java2.database.DBException;
import lv.javaguru.java2.database.UserDAO;
import lv.javaguru.java2.domain.User;
import lv.javaguru.java2.servlet.mvc.AccessCheck.AccessChecker;
import lv.javaguru.java2.servlet.mvc.models.MVCModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

//@Component
//public class RegisterController extends AccessController {

@Controller
public class RegisterController {
    @Autowired
    @Qualifier("ORM_UserDAO")
    private UserDAO userDAO;

//    @Override
//    public MVCModel safeRequest(HttpServletRequest request, HttpServletResponse response) throws TypeMismatchException {


    @RequestMapping(value = "register", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView model = AccessChecker.check(request);
        if (model != null) return model;

        model = new ModelAndView("register");

        HttpSession session = request.getSession();
        //session.setAttribute("page_name", "Registration");

        if (request.getMethod().equals("POST")) {
            User user = new User(
                    request.getParameter("name"),
                    request.getParameter("surname"),
                    request.getParameter("gender"),
                    request.getParameter("phone"),
                    request.getParameter("email"),
                    request.getParameter("username"),
                    request.getParameter("password"),
                    AccessLevel.CLIENT.getValue());

            if(!checkFields(user)) {
                //return new MVCModel("/register.jsp", "All fields must be filled.");
                return  model.addObject("model", "All fields must be filled.");
            }


            User testUser = null;
            try {
                testUser = userDAO.getByLogin(user.getLogin());
            } catch (DBException e) {
                e.printStackTrace();
            }

            try {
                if (testUser == null) {
                    user.setPassword(PasswordHash.createHash(user.getPassword()));
                    try {
                        userDAO.create(user);
                        session.setAttribute("user_id", userDAO.getByLogin(user.getLogin()).getId());
                    } catch (DBException e) {
                        e.printStackTrace();
                        //return new MVCModel("/register.jsp", "Something gone wrong with DB.");
                        return model.addObject("model", "Something gone wrong with DB.");
                    }

                    session.setAttribute("username", request.getParameter("username"));
                    session.setAttribute("name", request.getParameter("name"));
                    session.setAttribute("surname", request.getParameter("surname"));
                    session.setAttribute("access_level", AccessLevel.CLIENT.getValue());

                    Map<Long, Integer> inCart
                            = (HashMap<Long, Integer>) session.getAttribute("in_cart");

                    if (inCart.size() > 0) {
                        session.setAttribute("transfer", "yes");
//                        return new MVCModel("/transfer.jsp", "You have products in your cart. " +
//                                "Do you want to transfer them on your account?");

                        return model.addObject("model", "You have products in your cart. " +
                                "Do you want to transfer them on your account?");
                    }
                } else {
                    //return new MVCModel("/register.jsp", "This login is already in use.");
                    return model.addObject("model", "This login is already in use.");
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }

        //return new MVCModel("/register.jsp", null);
        return model;
    }

    private boolean checkFields(User user) {
        return !(user.getEmail().isEmpty()
                || user.getGender().isEmpty()
                || user.getPhone().isEmpty()
                || user.getName().isEmpty()
                || user.getSurname().isEmpty()
                || user.getLogin().isEmpty()
                || user.getPassword().isEmpty());
    }
}

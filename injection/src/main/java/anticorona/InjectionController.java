package anticorona;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 @RestController
 public class InjectionController {

     @Autowired
     InjectionRepository injectionRepository;

     @RequestMapping(value = "/injections/checkStatus",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8")
    public boolean checkStatus(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("##### /injection/checkStatus  called #####");

        boolean status = false;      

        System.out.println("##### request.getParameter#####"+request.getParameter("injectionId"));
        Long injectionId = Long.valueOf(request.getParameter("injectionId"));        
      

        Optional<Injection> injection= injectionRepository.findById(injectionId);
      
        if(injection.isPresent()){
            Injection injectionValue = injection.get();


            System.out.println("##### injectionValue.getStatus()###############"+injectionValue.getStatus());         
            

            //발급  가능한지 상태 체크 
            if(injectionValue.getStatus().equals("INJECTED")) {
               
                status = true;              
                injectionRepository.save(injectionValue);
            }
        }

        return status;
     }
 }

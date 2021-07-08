
package anticorona.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="injection",  url="http://localhost:8081")
public interface InjectionService {

    @RequestMapping(method= RequestMethod.GET, path="/injections/checkStatus")
    public boolean checkStatus(@RequestParam("injectionId") Long injectionId);

  

}
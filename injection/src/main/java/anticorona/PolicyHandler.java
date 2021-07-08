package anticorona;

import anticorona.config.kafka.KafkaProcessor;

import java.util.Optional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired InjectionRepository injectionRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverApplyingCancelled_ModifyInjection(@Payload ApplyingCancelled applyingCancelled){

        if(!applyingCancelled.validate()) return;

        System.out.println("\n\n##### listener ModifyStock : " + applyingCancelled.toJson() + "\n\n");

        // 예약수량 감소 //
      //  Optional<Injection> injection = injectionRepository.findById(applyingCancelled.getInjectionId());
      //  if(injection.isPresent()){
      //      Injection injectionValue = injection.get();
     //       injectionValue.setBookQty(injectionValue.getBookQty()-1);
     //       injectionRepository.save(injectionValue);
     //   }
        
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCompleted_ModifyInjection(@Payload Completed completed){

        if(!completed.validate()) return;

        System.out.println("\n\n##### listener ModifyStock : " + completed.toJson() + "\n\n");

        // 재고수량 & 예약수량 감소 //
     //   Optional<Injection> injection = injectionRepository.findById(completed.getInjectionId());
    //    if(injection.isPresent()){
    //        Injection injectionValue = injection.get();
    //        injectionValue.setStock(injectionValue.getStock()-1);
    //        injectionValue.setBookQty(injectionValue.getBookQty()-1);
    //        injectionRepository.save(injectionValue);
    //    }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}

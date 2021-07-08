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
    
    @Autowired ApplyingRepository applyingRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCompleted_UpdateStatus(@Payload Completed completed){

        if(!completed.validate()) return;

        System.out.println("\n\n##### listener UpdateStatus : " + completed.toJson() + "\n\n");
        Optional<Applying> applying = applyingRepository.findById(completed.getApplyingId​());
        if(applying.isPresent()){
            Applying applyingValue = applying.get();
            //발급완료
            applyingValue.setIssueStatus("ISSUE_COMPLETED");
            applyingRepository.save(applyingValue);
        }
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}

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

    @Autowired
    IssueRepository issueRepository;


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverApplied_AcceptApplying(@Payload Applied applied){

        if(!applied.validate()) return;

        System.out.println("\n\n##### listener AcceptBooking : " + applied.toJson() + "\n\n");

        // 발급준비(AcceptApplying) //
        
        Issue issue = new Issue();

        issue.setIssueStatus("ISSUE_READY");
        issue.setApplyingId(applied.getApplyingId());            
        issue.setInjectionId(applied.getInjectionId());
        issue.setUserId(applied.getUserId());

        issueRepository.save(issue);     
          
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverApplyingCancelled_CancelApplying(@Payload ApplyingCancelled applyingCancelled){

        if(!applyingCancelled.validate()) return;

        System.out.println("\n\n##### listener AcceptCancelApplying : " + applyingCancelled.toJson() + "\n\n");

        // 발급취소(AcceptCancelApplying) //        
        Optional<Issue> issueOptional = issueRepository.findByApplyingId(applyingCancelled.getApplyingId());
        if(issueOptional.isPresent()){
            Issue issue = issueOptional.get();
            issue.setIssueStatus("ISSUE_CANCELLED");
            issueRepository.save(issue);
        }            
        
    }

}

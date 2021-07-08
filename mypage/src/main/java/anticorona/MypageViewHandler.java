package anticorona;

import anticorona.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MypageViewHandler {


    @Autowired
    private MypageRepository mypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenApplied_then_CREATE_1 (@Payload Applied applied) {
        try {

            if (!applied.validate()) return;

            // view 객체 생성
            Mypage mypage = new Mypage();
            // view 객체에 이벤트의 Value 를 set 함
            mypage.setApplyingId(applied.getApplyingId());
            mypage.setVcName(applied.getVcName());
            mypage.setUserId(applied.getUserId());
            mypage.setInjectionId(applied.getInjectionId());
            mypage.setStatus(applied.getStatus());
            mypage.setIssueStatus(applied.getIssueStatus());
            // view 레파지 토리에 save
            mypageRepository.save(mypage);
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenApplyingCancelled_then_UPDATE_1(@Payload ApplyingCancelled applyingCancelled) {
        try {
            if (!applyingCancelled.validate()) return;
                // view 객체 조회
            Optional<Mypage> mypageOptional = mypageRepository.findByApplyingId(applyingCancelled.getApplyingId());
            if( mypageOptional.isPresent()) {
                Mypage mypage = mypageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setIssueStatus(applyingCancelled.getIssueStatus());
                // view 레파지 토리에 save
                mypageRepository.save(mypage);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenCompleted_then_UPDATE_2(@Payload Completed completed) {
        try {
            if (!completed.validate()) return;
                // view 객체 조회
            Optional<Mypage> mypageOptional = mypageRepository.findByApplyingId(completed.getApplyingId​());
            if( mypageOptional.isPresent()) {
                Mypage mypage = mypageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    mypage.setIssueStatus(completed.getIssueStatus());
                // view 레파지 토리에 save
                mypageRepository.save(mypage);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
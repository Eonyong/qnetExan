package com.qnetexam.qnetexan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.util.Random;

public class CardContainer extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private boolean isShowingBackLayout = false;
    private Bundle bundle;
    private Flip_Back_View flip_back_view = new Flip_Back_View();
    private Flip_Front_View flip_front_view = new Flip_Front_View();
    private int clickNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_container);

//        FullAd();

        QnA qna = new QnA();
        final String[] questionList = qna.questionList;
        final String[] answerList = qna.answerList;
        Button button = findViewById(R.id.go_back);
        RelativeLayout card_container = findViewById(R.id.card_container);

        if (savedInstanceState == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.card_container, new Flip_Front_View())
                    .commit();
        else{
            isShowingBackLayout = getFragmentManager().getBackStackEntryCount() > 0;
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        card_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCard();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickNum++;

//                if (clickNum > 5){
//                    if (fullAd.isLoaded()){
//                        fullAd.show();
//                        clickNum = 0;
//                    }
//                }
                int random = new Random().nextInt(questionList.length);
                bundle = new Bundle();
                bundle.putString("rndAns", answerList[random]);
                bundle.putString("rndQue", questionList[random]);
                flip_back_view = new Flip_Back_View();
                flip_back_view.setArguments(bundle);

                flip_front_view = new Flip_Front_View();
                flip_front_view.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.card_container, flip_back_view)
                        .commit();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.card_container, flip_front_view)
                        .commit();
            }
        });

    }

    private void flipCard() {
        if (isShowingBackLayout){
            getFragmentManager().popBackStack();
            return;
        }
        isShowingBackLayout=true;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.card_container, flip_back_view).addToBackStack(null).commit();
    }
    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public void onBackStackChanged() {
        isShowingBackLayout = (getFragmentManager().getBackStackEntryCount() > 0);
    }

//    private void FullAd(){
//        fullAd = new InterstitialAd(this); //새 광고를 만듭니다.
//        fullAd.setAdUnitId(getResources().getString(R.string.full_ad)); //이전에 String에 저장해 두었던 광고 ID를 전면 광고에 설정합니다.
//        AdRequest adRequest1 = new AdRequest.Builder().build(); //새 광고요청
//        fullAd.loadAd(adRequest1); //요청한 광고를 load 합니다.
//        fullAd.setAdListener(new AdListener() { //전면 광고의 상태를 확인하는 리스너 등록
//
//            @Override
//            public void onAdClosed() { //전면 광고가 열린 뒤에 닫혔을 때
//                AdRequest adRequest1 = new AdRequest.Builder().build();  //새 광고요청
//                fullAd.loadAd(adRequest1); //요청한 광고를 load 합니다.
//            }
//        });
//    }

}
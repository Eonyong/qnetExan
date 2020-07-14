package com.qnetexam.qnetexan;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private long backKeyPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //광고 뷰 생성
        MobileAds.initialize(this, getString(R.string.ad_id));
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //리사이클러뷰 생성
        final ArrayList<ProblemList> problemListArrayList = solveProblem();
        ProblemAdapter adapter = new ProblemAdapter(this, problemListArrayList);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);

        //사이드바 메뉴 호출 버튼
        FloatingActionButton floatingActionButton = findViewById(R.id.side_bar_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        initiator();
    }


    //텍스트뷰에 문제와 답 생성
    private ArrayList<ProblemList> solveProblem(){

        QnA qna = new QnA();
        String[] questionList = qna.questionList;
        String[] answerList = qna.answerList;

        ArrayList<ProblemList> list = new ArrayList<>();

        for (int i = 0; i <questionList.length; i++){
            ProblemList problemList = new ProblemList();
            problemList.setQuestion(questionList[i]);
            problemList.setAnswer(answerList[i]);
            list.add(problemList);
        }
        return list;
    }

    //뒤로가기 두번 클릭시 종료되는 기능
    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis()>backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            finish();
        }
    }


    //NavigationBar function
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.quest_alert) {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);
        }
        else if (menuItem.getItemId() == R.id.card_game){
            Intent intent = new Intent(getApplicationContext(), CardContainer.class);
            startActivity(intent);
        }
        else if (menuItem.getItemId() == R.id.go_to_link){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.q-net.or.kr/man001.do?gSite=Q"));
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration){
        super.onConfigurationChanged(configuration);
        drawerToggle.onConfigurationChanged(configuration);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        if (drawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initiator(){
        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.navigation_header_container);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

}

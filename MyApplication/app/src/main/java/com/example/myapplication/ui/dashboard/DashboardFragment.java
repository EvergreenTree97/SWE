package com.example.myapplication.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DataHighway;
import com.example.myapplication.GetDataHighway;
import com.example.myapplication.LoadDataHighway;
import com.example.myapplication.MyAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit_Highway;
import com.example.myapplication.Road;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private MyAdapter mAdapter;

    public List<DataHighway> result_data = new ArrayList<>();
    private ArrayList<Road> list = new ArrayList<>();
    private ArrayList<Road> data_s = new ArrayList<>();

    public LoadDataHighway result;
    String key = "8098105639";
    private DashboardViewModel dashboardViewModel;
    private Context mContext;

    Spinner spinner, spinner2;
    ImageView img = null;
    ArrayAdapter<CharSequence> adspin, adspin2;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        insert_data();
        refresh();
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_traffic, container, false);

        spinner = (Spinner) root.findViewById(R.id.spinner2);
        spinner.setPrompt("노선을 선택하세요.");
        spinner2 = (Spinner) root.findViewById(R.id.spinner3);
        spinner.setPrompt("상/하행을 선택하세요.");

        adspin = ArrayAdapter.createFromResource(mContext, R.array.road, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                data_s.clear();
                ((TextView)parent.getChildAt(0)).setTextColor(Color.RED);
                for(int k = 0; k<list.size(); k++){
                    if(adspin.getItem(position).equals(list.get(k).getRouteName()) && adspin2.getItem(position).equals("상행") && list.get(k).getUpdownTypeCode().equals("S")){
                        data_s.add(new Road(list.get(k).getConzoneName(), list.get(k).getRouteName(), list.get(k).getStdHour(), list.get(k).getUpdownTypeCode()));
                        data_s.get(data_s.size()-1).setGrade(list.get(k).getGrade());
                    }
                    if(adspin.getItem(position).equals(list.get(k).getRouteName()) && adspin2.getItem(position).equals("하행")&&list.get(k).getUpdownTypeCode().equals("E")){
                        data_s.add(new Road(list.get(k).getConzoneName(), list.get(k).getRouteName(), list.get(k).getStdHour(), list.get(k).getUpdownTypeCode()));
                        data_s.get(data_s.size()-1).setGrade(list.get(k).getGrade());
                    }
                }
                mAdapter = new MyAdapter(img, data_s);
                recyclerView.setAdapter(mAdapter);

            }
            public void onNothingSelected(AdapterView<?>  parent) {}
        });
        adspin2 = ArrayAdapter.createFromResource(mContext, R.array.updown, android.R.layout.simple_spinner_item);
        adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adspin2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                data_s.clear();
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLUE);
                for(int k = 0; k<list.size(); k++){
                    if(adspin2.getItem(position).equals("상행")&&list.get(k).getUpdownTypeCode().equals("S")){
                        data_s.add(new Road(list.get(k).getConzoneName(), list.get(k).getRouteName(), list.get(k).getStdHour(), list.get(k).getUpdownTypeCode()));
                        data_s.get(data_s.size()-1).setGrade(list.get(k).getGrade());
                    }
                    if(adspin2.getItem(position).equals("하행")&&list.get(k).getUpdownTypeCode().equals("E")){
                        data_s.add(new Road(list.get(k).getConzoneName(), list.get(k).getRouteName(), list.get(k).getStdHour(), list.get(k).getUpdownTypeCode()));
                        data_s.get(data_s.size()-1).setGrade(list.get(k).getGrade());
                    }
                }
                mAdapter = new MyAdapter(img, data_s);
                recyclerView.setAdapter(mAdapter);
            }
            public void onNothingSelected(AdapterView<?>  parent) {}
        });
        recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);


        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    //데이터 넣어주기만 하면 끝납니다.
    public void insert_data() {
        //콘조, 루트, 시간, 상하행
        list.add(new Road("한남IC-잠원IC","경부선","default","E"));
        list.add(new Road("잠원IC-반포IC","경부선","default","E"));
        list.add(new Road("서초IC-양재IC","경부선","default","E"));
        list.add(new Road("양재IC-금토JC","경부선","default","E"));
        list.add(new Road("대왕판교IC-판교JC","경부선","default","E"));
        list.add(new Road("판교JC-판교IC","경부선","default","E"));
        list.add(new Road("판교IC-서울요금소","경부선","default","E"));
        list.add(new Road("서울요금소-신갈JC","경부선","default","E"));
        list.add(new Road("신갈JC-수원신갈IC","경부선","default","E"));
        list.add(new Road("수원신갈IC-기흥IC","경부선","default","E"));
        list.add(new Road("기흥IC-기흥동탄IC","경부선","default","E"));
        list.add(new Road("기흥동탄IC-동탄JC","경부선","default","E"));
        list.add(new Road("동탄JC-오산IC","경부선","default","E"));
        list.add(new Road("오산IC-안성JC","경부선","default","E"));
        list.add(new Road("안성JC-안성IC","경부선","default","E"));
        list.add(new Road("안성IC-북천안IC","경부선","default","E"));
        list.add(new Road("북천안IC-천안IC","경부선","default","E"));
        list.add(new Road("천안IC-천안JC","경부선","default","E"));
        list.add(new Road("천안JC-목천IC","경부선","default","E"));
        list.add(new Road("목천IC-옥산JC","경부선","default","E"));
        list.add(new Road("옥산JC-옥산IC","경부선","default","E"));
        list.add(new Road("옥산IC-청주IC","경부선","default","E"));
        list.add(new Road("청주IC-남이JC","경부선","default","E"));
        list.add(new Road("남이JC-청주JC","경부선","default","E"));
        list.add(new Road("청주JC-남청주IC","경부선","default","E"));
        list.add(new Road("남청주IC-신탄진IC","경부선","default","E"));
        list.add(new Road("신탄진IC-회덕JC","경부선","default","E"));
        list.add(new Road("회덕JC-대전IC","경부선","default","E"));
        list.add(new Road("대전IC-비룡JC","경부선","default","E"));
        list.add(new Road("비룡JC-옥천IC","경부선","default","E"));
        list.add(new Road("옥천IC-금강IC","경부선","default","E"));
        list.add(new Road("금강IC-영동IC","경부선","default","E"));
        list.add(new Road("영동IC-황간IC","경부선","default","E"));
        list.add(new Road("황간IC-추풍령IC","경부선","default","E"));
        list.add(new Road("추풍령IC-김천IC","경부선","default","E"));
        list.add(new Road("김천IC-동김천IC","경부선","default","E"));
        list.add(new Road("동김천IC-김천JC","경부선","default","E"));
        list.add(new Road("김천JC-구미IC","경부선","default","E"));
        list.add(new Road("구미IC-남구미IC","경부선","default","E"));
        list.add(new Road("남구미IC-왜관IC","경부선","default","E"));
        list.add(new Road("왜관IC-칠곡물류IC","경부선","default","E"));
        list.add(new Road("칠곡물류IC-금호JC","경부선","default","E"));
        list.add(new Road("북대구IC-도동JC","경부선","default","E"));
        list.add(new Road("도동JC-동대구JC","경부선","default","E"));
        list.add(new Road("동대구JC-경산IC","경부선","default","E"));
        list.add(new Road("경산IC-영천IC","경부선","default","E"));
        list.add(new Road("영천IC-영천JC","경부선","default","E"));
        list.add(new Road("영천JC-건천IC","경부선","default","E"));
        list.add(new Road("건천IC-경주IC","경부선","default","E"));
        list.add(new Road("경주IC-활천IC","경부선","default","E"));
        list.add(new Road("활천IC-언양JC","경부선","default","E"));
        list.add(new Road("언양JC-서울산IC","경부선","default","E"));
        list.add(new Road("서울산IC-통도사IC","경부선","default","E"));
        list.add(new Road("통도사IC- 통도사 하이패스IC","경부선","default","E"));
        list.add(new Road("통도사 하이패스IC- 양산IC","경부선","default","E"));
        list.add(new Road("양산IC-양산JC","경부선","default","E"));
        list.add(new Road("양산JC-노포JC","경부선","default","E"));
        list.add(new Road("노포JC-노포IC","경부선","default","E"));
        list.add(new Road("노포IC-부산요금소","경부선","default","E"));
        list.add(new Road("부산요금소-영락IC","경부선","default","E"));
        list.add(new Road("영락IC-구서IC","경부선","default","E"));
        list.add(new Road("판교JC-청계요금소","수도권제1순환선","default","S"));
        list.add(new Road("청계요금소-학의JC","수도권제1순환선","default","S"));
        list.add(new Road("학의JC-평촌IC","수도권제1순환선","default","S"));
        list.add(new Road("평촌IC-산본IC","수도권제1순환선","default","S"));
        list.add(new Road("산본IC-조남JC","수도권제1순환선","default","S"));
        list.add(new Road("조남JC-도리JC","수도권제1순환선","default","S"));
        list.add(new Road("도리JC-안현JC","수도권제1순환선","default","S"));
        list.add(new Road("안현JC-시흥요금소","수도권제1순환선","default","S"));
        list.add(new Road("시흥요금소-시흥IC","수도권제1순환선","default","S"));
        list.add(new Road("시흥IC-장수IC","수도권제1순환선","default","S"));
        list.add(new Road("장수IC-송내IC","수도권제1순환선","default","S"));
        list.add(new Road("송내IC-중동IC","수도권제1순환선","default","S"));
        list.add(new Road("중동IC-서운JC","수도권제1순환선","default","S"));
        list.add(new Road("서운JC-계양IC","수도권제1순환선","default","S"));
        list.add(new Road("계양IC-노오지JC","수도권제1순환선","default","S"));
        list.add(new Road("노오지JC-김포요금소","수도권제1순환선","default","S"));
        list.add(new Road("김포요금소-김포IC","수도권제1순환선","default","S"));
        list.add(new Road("김포IC-자유로IC","수도권제1순환선","default","S"));
        list.add(new Road("자유로IC-일산IC","수도권제1순환선","default","S"));
        list.add(new Road("일산IC-고양IC","수도권제1순환선","default","S"));
        list.add(new Road("고양IC-통일로IC","수도권제1순환선","default","S"));
        list.add(new Road("통일로IC-양주요금소","수도권제1순환선","default","S"));
        list.add(new Road("양주요금소-송추IC","수도권제1순환선","default","S"));
        list.add(new Road("송추IC-의정부IC","수도권제1순환선","default","S"));
        list.add(new Road("의정부IC-별내IC","수도권제1순환선","default","S"));
        list.add(new Road("별내IC-퇴계원IC","수도권제1순환선","default","S"));
        list.add(new Road("퇴계원IC-구리IC","수도권제1순환선","default","S"));
        list.add(new Road("구리IC-남양주IC","수도권제1순환선","default","S"));
        list.add(new Road("남양주IC-구리남양주요금소","수도권제1순환선","default","S"));
        list.add(new Road("구리남양주요금소-토평IC","수도권제1순환선","default","S"));
        list.add(new Road("토평IC-강밀IC","수도권제1순환선","default","S"));
        list.add(new Road("강밀IC-상밀IC","수도권제1순환선","default","S"));
        list.add(new Road("상밀IC-하남JC","수도권제1순환선","default","S"));
        list.add(new Road("하남JC-서하남IC","수도권제1순환선","default","S"));
        list.add(new Road("서하남IC-송파IC","수도권제1순환선","default","S"));
        list.add(new Road("송파IC-성남요금소","수도권제1순환선","default","S"));
        list.add(new Road("성남요금소-성남IC","수도권제1순환선","default","S"));
        list.add(new Road("잠원IC-한남IC","경부선","default","S"));
        list.add(new Road("반포IC-잠원IC","경부선","default","S"));
        list.add(new Road("양재IC-서초IC","경부선","default","S"));
        list.add(new Road("금토JC-양재IC","경부선","default","S"));
        list.add(new Road("판교JC-대왕판교IC","경부선","default","S"));
        list.add(new Road("판교IC-판교JC","경부선","default","S"));
        list.add(new Road("서울요금소-판교IC","경부선","default","S"));
        list.add(new Road("신갈JC-서울요금소","경부선","default","S"));
        list.add(new Road("수원신갈IC-신갈JC","경부선","default","S"));
        list.add(new Road("기흥IC-수원신갈IC","경부선","default","S"));
        list.add(new Road("기흥동탄IC-기흥IC","경부선","default","S"));
        list.add(new Road("동탄JC-기흥동탄IC","경부선","default","S"));
        list.add(new Road("오산IC-동탄JC","경부선","default","S"));
        list.add(new Road("안성JC-오산IC","경부선","default","S"));
        list.add(new Road("안성IC-안성JC","경부선","default","S"));
        list.add(new Road("북천안IC-안성IC","경부선","default","S"));
        list.add(new Road("천안IC-북천안IC","경부선","default","S"));
        list.add(new Road("천안JC-천안IC","경부선","default","S"));
        list.add(new Road("목천IC-천안JC","경부선","default","S"));
        list.add(new Road("옥산JC-목천IC","경부선","default","S"));
        list.add(new Road("옥산IC-옥산JC","경부선","default","S"));
        list.add(new Road("청주IC-옥산IC","경부선","default","S"));
        list.add(new Road("남이JC-청주IC","경부선","default","S"));
        list.add(new Road("청주JC-남이JC","경부선","default","S"));
        list.add(new Road("남청주IC-청주JC","경부선","default","S"));
        list.add(new Road("신탄진IC-남청주IC","경부선","default","S"));
        list.add(new Road("회덕JC-신탄진IC","경부선","default","S"));
        list.add(new Road("대전IC-회덕JC","경부선","default","S"));
        list.add(new Road("비룡JC-대전IC","경부선","default","S"));
        list.add(new Road("옥천IC-비룡JC","경부선","default","S"));
        list.add(new Road("금강IC-옥천IC","경부선","default","S"));
        list.add(new Road("영동IC-금강IC","경부선","default","S"));
        list.add(new Road("황간IC-영동IC","경부선","default","S"));
        list.add(new Road("추풍령IC-황간IC","경부선","default","S"));
        list.add(new Road("김천IC-추풍령IC","경부선","default","S"));
        list.add(new Road("동김천IC-김천IC","경부선","default","S"));
        list.add(new Road("김천JC-동김천IC","경부선","default","S"));
        list.add(new Road("구미IC-김천JC","경부선","default","S"));
        list.add(new Road("남구미IC-구미IC","경부선","default","S"));
        list.add(new Road("왜관IC-남구미IC","경부선","default","S"));
        list.add(new Road("칠곡물류IC-왜관IC","경부선","default","S"));
        list.add(new Road("금호JC-칠곡물류IC","경부선","default","S"));
        list.add(new Road("도동JC-북대구IC","경부선","default","S"));
        list.add(new Road("동대구JC-도동JC","경부선","default","S"));
        list.add(new Road("경산IC-동대구JC","경부선","default","S"));
        list.add(new Road("영천IC-경산IC","경부선","default","S"));
        list.add(new Road("영천JC-영천IC","경부선","default","S"));
        list.add(new Road("건천IC-영천JC","경부선","default","S"));
        list.add(new Road("경주IC-건천IC","경부선","default","S"));
        list.add(new Road("활천IC-경주IC","경부선","default","S"));
        list.add(new Road("언양JC-활천IC","경부선","default","S"));
        list.add(new Road("서울산IC-언양JC","경부선","default","S"));
        list.add(new Road("통도사IC-서울산IC","경부선","default","S"));
        list.add(new Road(" 통도사 하이패스IC-통도사IC","경부선","default","S"));
        list.add(new Road(" 양산IC-통도사 하이패스IC","경부선","default","S"));
        list.add(new Road("양산JC-양산IC","경부선","default","S"));
        list.add(new Road("노포JC-양산JC","경부선","default","S"));
        list.add(new Road("노포IC-노포JC","경부선","default","S"));
        list.add(new Road("부산요금소-노포IC","경부선","default","S"));
        list.add(new Road("영락IC-부산요금소","경부선","default","S"));
        list.add(new Road("구서IC-영락IC","경부선","default","S"));
        list.add(new Road("청계요금소-판교JC","수도권제1순환선","default","E"));
        list.add(new Road("학의JC-청계요금소","수도권제1순환선","default","E"));
        list.add(new Road("평촌IC-학의JC","수도권제1순환선","default","E"));
        list.add(new Road("산본IC-평촌IC","수도권제1순환선","default","E"));
        list.add(new Road("조남JC-산본IC","수도권제1순환선","default","E"));
        list.add(new Road("도리JC-조남JC","수도권제1순환선","default","E"));
        list.add(new Road("안현JC-도리JC","수도권제1순환선","default","E"));
        list.add(new Road("시흥요금소-안현JC","수도권제1순환선","default","E"));
        list.add(new Road("시흥IC-시흥요금소","수도권제1순환선","default","E"));
        list.add(new Road("장수IC-시흥IC","수도권제1순환선","default","E"));
        list.add(new Road("송내IC-장수IC","수도권제1순환선","default","E"));
        list.add(new Road("중동IC-송내IC","수도권제1순환선","default","E"));
        list.add(new Road("서운JC-중동IC","수도권제1순환선","default","E"));
        list.add(new Road("계양IC-서운JC","수도권제1순환선","default","E"));
        list.add(new Road("노오지JC-계양IC","수도권제1순환선","default","E"));
        list.add(new Road("김포요금소-노오지JC","수도권제1순환선","default","E"));
        list.add(new Road("김포IC-김포요금소","수도권제1순환선","default","E"));
        list.add(new Road("자유로IC-김포IC","수도권제1순환선","default","E"));
        list.add(new Road("일산IC-자유로IC","수도권제1순환선","default","E"));
        list.add(new Road("고양IC-일산IC","수도권제1순환선","default","E"));
        list.add(new Road("통일로IC-고양IC","수도권제1순환선","default","E"));
        list.add(new Road("양주요금소-통일로IC","수도권제1순환선","default","E"));
        list.add(new Road("송추IC-양주요금소","수도권제1순환선","default","E"));
        list.add(new Road("의정부IC-송추IC","수도권제1순환선","default","E"));
        list.add(new Road("별내IC-의정부IC","수도권제1순환선","default","E"));
        list.add(new Road("퇴계원IC-별내IC","수도권제1순환선","default","E"));
        list.add(new Road("구리IC-퇴계원IC","수도권제1순환선","default","E"));
        list.add(new Road("남양주IC-구리IC","수도권제1순환선","default","E"));
        list.add(new Road("구리남양주요금소-남양주IC","수도권제1순환선","default","E"));
        list.add(new Road("토평IC-구리남양주요금소","수도권제1순환선","default","E"));
        list.add(new Road("강밀IC-토평IC","수도권제1순환선","default","E"));
        list.add(new Road("상밀IC-강밀IC","수도권제1순환선","default","E"));
        list.add(new Road("하남JC-상밀IC","수도권제1순환선","default","E"));
        list.add(new Road("서하남IC-하남JC","수도권제1순환선","default","E"));
        list.add(new Road("송파IC-서하남IC","수도권제1순환선","default","E"));
        list.add(new Road("성남요금소-송파IC","수도권제1순환선","default","E"));
        list.add(new Road("성남IC-성남요금소","수도권제1순환선","default","E"));
        list.add(new Road("서해안선종점-금천IC","서해안선","default","E"));
        list.add(new Road("금천IC-일직JC","서해안선","default","E"));
        list.add(new Road("일직JC-광명역IC","서해안선","default","E"));
        list.add(new Road("광명역IC-목감IC","서해안선","default","E"));
        list.add(new Road("목감IC-조남JC","서해안선","default","E"));
        list.add(new Road("조남JC-서서울요금소","서해안선","default","E"));
        list.add(new Road("서서울요금소-안산JC","서해안선","default","E"));
        list.add(new Road("안산JC-팔곡JC","서해안선","default","E"));
        list.add(new Road("팔곡JC-매송IC","서해안선","default","E"));
        list.add(new Road("매송IC-비봉IC ","서해안선","default","E"));
        list.add(new Road("비봉IC -발안IC","서해안선","default","E"));
        list.add(new Road("발안IC-서평택JC","서해안선","default","E"));
        list.add(new Road("서평택JC-서평택IC","서해안선","default","E"));
        list.add(new Road("서평택IC-송악IC","서해안선","default","E"));
        list.add(new Road("송악IC-당진IC","서해안선","default","E"));
        list.add(new Road("당진IC-당진JC","서해안선","default","E"));
        list.add(new Road("당진JC-서산IC","서해안선","default","E"));
        list.add(new Road("서산IC-해미IC","서해안선","default","E"));
        list.add(new Road("해미IC-홍성IC","서해안선","default","E"));
        list.add(new Road("홍성IC-광천IC","서해안선","default","E"));
        list.add(new Road("광천IC-대천IC","서해안선","default","E"));
        list.add(new Road("대천IC-무창포IC","서해안선","default","E"));
        list.add(new Road("무창포IC-춘장대IC","서해안선","default","E"));
        list.add(new Road("춘장대IC-서천IC","서해안선","default","E"));
        list.add(new Road("서천IC-동서천JC","서해안선","default","E"));
        list.add(new Road("동서천JC-군산IC","서해안선","default","E"));
        list.add(new Road("군산IC-동군산IC","서해안선","default","E"));
        list.add(new Road("동군산IC-서김제IC","서해안선","default","E"));
        list.add(new Road("서김제IC-부안IC","서해안선","default","E"));
        list.add(new Road("부안IC-줄포IC","서해안선","default","E"));
        list.add(new Road("줄포IC-선운산IC","서해안선","default","E"));
        list.add(new Road("선운산IC-고창IC","서해안선","default","E"));
        list.add(new Road("고창IC-영광IC","서해안선","default","E"));
        list.add(new Road("영광IC-함평IC","서해안선","default","E"));
        list.add(new Road("함평IC-함평JC","서해안선","default","E"));
        list.add(new Road("함평JC-무안IC","서해안선","default","E"));
        list.add(new Road("무안IC-목포요금소","서해안선","default","E"));
        list.add(new Road("목포요금소-일로IC","서해안선","default","E"));
        list.add(new Road("일로IC-목포IC","서해안선","default","E"));
        list.add(new Road("신월IC-부천IC","경인선","default","S"));
        list.add(new Road("부천IC-서운JC","경인선","default","S"));
        list.add(new Road("서운JC-인천요금소","경인선","default","S"));
        list.add(new Road("인천요금소-부평IC","경인선","default","S"));
        list.add(new Road("부평IC-서인천IC","경인선","default","S"));
        list.add(new Road("서인천IC-가좌IC","경인선","default","S"));
        list.add(new Road("가좌IC-도화IC","경인선","default","S"));
        list.add(new Road("도화IC-경인선기점","경인선","default","S"));
        list.add(new Road("고령JC-동고령IC","광주대구선","default","S"));
        list.add(new Road("동고령IC-고령IC","광주대구선","default","S"));
        list.add(new Road("고령IC-해인사IC","광주대구선","default","S"));
        list.add(new Road("해인사IC-가조IC","광주대구선","default","S"));
        list.add(new Road("가조IC-거창IC","광주대구선","default","S"));
        list.add(new Road("거창IC-함양JC","광주대구선","default","S"));
        list.add(new Road("함양JC-함양IC","광주대구선","default","S"));
        list.add(new Road("함양IC-서함양하이패스IC","광주대구선","default","S"));
        list.add(new Road("서함양하이패스IC-지리산IC","광주대구선","default","S"));
        list.add(new Road("지리산IC-동남원IC","광주대구선","default","S"));
        list.add(new Road("동남원IC-남원IC","광주대구선","default","S"));
        list.add(new Road("남원IC-남원JC","광주대구선","default","S"));
        list.add(new Road("남원JC-순창IC","광주대구선","default","S"));
        list.add(new Road("순창IC-담양IC","광주대구선","default","S"));
        list.add(new Road("담양IC-담양JC","광주대구선","default","S"));
        list.add(new Road("담양JC-고서JC","광주대구선","default","S"));
        list.add(new Road("금천IC-서해안선종점","서해안선","default","S"));
        list.add(new Road("일직JC-금천IC","서해안선","default","S"));
        list.add(new Road("광명역IC-일직JC","서해안선","default","S"));
        list.add(new Road("목감IC-광명역IC","서해안선","default","S"));
        list.add(new Road("조남JC-목감IC","서해안선","default","S"));
        list.add(new Road("서서울요금소-조남JC","서해안선","default","S"));
        list.add(new Road("안산JC-서서울요금소","서해안선","default","S"));
        list.add(new Road("팔곡JC-안산JC","서해안선","default","S"));
        list.add(new Road("매송IC-팔곡JC","서해안선","default","S"));
        list.add(new Road("비봉IC -매송IC","서해안선","default","S"));
        list.add(new Road("발안IC-비봉IC ","서해안선","default","S"));
        list.add(new Road("서평택JC-발안IC","서해안선","default","S"));
        list.add(new Road("서평택IC-서평택JC","서해안선","default","S"));
        list.add(new Road("송악IC-서평택IC","서해안선","default","S"));
        list.add(new Road("당진IC-송악IC","서해안선","default","S"));
        list.add(new Road("당진JC-당진IC","서해안선","default","S"));
        list.add(new Road("서산IC-당진JC","서해안선","default","S"));
        list.add(new Road("해미IC-서산IC","서해안선","default","S"));
        list.add(new Road("홍성IC-해미IC","서해안선","default","S"));
        list.add(new Road("광천IC-홍성IC","서해안선","default","S"));
        list.add(new Road("대천IC-광천IC","서해안선","default","S"));
        list.add(new Road("무창포IC-대천IC","서해안선","default","S"));
        list.add(new Road("춘장대IC-무창포IC","서해안선","default","S"));
        list.add(new Road("서천IC-춘장대IC","서해안선","default","S"));
        list.add(new Road("동서천JC-서천IC","서해안선","default","S"));
        list.add(new Road("군산IC-동서천JC","서해안선","default","S"));
        list.add(new Road("동군산IC-군산IC","서해안선","default","S"));
        list.add(new Road("서김제IC-동군산IC","서해안선","default","S"));
        list.add(new Road("부안IC-서김제IC","서해안선","default","S"));
        list.add(new Road("줄포IC-부안IC","서해안선","default","S"));
        list.add(new Road("선운산IC-줄포IC","서해안선","default","S"));
        list.add(new Road("고창IC-선운산IC","서해안선","default","S"));
        list.add(new Road("영광IC-고창IC","서해안선","default","S"));
        list.add(new Road("함평IC-영광IC","서해안선","default","S"));
        list.add(new Road("함평JC-함평IC","서해안선","default","S"));
        list.add(new Road("무안IC-함평JC","서해안선","default","S"));
        list.add(new Road("목포요금소-무안IC","서해안선","default","S"));
        list.add(new Road("일로IC-목포요금소","서해안선","default","S"));
        list.add(new Road("목포IC-일로IC","서해안선","default","S"));
        list.add(new Road("부천IC-신월IC","경인선","default","E"));
        list.add(new Road("서운JC-부천IC","경인선","default","E"));
        list.add(new Road("인천요금소-서운JC","경인선","default","E"));
        list.add(new Road("부평IC-인천요금소","경인선","default","E"));
        list.add(new Road("서인천IC-부평IC","경인선","default","E"));
        list.add(new Road("가좌IC-서인천IC","경인선","default","E"));
        list.add(new Road("도화IC-가좌IC","경인선","default","E"));
        list.add(new Road("경인선기점-도화IC","경인선","default","E"));
        list.add(new Road("동고령IC-고령JC","광주대구선","default","E"));
        list.add(new Road("고령IC-동고령IC","광주대구선","default","E"));
        list.add(new Road("해인사IC-고령IC","광주대구선","default","E"));
        list.add(new Road("가조IC-해인사IC","광주대구선","default","E"));
        list.add(new Road("거창IC-가조IC","광주대구선","default","E"));
        list.add(new Road("함양JC-거창IC","광주대구선","default","E"));
        list.add(new Road("함양IC-함양JC","광주대구선","default","E"));
        list.add(new Road("서함양하이패스IC-함양IC","광주대구선","default","E"));
        list.add(new Road("지리산IC-서함양하이패스IC","광주대구선","default","E"));
        list.add(new Road("동남원IC-지리산IC","광주대구선","default","E"));
        list.add(new Road("남원IC-동남원IC","광주대구선","default","E"));
        list.add(new Road("남원JC-남원IC","광주대구선","default","E"));
        list.add(new Road("순창IC-남원JC","광주대구선","default","E"));
        list.add(new Road("담양IC-순창IC","광주대구선","default","E"));
        list.add(new Road("담양JC-담양IC","광주대구선","default","E"));
        list.add(new Road("고서JC-담양JC","광주대구선","default","E"));
        list.add(new Road("마산요금소-동마산IC","남해선","default","S"));
        list.add(new Road("동마산IC-서마산IC","남해선","default","S"));
        list.add(new Road("서마산IC-내서JC","남해선","default","S"));
        list.add(new Road("내서JC-산인JC","남해선","default","S"));
        list.add(new Road("서부산IC-가락IC","남해선","default","S"));
        list.add(new Road("가락IC-서부산요금소","남해선","default","S"));
        list.add(new Road("서부산요금소-장유IC","남해선","default","S"));
        list.add(new Road("장유IC-냉정JC","남해선","default","S"));
        list.add(new Road("덕천IC-대저JC","남해선","default","S"));
        list.add(new Road("대저JC-북부산요금소","남해선","default","S"));
        list.add(new Road("북부산요금소-김해JC","남해선","default","S"));
        list.add(new Road("김해JC-동김해IC","남해선","default","S"));
        list.add(new Road("동김해IC-서김해IC","남해선","default","S"));
        list.add(new Road("서김해IC-냉정JC","남해선","default","S"));
        list.add(new Road("냉정JC-진례JC","남해선","default","S"));
        list.add(new Road("진례JC-진례IC","남해선","default","S"));
        list.add(new Road("진례IC-진영JC","남해선","default","S"));
        list.add(new Road("진영JC-동창원IC","남해선","default","S"));
        list.add(new Road("동창원IC-창원JC","남해선","default","S"));
        list.add(new Road("창원JC-북창원IC","남해선","default","S"));
        list.add(new Road("북창원IC-창원1터널(동측)","남해선","default","S"));
        list.add(new Road("창원1터널(동측)-창원1터널(서측)","남해선","default","S"));
        list.add(new Road("창원1터널(서측)-칠원JC","남해선","default","S"));
        list.add(new Road("칠원JC-산인JC","남해선","default","S"));
        list.add(new Road("산인JC-함안IC","남해선","default","S"));
        list.add(new Road("함안IC-장지IC","남해선","default","S"));
        list.add(new Road("장지IC-군북IC","남해선","default","S"));
        list.add(new Road("군북IC-지수IC","남해선","default","S"));
        list.add(new Road("지수IC-진성IC","남해선","default","S"));
        list.add(new Road("진성IC-문산IC","남해선","default","S"));
        list.add(new Road("문산IC-진주IC","남해선","default","S"));
        list.add(new Road("진주IC-진주JC","남해선","default","S"));
        list.add(new Road("진주JC-사천IC","남해선","default","S"));
        list.add(new Road("사천IC-축동IC","남해선","default","S"));
        list.add(new Road("축동IC-곤양IC","남해선","default","S"));
        list.add(new Road("곤양IC-진교IC","남해선","default","S"));
        list.add(new Road("진교IC-하동IC","남해선","default","S"));
        list.add(new Road("하동IC-진월IC","남해선","default","S"));
        list.add(new Road("진월IC-옥곡IC","남해선","default","S"));
        list.add(new Road("옥곡IC-동광양IC","남해선","default","S"));
        list.add(new Road("동광양IC-광양IC","남해선","default","S"));
        list.add(new Road("광양IC-순천JC","남해선","default","S"));
        list.add(new Road("순천JC-순천IC","남해선","default","S"));
        list.add(new Road("순천IC-서춘선IC","남해선","default","S"));
        list.add(new Road("동마산IC-마산요금소","남해선","default","E"));
        list.add(new Road("서마산IC-동마산IC","남해선","default","E"));
        list.add(new Road("내서JC-서마산IC","남해선","default","E"));
        list.add(new Road("산인JC-내서JC","남해선","default","E"));
        list.add(new Road("가락IC-서부산IC","남해선","default","E"));
        list.add(new Road("서부산요금소-가락IC","남해선","default","E"));
        list.add(new Road("장유IC-서부산요금소","남해선","default","E"));
        list.add(new Road("냉정JC-장유IC","남해선","default","E"));
        list.add(new Road("대저JC-덕천IC","남해선","default","E"));
        list.add(new Road("북부산요금소-대저JC","남해선","default","E"));
        list.add(new Road("김해JC-북부산요금소","남해선","default","E"));
        list.add(new Road("동김해IC-김해JC","남해선","default","E"));
        list.add(new Road("서김해IC-동김해IC","남해선","default","E"));
        list.add(new Road("냉정JC-서김해IC","남해선","default","E"));
        list.add(new Road("진례JC-냉정JC","남해선","default","E"));
        list.add(new Road("진례IC-진례JC","남해선","default","E"));
        list.add(new Road("진영JC-진례IC","남해선","default","E"));
        list.add(new Road("동창원IC-진영JC","남해선","default","E"));
        list.add(new Road("창원JC-동창원IC","남해선","default","E"));
        list.add(new Road("북창원IC-창원JC","남해선","default","E"));
        list.add(new Road("창원1터널(동측)-북창원IC","남해선","default","E"));
        list.add(new Road("창원1터널(서측)-창원1터널(동측)","남해선","default","E"));
        list.add(new Road("칠원JC-창원1터널(서측)","남해선","default","E"));
        list.add(new Road("산인JC-칠원JC","남해선","default","E"));
        list.add(new Road("함안IC-산인JC","남해선","default","E"));
        list.add(new Road("장지IC-함안IC","남해선","default","E"));
        list.add(new Road("군북IC-장지IC","남해선","default","E"));
        list.add(new Road("지수IC-군북IC","남해선","default","E"));
        list.add(new Road("진성IC-지수IC","남해선","default","E"));
        list.add(new Road("문산IC-진성IC","남해선","default","E"));
        list.add(new Road("진주IC-문산IC","남해선","default","E"));
        list.add(new Road("진주JC-진주IC","남해선","default","E"));
        list.add(new Road("사천IC-진주JC","남해선","default","E"));
        list.add(new Road("축동IC-사천IC","남해선","default","E"));
        list.add(new Road("곤양IC-축동IC","남해선","default","E"));
        list.add(new Road("진교IC-곤양IC","남해선","default","E"));
        list.add(new Road("하동IC-진교IC","남해선","default","E"));
        list.add(new Road("진월IC-하동IC","남해선","default","E"));
        list.add(new Road("옥곡IC-진월IC","남해선","default","E"));
        list.add(new Road("동광양IC-옥곡IC","남해선","default","E"));
        list.add(new Road("광양IC-동광양IC","남해선","default","E"));
        list.add(new Road("순천JC-광양IC","남해선","default","E"));
        list.add(new Road("순천IC-순천JC","남해선","default","E"));
        list.add(new Road("서춘선IC-순천IC","남해선","default","E"));
        list.add(new Road("하남JC-하남IC","중부선","default","E"));
        list.add(new Road("하남IC-동서울요금소","중부선","default","E"));
        list.add(new Road("동서울요금소-산곡JC","중부선","default","E"));
        list.add(new Road("산곡JC-경기광주IC","중부선","default","E"));
        list.add(new Road("경기광주IC-경기광주JC","중부선","default","E"));
        list.add(new Road("경기광주JC-곤지암IC","중부선","default","E"));
        list.add(new Road("곤지암IC-서이천IC","중부선","default","E"));
        list.add(new Road("서이천IC-마장JC","중부선","default","E"));
        list.add(new Road("마장JC-호법JC","중부선","default","E"));
        list.add(new Road("호법JC-남이천IC","중부선","default","E"));
        list.add(new Road("남이천IC-일죽IC","중부선","default","E"));
        list.add(new Road("일죽IC-대소IC","중부선","default","E"));
        list.add(new Road("대소IC-대소JC","중부선","default","E"));
        list.add(new Road("대소JC-진천IC","중부선","default","E"));
        list.add(new Road("진천IC-증평IC","중부선","default","E"));
        list.add(new Road("증평IC-오창JC","중부선","default","E"));
        list.add(new Road("오창JC-오창IC","중부선","default","E"));
        list.add(new Road("대덕JC-담양JC","고창담양선","default","S"));
        list.add(new Road("담양JC-북광주IC","고창담양선","default","S"));
        list.add(new Road("북광주IC-장성 JC","고창담양선","default","S"));
        list.add(new Road("장성 JC-장성물류IC","고창담양선","default","S"));
        list.add(new Road("장성물류IC-남고창IC","고창담양선","default","S"));
        list.add(new Road("하남IC-하남JC","중부선","default","S"));
        list.add(new Road("동서울요금소-하남IC","중부선","default","S"));
        list.add(new Road("산곡JC-동서울요금소","중부선","default","S"));
        list.add(new Road("경기광주IC-산곡JC","중부선","default","S"));
        list.add(new Road("경기광주JC-경기광주IC","중부선","default","S"));
        list.add(new Road("곤지암IC-경기광주JC","중부선","default","S"));
        list.add(new Road("서이천IC-곤지암IC","중부선","default","S"));
        list.add(new Road("마장JC-서이천IC","중부선","default","S"));
        list.add(new Road("호법JC-마장JC","중부선","default","S"));
        list.add(new Road("남이천IC-호법JC","중부선","default","S"));
        list.add(new Road("일죽IC-남이천IC","중부선","default","S"));
        list.add(new Road("대소IC-일죽IC","중부선","default","S"));
        list.add(new Road("대소JC-대소IC","중부선","default","S"));
        list.add(new Road("진천IC-대소JC","중부선","default","S"));
        list.add(new Road("증평IC-진천IC","중부선","default","S"));
        list.add(new Road("오창JC-증평IC","중부선","default","S"));
        list.add(new Road("오창IC-오창JC","중부선","default","S"));
        list.add(new Road("담양JC-대덕JC","고창담양선","default","E"));
        list.add(new Road("북광주IC-담양JC","고창담양선","default","E"));
        list.add(new Road("장성 JC-북광주IC","고창담양선","default","E"));
        list.add(new Road("장성물류IC-장성 JC","고창담양선","default","E"));
        list.add(new Road("남고창IC-장성물류IC","고창담양선","default","E"));
        list.add(new Road("산내JC-안영IC","대전남부선","default","S"));
        list.add(new Road("안영IC-서대전IC","대전남부선","default","S"));
        list.add(new Road("남포항IC-동경주IC","동해선(부산-포항)","default","S"));
        list.add(new Road("동경주IC-남경주IC","동해선(부산-포항)","default","S"));
        list.add(new Road("남경주IC-범서IC","동해선(부산-포항)","default","S"));
        list.add(new Road("속초IC-북양양IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("북양양IC-양양IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("양양IC-하조대IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("하조대IC-남양양IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("남양양IC-북강릉IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("북강릉IC-강릉JC","동해선(삼척-속초)","default","S"));
        list.add(new Road("강릉JC-강릉IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("강릉IC-남강릉IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("남강릉IC-옥계IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("옥계IC-망상IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("망상IC-동해IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("동해IC-삼척IC","동해선(삼척-속초)","default","S"));
        list.add(new Road("동광산요금소-서광산IC","무안광주선","default","S"));
        list.add(new Road("서광산IC-나주IC","무안광주선","default","S"));
        list.add(new Road("나주IC-문평IC","무안광주선","default","S"));
        list.add(new Road("문평IC-동함평IC","무안광주선","default","S"));
        list.add(new Road("동함평IC-함평JC","무안광주선","default","S"));
        list.add(new Road("함평JC-북무안IC","무안광주선","default","S"));
        list.add(new Road("북무안IC-무안공항IC","무안광주선","default","S"));
        list.add(new Road("안영IC-산내JC","대전남부선","default","E"));
        list.add(new Road("서대전IC-안영IC","대전남부선","default","E"));
        list.add(new Road("동경주IC-남포항IC","동해선(부산-포항)","default","E"));
        list.add(new Road("남경주IC-동경주IC","동해선(부산-포항)","default","E"));
        list.add(new Road("범서IC-남경주IC","동해선(부산-포항)","default","E"));
        list.add(new Road("북양양IC-속초IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("양양IC-북양양IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("하조대IC-양양IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("남양양IC-하조대IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("북강릉IC-남양양IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("강릉JC-북강릉IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("강릉IC-강릉JC","동해선(삼척-속초)","default","E"));
        list.add(new Road("남강릉IC-강릉IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("옥계IC-남강릉IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("망상IC-옥계IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("동해IC-망상IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("삼척IC-동해IC","동해선(삼척-속초)","default","E"));
        list.add(new Road("서광산IC-동광산요금소","무안광주선","default","E"));
        list.add(new Road("나주IC-서광산IC","무안광주선","default","E"));
        list.add(new Road("문평IC-나주IC","무안광주선","default","E"));
        list.add(new Road("동함평IC-문평IC","무안광주선","default","E"));
        list.add(new Road("함평JC-동함평IC","무안광주선","default","E"));
        list.add(new Road("북무안IC-함평JC","무안광주선","default","E"));
        list.add(new Road("무안공항IC-북무안IC","무안광주선","default","E"));
        list.add(new Road("기장JC-기장철마IC","부산외곽선","default","S"));
        list.add(new Road("기장철마IC-금정IC","부산외곽선","default","S"));
        list.add(new Road("금정IC-노포JC","부산외곽선","default","S"));
        list.add(new Road("노포JC-김해가야Hi","부산외곽선","default","S"));
        list.add(new Road("김해가야Hi-대감JC","부산외곽선","default","S"));
        list.add(new Road("대감JC-광재IC","부산외곽선","default","S"));
        list.add(new Road("광재IC-한림IC","부산외곽선","default","S"));
        list.add(new Road("한림IC-진영IC","부산외곽선","default","S"));
        list.add(new Road("기장철마IC-기장JC","부산외곽선","default","E"));
        list.add(new Road("금정IC-기장철마IC","부산외곽선","default","E"));
        list.add(new Road("노포JC-금정IC","부산외곽선","default","E"));
        list.add(new Road("김해가야Hi-노포JC","부산외곽선","default","E"));
        list.add(new Road("대감JC-김해가야Hi","부산외곽선","default","E"));
        list.add(new Road("광재IC-대감JC","부산외곽선","default","E"));
        list.add(new Road("한림IC-광재IC","부산외곽선","default","E"));
        list.add(new Road("진영IC-한림IC","부산외곽선","default","E"));
        
        for(int i = 0; i < list.size();i++){
            if(list.get(i).getRouteName().equals("영동선") && list.get(i).getUpdownTypeCode().equals("S")){
                data_s.add(list.get(i));
            }
        }
    }
    public void change_data() {
        if(result_data.size() != 0) {
            for (int i = 0; i < result_data.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    //막힌 경로가 똑같이 있다면?
                    if (result_data.get(i).getConzoneName().equals(list.get(j).getConzoneName())) {
                        list.get(j).setGrade(result_data.get(i).getGrade());
                        Log.d("124",Integer.toString(i));
                    }
                }
            }
        }
    }
    public void refresh(){
        /*레트로핏을 이용하여 데이터 가져오기*/
        //정보를 가지는 리스트를 초기화!
        GetDataHighway service1 = Retrofit_Highway.getInstance().create(GetDataHighway.class);
        Call<LoadDataHighway> call = service1.getInstanceHighway(key, "json");
        //레트로핏 비동기 실행
        call.enqueue(new Callback<LoadDataHighway>() {
            @Override
            public void onResponse(Call<LoadDataHighway> call, Response<LoadDataHighway> response) {
                if(response.isSuccessful()){
                    result = (LoadDataHighway)response.body();
                    if(result.getDhh().size() != 0) {
                        for (int i = 0; i < result.getDhh().size(); i++) {
                            result_data.add(
                                    new DataHighway(result.getDhh().get(i).getStdHour(),
                                            result.getDhh().get(i).getRouteNo(),
                                            result.getDhh().get(i).getRouteName(),
                                            result.getDhh().get(i).getUpdownTypeCode(),
                                            result.getDhh().get(i).getVdsId(),
                                            result.getDhh().get(i).getTrafficAmout(),
                                            result.getDhh().get(i).getShareRatio(),
                                            result.getDhh().get(i).getConzoneId(),
                                            result.getDhh().get(i).getConzoneName(),
                                            result.getDhh().get(i).getStdDate(),
                                            result.getDhh().get(i).getSpeed(),
                                            result.getDhh().get(i).getTimeAvg(),
                                            result.getDhh().get(i).getGrade()));
                        }
                        OnSuccess(result_data);
                    }
                }
                else{
                    Log.v("결과는 : ","실패");
                }

            }
            @Override
            public void onFailure(Call<LoadDataHighway> call, Throwable t) {
                Log.d("결과는 : ","아에 연결도 안돼!");
            }

        });

    }
    public void OnSuccess(List<DataHighway> result_data) {
        change_data();
    }
}
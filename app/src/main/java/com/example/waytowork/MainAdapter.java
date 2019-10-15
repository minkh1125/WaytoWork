package com.example.waytowork;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private List<MainData> items = null;// 굳이 null..?  , holder에 있는걸 담는 곳
    private ArrayList<MainData> arrayList;//  검색 filter 메소드를 필터릴 하기 위해 만든 곳
    private LayoutInflater mInflate; // 추가 레이아웃 붙여서 읽어들일려고
    private Context mContext; // 추가  어플자기자신
    Intent i;

    public MainAdapter(Context context, List<MainData> items) {
        this.items = items;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
        arrayList = new ArrayList<MainData>();//굳이 MainData..?
        arrayList.addAll(items);
    }

    @Override
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//액티비티의 온크에이트랑 비슷한대 리스트뷰가 처음으로 생성될때 생성주기
        View view = mInflate.inflate(R.layout.item_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.CustomViewHolder holder, int position) {
        //실제 추가될때의 생명주기

        holder.tv_start.setText(items.get(position).getTv_start_po());
        holder.tv_end.setText(items.get(position).getTv_end_po());
        holder.tv_content.setText(items.get(position).getTv_content());

        if (items.get(position).getIv_item_kat().equals("문서")) {
            holder.iv_kat.setImageResource(R.drawable.ic_marker);
        } else if (items.get(position).getIv_item_kat().equals("음식")) {
            holder.iv_kat.setImageResource(R.drawable.ic_start);
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = holder.tv_content.getText().toString();
                i = new Intent(v.getContext(), Pop.class);
                i.putExtra("content", content);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // extends에 액티비티가 없기때문에 이 문구를 써줘야 액티비티가 넘어간다
                mContext.startActivity(i);  //여기부분도 딱히 필요없어질듯
            }
        });
    }

    //검색기능 구현부
    public void filterList(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        items.clear();
        if (charText.length() == 0) {
            items.addAll(arrayList);
        } else {
            for (MainData mainData : arrayList) {
                String title = mainData.getTv_start_po();
                if (title.toLowerCase().contains(charText)) {
                    items.add(mainData);
                    //Log.e("title", title);
                }
            }
        }
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        //return 0;
        return (null != items ? items.size() : 0);

    }


//위에 롱클릭 했을때 삭제 하려고 만들어 준건데 딱히 쓸모 없어서 일단 주석
//    public void remove(int position){
//        try {
//            arrayList.remove(position);
//            notifyItemRemoved(position); //새로고침이라는뜻 윗줄에list뷰가 한걸 새로고침
//        }catch (IndexOutOfBoundsException ex){
//            ex.printStackTrace();
//        }
//    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_kat;
        protected TextView tv_start;
        protected TextView tv_end;
        protected TextView tv_content;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_kat = (ImageView) itemView.findViewById(R.id.iv_kat);//find뷰랑 비슷함쓰는이유는 액티비티 형태의 클래스가 아니기 때문에
            this.tv_start = (TextView) itemView.findViewById(R.id.tv_start);
            this.tv_end = (TextView) itemView.findViewById(R.id.tv_end);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}

package com.example.kasun.busysms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.kasun.busysms.autoSms.autoSmsHome;

import java.util.ArrayList;
import java.util.HashMap;

public class home extends AppCompatActivity {

    ListView simpleListView;
    String[] animalName={"Lion","Tiger","Monkey","Dog","Cat","Elephant"};//animal names array
    int[] animalImages={
            R.drawable.function1,
            R.drawable.function2,
            R.drawable.function3,
            R.drawable.function4};//animal images array

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        simpleListView=(ListView)findViewById(R.id.homeListView);

        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
        for (int i=0;i<4;i++)
        {
            HashMap<String,String> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put("image",animalImages[i]+"");
            arrayList.add(hashMap);//add the hashmap into arrayList
        }
        String[] from={"image"};//string array
        int[] to={R.id.homeImage};//int array of views id's
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,arrayList,R.layout.home_item_list,from,to);//Create object and set the parameters for simpleAdapter
        simpleListView.setAdapter(simpleAdapter);//sets the adapter for listView


        //perform listView item click event
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0){
                    Intent intent = new Intent(home.this,autoSmsHome.class);
                    startActivity(intent);
                }else if(position ==1){
                    Intent intent = new Intent(home.this,alarmHome.class);
                    startActivity(intent);
                }else if(position ==2){
                    Intent intent = new Intent(home.this,blockerHome.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(home.this,calendarHome.class);
                    startActivity(intent);
                }//show the selected image in toast according to position
            }
        });

      /*  GridView gridView=(GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    Intent intent = new Intent(home.this,autoSmsHome.class);
                    startActivity(intent);
                }else if(position ==1){
                    Intent intent = new Intent(home.this,alarmHome.class);
                    startActivity(intent);
                }else if(position ==2){
                    Intent intent = new Intent(home.this,blockerHome.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(home.this,calendarHome.class);
                    startActivity(intent);
                }
            }
        });


*/
    }


/*
    public class ImageAdapter extends BaseAdapter{
        private Context mContext;

        public  ImageAdapter(Context c){
            mContext=c;
        }

        public int getCount(){
            return mImages.length;
        }

        public  Object getItem(int position){
            return null;
        }
        public long getItemId(int position){
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            ImageView imageView =new ImageView(mContext);
            imageView.setImageResource(mImages[position]);
            return  imageView;
        }

        private Integer[] mImages={
                R.drawable.function1,
                R.drawable.function2,
                R.drawable.function3,
                R.drawable.function4
        };
    }
*/

}

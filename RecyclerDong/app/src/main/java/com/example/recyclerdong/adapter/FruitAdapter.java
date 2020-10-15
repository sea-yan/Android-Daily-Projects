package com.example.recyclerdong.adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerdong.Fruit;
import com.example.recyclerdong.MainActivity;
import com.example.recyclerdong.R;
import com.example.recyclerdong.base.dbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<Fruit> mFruitList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;//声明一下这个接口
    private SQLiteDatabase db;
    private Map<String, Object> item;
    private Cursor cursor;
    private ArrayList<Map<String, Object>> data;
    private dbHelper dbHelper;
    private TextView textView1;
    private TextView textView2;
    private ListView listview;
    private SimpleAdapter listAdapter;
    private static String DB_NAME = "mydb";
//    MainActivity mainActivity;

    public FruitAdapter(List<Fruit> fruitList , Context context , OnItemClickListener onItemClickListener) {
        this.mFruitList = fruitList;
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fruitImage;
        TextView fruitName;
        Button insert;
        Button select;
        public ViewHolder(View view) {
            super(view);
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruitname);
            insert = view.findViewById(R.id.i_button);
            select = view.findViewById(R.id.s_button);

        }

    }

    //用于创建ViewHolder实例,并把加载的布局传入到构造函数去,再把ViewHolder实例返回。
    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //onBindViewHolder()则是用于对子项的数据进行赋值,会在每个子项被滚动到屏幕内时执行。position得到当前项的Fruit实例。
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        dbHelper = new dbHelper(mContext, DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库
        data = new ArrayList<>();
        final Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(position);
                Toast.makeText(mContext, "点击图片 " + position, Toast.LENGTH_SHORT).show();
            }
        });

        holder.fruitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(position);
                Toast.makeText(mContext, "点击文本框 " + holder.fruitName.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.insert.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mOnItemClickListener.onClick(position);
                Toast.makeText(mContext, "添加 " + position, Toast.LENGTH_SHORT).show();

                ContentValues values = new ContentValues();
                values.put("name", fruit.getName());
                long rowid = db.insert(dbHelper.TB_NAME, null, values);
                if (rowid == -1)
                    Log.i("myDbDemo", "数据添加失败！");
                else
                    Log.i("myDbDemo", "数据添加成功!" + rowid);
            }
        });

        holder.select.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mOnItemClickListener.onClick(position);
                Toast.makeText(mContext, "查看 " + position, Toast.LENGTH_SHORT).show();

                data.clear();
                cursor = db.query(dbHelper.TB_NAME, null, null, null, null, null, "_id ASC");
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(0);
                    String name = cursor.getString(1);
                    item = new HashMap<String, Object>();
                    item.put("id", id);
                    item.put("name", name);
                    data.add(item);
                    cursor.moveToNext();
                }
                showLook().show();
            }
        });
    }

    //getItemCount()返回RecyclerView的子项数目。
    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    public Dialog showLook() {
        final Dialog mLook = new Dialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.look,null);
        mLook.setContentView(view);
        listview = mLook.findViewById (R.id.listView);
        showList();
        return mLook;
    }


    // 着重看一下这个方法
    private void showList() {
        // TODO Auto-generated method stub
        listAdapter = new SimpleAdapter(mContext, data,
                R.layout.look, new String[]{"id", "name"}, new int[] {R.id.ID, R.id.Name});
        listview.setAdapter(listAdapter);
    }

    // 外部接口回调监听
    public interface OnItemClickListener {
        void onClick(int pos);
    }
}

package com.dwh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwh.sqlitedemo.R;
import com.dwh.*;

import java.util.ArrayList;

public class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.ViewHolder> {

        List<UserInfo> userInfos = new ArrayList<>();

public RecyAdapter(List<UserInfo> users){
        this.userInfos = users;
        }

/**
 * 加载xml布局小文件
 * @param viewGroup
 * @param i
 * @return
 */
@Override
public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sel_item, viewGroup, false);
        return new ViewHolder(view);
        }

/**
 * 绑定数据
 * @param viewHolder
 * @param i
 */
@Override
public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txt_item_user_id.setText(String.valueOf(userInfos.get(i).getUserid()));
        viewHolder.txt_item_username.setText(userInfos.get(i).getUsername());
        viewHolder.txt_item_password.setText(userInfos.get(i).getPassword());
        }


/**
 * 绑定数据
 * @param viewHolder
 * @param i
 */


/**
 * 加载小文件的个数
 * 来个6
 * @return
 */
@Override
public int getItemCount() {
        if(userInfos == null){
        return 0;
        }
        return userInfos.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {

    LinearLayout lin_manager;

    TextView txt_item_user_id, txt_item_username, txt_item_password, txt_item_age, txt_item_address;

    public ViewHolder(final View view) {
        super(view);
        lin_manager = view.findViewById(R.id.lin_manager);
        txt_item_user_id = view.findViewById(R.id.txt_item_user_id);
        txt_item_username = view.findViewById(R.id.txt_item_username);
        txt_item_password = view.findViewById(R.id.txt_item_password);
        txt_item_age = view.findViewById(R.id.txt_item_age);
        txt_item_address = view.findViewById(R.id.txt_item_address);

        txt_item_user_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linster.textItemOnClick(v, getPosition());
            }
        });
        lin_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linster.textItemOnClick(v, getPosition());
            }
        });
        lin_manager.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LongLinster.textItemOnLongClick(v, getPosition());
                return false;
            }
        });
    }
}

    public ItemOnClickLinster Linster;

    public void setLinster(ItemOnClickLinster linster) {
        Linster = linster;
    }

public interface ItemOnClickLinster{
    void textItemOnClick(View view, int position);
}


    public ItemOnLongClickLinster LongLinster;

    public void setlongLinster(ItemOnLongClickLinster longLinster) {
        LongLinster = longLinster;
    }

public interface ItemOnLongClickLinster{
    void textItemOnLongClick(View view, int position);
}

}

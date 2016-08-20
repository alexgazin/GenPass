package com.pr0jector.genpass;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pr0jector on 11.08.2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemHolder> {

    private List<String> itemsName;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    private static Context sContext;

  //  public RecyclerViewAdapter(Context context){
  //      layoutInflater = LayoutInflater.from(context);
  //      itemsName = new ArrayList<String>();
  //  }

    // Adapter's Constructor
    public RecyclerViewAdapter(Context context, ArrayList<String> myDataset) {
        layoutInflater = LayoutInflater.from(context);
        itemsName = myDataset;
        sContext = context;
    }

    @Override
    public RecyclerViewAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.layout_item, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ItemHolder holder, int position) {
        holder.setItemName(itemsName.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsName.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener(){
        return onItemClickListener;
    }


    public interface OnItemClickListener{
        public void onItemClick(ItemHolder item, int position);
    }

    public void addAll(List<String> list) {
        itemsName.clear();
        itemsName.addAll(list);
        notifyItemRangeInserted(0, itemsName.size());
    }

    public List<String> getDataSet() { // дай мне дату из адаптера
      return itemsName;
    }

    public void remove(int location){
        if(location >= itemsName.size())
            return;

        itemsName.remove(location);
        notifyItemRemoved(location);
    }


    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RecyclerViewAdapter parent;
        TextView outputPassword;



        public ItemHolder(View itemView, RecyclerViewAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.parent = parent;
            outputPassword = (TextView) itemView.findViewById(R.id.textView2);
        }


        public void setItemName(CharSequence name){
            outputPassword.setText(name);
        }

        public CharSequence getItemName(){
            return outputPassword.getText();
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if(listener != null){
                listener.onItemClick(this, getPosition());
            }
        }
    }
}

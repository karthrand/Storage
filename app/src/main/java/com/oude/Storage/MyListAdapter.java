package com.oude.Storage;
import android.support.v7.widget.*;
import java.util.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>
{
    private List<MyList> mMyList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View MyListView;
        ImageView MyListImage;
        TextView MyListName;

        public ViewHolder(View view) {
            super(view);
            MyListView = view;
            MyListImage = (ImageView) view.findViewById(R.id.list_image);
            MyListName = (TextView) view.findViewById(R.id.list_name);
        }
    }

    public MyListAdapter(List<MyList> MyList) {
        mMyList = MyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mylist, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.MyListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    MyList MyList = mMyList.get(position);
                    switch(position){
                        case 0:
                            Intent intent = new Intent(v.getContext(),FileActivity.class);
                            v.getContext().startActivity(intent);

                            break;
                        default:
                            Toast.makeText(v.getContext(), "you clicked view " + MyList.getName(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        holder.MyListImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    MyList MyList = mMyList.get(position);
                    Toast.makeText(v.getContext(), "you clicked image " + MyList.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyList MyList = mMyList.get(position);
        holder.MyListImage.setImageResource(MyList.getImageId());
        holder.MyListName.setText(MyList.getName());
    }

    @Override
    public int getItemCount() {
        return mMyList.size();
    }


}

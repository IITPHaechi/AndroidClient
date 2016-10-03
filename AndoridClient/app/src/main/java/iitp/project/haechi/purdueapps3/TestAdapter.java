package iitp.project.haechi.purdueapps3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dnay2 on 2016-10-02.
 */
public class TestAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> items;

    public TestAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        if(items != null) return items.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView textView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.setup_listview, null);
            holder.textView = (TextView) view.findViewById(R.id.setup);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String item = items.get(i);
        holder.textView.setText(item);

        return view;
    }
}

package com.matias.core;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

/*
 * Created by BenTorres on 01/11/2015.
 */
class CheckLog_BaseAdapter extends BaseAdapter {

    private List<CheckLog> checklog_list;
    private LayoutInflater inflater;

    CheckLog_BaseAdapter(List<CheckLog> checklog_list, LayoutInflater inflater){
        super();
        this.checklog_list = checklog_list;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return checklog_list.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < checklog_list.size()) {
            return checklog_list.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.check_log, parent, false);

        TextView tv_hours = (TextView) convertView.findViewById(R.id.tv_log);
        ImageView iv_log_action = (ImageView) convertView.findViewById(R.id.iv_log_action);

        if(checklog_list.get(position).isCheck_in()){
            iv_log_action.setImageResource(R.drawable.ic_action_forward);
            tv_hours.setTextColor(My_Colors.PRIMARY_COLOR);
        }
        else{
            iv_log_action.setImageResource(R.drawable.ic_action_back);
        }

        String hours_minutes = String.format(Locale.getDefault(), "%02d:%02d %s",
            checklog_list.get(position).getHours_12format(),
            checklog_list.get(position).getMinutes(),
            checklog_list.get(position).get_am_pm_string());

        tv_hours.setText(hours_minutes);

        return convertView;
    }
}

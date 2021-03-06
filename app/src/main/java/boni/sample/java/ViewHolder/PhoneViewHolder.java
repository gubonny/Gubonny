package boni.sample.java.ViewHolder;

import android.view.View;
import android.widget.TextView;

import boni.sample.java.VO.VO_Phone;
import boni.sample.java.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by gbkim on 2018-01-11.
 */

public class PhoneViewHolder extends ChildViewHolder {

    private TextView phoneName;

    public PhoneViewHolder(View itemView) {
        super(itemView);

        phoneName = itemView.findViewById(R.id.phone_name);
    }

    public void onBind(VO_Phone phone, ExpandableGroup group) {

        phoneName.setText(phone.getName());
    }
}

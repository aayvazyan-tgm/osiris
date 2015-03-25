package at.pria.osiris.osiris.view.elements.naviBar;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import at.pria.osiris.osiris.R;

/**
 * Created by helmuthbrunner on 25/03/15.
 */

/**
 * A custom Adapter.
 * source: http://www.tutecentral.com/android-custom-navigation-drawer/
 */
public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    private Context context;
    private List<DrawerItem> drawerItemList;
    private int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID, int onemore, List<DrawerItem> listItems) {
        super(context, layoutResourceID, onemore, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            TextView textView= (TextView) view.findViewById(R.id.drawer_itemName);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(22f);
            drawerHolder.ItemName= textView;
            drawerHolder.icon= (ImageView) view.findViewById(R.id.drawer_icon);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }

        DrawerItem dItem= this.drawerItemList.get(position);

        drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
        drawerHolder.ItemName.setText(dItem.getItemName());

        return view;
    }

    private static class DrawerItemHolder {
        TextView ItemName;
        ImageView icon;
    }
}
package mindmade.akshayaproducts.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mindmade.akshayaproducts.R;

/**
 * Created by suganya on 12/13/2016.
 */
public class Shop_list_Adapter extends BaseAdapter {
    Context context;
    ArrayList<HashMap<String, String>> shoplist;
   // private final String[] web;
  //  private final String[] address;
    public LayoutInflater inflater;
    static class ViewHolder {
        TextView text,textadd,textid;
       // ImageView img;
    }
  //  private final Integer[] imageid;
    public Shop_list_Adapter(Context con,   ArrayList shoplist) {

        context=con;
       // this.web=web;
       // this.imageid=imageid;
       // this.address=address;
        this.shoplist=shoplist;
        inflater=LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return shoplist.size();
    }

    @Override
    public Object getItem(int position) {
        return shoplist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
        convertView= inflater.inflate(R.layout.shop_list_content,null,false);
        holder = new ViewHolder();
        holder.textadd=(TextView)convertView.findViewById(R.id.text1);
        holder.text=(TextView)convertView.findViewById(R.id.text);
        holder.textid=(TextView)convertView.findViewById(R.id.text2);

        convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        HashMap<String,String> hashMap= (HashMap<String, String>)shoplist.get(position);
        String id=hashMap.get("store_id");
        holder.textid.setText(id);
        holder.text.setText(hashMap.get("store_name"));
        holder.textadd.setText(hashMap.get("address"));
        Log.d("success1","default1"+ hashMap.get("address"));
        Log.d("success2","default2"+ hashMap.get("store_name"));
        Log.d("success3","default3"+ hashMap.get("store_id"));
        return convertView;
    }
}

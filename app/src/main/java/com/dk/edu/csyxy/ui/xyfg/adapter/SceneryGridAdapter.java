package com.dk.edu.csyxy.ui.xyfg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.ui.xyfg.entity.SceneryEntity;

import java.util.ArrayList;
import java.util.List;

public class SceneryGridAdapter extends BaseAdapter{
	private Context context;
	private List<SceneryEntity> list=new ArrayList<SceneryEntity>();
	private LayoutInflater lif;
	
	public List<SceneryEntity> getList() {
		return list;
	}

	public void setList(List<SceneryEntity> list) {
		this.list = list;
	}
	
	private class MyView {
		private ImageView image;
	}
	
	public SceneryGridAdapter(Context context, List<SceneryEntity> list) {
		this.context = context;
		this.list = list;
	}
	
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyView mv;
		if (convertView == null) {
			mv = new MyView();
			lif = LayoutInflater.from(context);// 转化到context这个容器
			convertView = lif.inflate(R.layout.app_scenery_listview_item, null);// 设置要转化的layout文件
			mv.image = (ImageView) convertView.findViewById(R.id.scenerygridimage);// 取得实例
			convertView.setTag(mv);
		} else {
			mv = (MyView) convertView.getTag();
		}
		
//		mv.image.setImageURI(Uri.parse(list.get(position).getThumb()));
		Glide.with(context).load(list.get(position).getThumb()).placeholder(R.color.transparent).into(mv.image);
		
		return convertView;
	}


}
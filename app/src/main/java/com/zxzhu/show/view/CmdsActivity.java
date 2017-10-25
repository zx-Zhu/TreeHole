package com.zxzhu.show.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.zxzhu.show.R;
import com.zxzhu.show.adapters.MyRecyclerAdapter;
import com.zxzhu.show.databinding.ActivityCmdsBinding;
import com.zxzhu.show.databinding.ItemCmdBinding;
import com.zxzhu.show.units.base.BaseActivity;

import java.util.List;

public class CmdsActivity extends BaseActivity {
    private ActivityCmdsBinding binding;
    private List<AVObject> list;
    @Override
    protected void initData() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cmds);
        list = (List<AVObject>) getIntent().getSerializableExtra("list");
        TextView title = $(R.id.header_title);
        title.setText("树洞推荐");
        ImageView back = $(R.id.back_header);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Log.d("zhuzhanxuan", "initData: "+list.get(1).get("text").toString());
        back.setVisibility(View.VISIBLE);
        binding.cmdsList.setLayoutManager(new LinearLayoutManager(this));
        binding.cmdsList.setAdapter(new MyRecyclerAdapter<ItemCmdBinding>(this, R.layout.item_cmd, list.size(), new MyRecyclerAdapter.BindView<ItemCmdBinding>() {
            @Override
            public void onBindViewHolder(ItemCmdBinding b, int position) throws Exception {
                final int num = list.size() - position-1;
                b.titleCmdItem.setText(list.get(num).get("text").toString());
                Glide.with(getActivity()).load(list.get(num).getAVFile("pic").getUrl()).into(b.imgCmdItem);
                if (list.get(num).get("url").toString() != null) {
                    b.layoutCmdItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(CmdsActivity.this, WebActivity.class);
                            intent.putExtra("title", list.get(num).get("text").toString());
                            intent.putExtra("url", list.get(num).get("url").toString());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onRecycled(ItemCmdBinding b) {

            }
        }));
        binding.cmdsList.scrollToPosition(list.size()-1);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_cmds;
    }
}

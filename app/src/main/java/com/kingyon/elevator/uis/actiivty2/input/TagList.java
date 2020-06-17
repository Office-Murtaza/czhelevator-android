package com.kingyon.elevator.uis.actiivty2.input;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.actiivty2.input.adapter.TagAdapter;
import com.kingyon.elevator.uis.actiivty2.input.utils.Tag;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By Admin  on 2020/4/29
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:
 */
public class TagList extends BaseActivity {

  @BindView(R.id.recycler)
  RecyclerView recycler;
  private TagList mTagList;

  public static final String RESULT_TAG = "RESULT_TAG";
  private TagAdapter mTagAdapter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);

  }

  @Override
  public int getContentViewId() {
    return R.layout.common_recycler;
  }

  @Override
  public void init(Bundle savedInstanceState) {
    ButterKnife.bind(this);
    mTagList = this;
    initView();
  }

  private void initView() {
    List<Tag> result = new ArrayList<>();
//    NetService.getInstance().setOueryTopic(1,"")
//            .compose(this.bindLifeCycle())
//            .subscribe(new CustomApiCallback<QueryTopicEntity<QueryTopicEntity.PageContentBean>>() {
//              @Override
//              protected void onResultError(ApiException ex) {
//                LogUtils.e(ex);
//              }
//              @Override
//              public void onNext(QueryTopicEntity<QueryTopicEntity.PageContentBean> pageContentBeanQueryTopicEntity) {
//                for (int i = 0; i < pageContentBeanQueryTopicEntity.getPageContent().size(); i++) {
//                  Tag tag = new Tag(pageContentBeanQueryTopicEntity.getPageContent().get(i).getTitle(), String.valueOf(pageContentBeanQueryTopicEntity.getPageContent().get(i).getId()));
//                  result.add(tag);
//                }
//                recycler.setLayoutManager(new LinearLayoutManager(mTagList));
//                mTagAdapter = new TagAdapter(TagList.this,result);
//                recycler.setAdapter(mTagAdapter);
//              }
//            });
    mTagAdapter.setOnItemClickListener(new TagAdapter.ItemClickListener() {
      @Override
      public void onItemClick(int position) {
        List<Tag> data = mTagAdapter.getData();
        Tag tag = data.get(position);
        setResult(tag);
      }
    });
  }



  public static Intent getIntent(Activity activity) {
    return new Intent(activity, TagList.class);
  }

  private void setResult(Tag tag) {
    Intent intent = getIntent();
//    intent.putExtra(RESULT_TAG, tag);
    setResult(RESULT_OK, intent);
    finish();
  }
}



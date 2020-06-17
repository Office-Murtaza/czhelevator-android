package com.kingyon.elevator.uis.activities.devices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.CellDetailsEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.UploadImageAdapter;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.utils.AddressPickerUtil;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateLoadingActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by GongLi on 2019/1/17.
 * Email：lc824767150@163.com
 */

public class CellEditActivity extends BaseStateLoadingActivity implements AddressPickerUtil.OnAreaSelectedListener {

    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.rv_logo)
    RecyclerView rvLogo;
    @BindView(R.id.rv_images)
    RecyclerView rvImages;
    @BindView(R.id.tv_create)
    TextView tvCreate;

    private boolean edit;
    private long editCellId;

    private UploadImageAdapter logoAdapter;
    private UploadImageAdapter imagesAdapter;
    private AddressPickerUtil addressUtil;

    private OptionsPickerView typePicker;
    private List<NormalParamEntity> typeOptions;

    @Override
    protected String getTitleText() {
        edit = getIntent().getBooleanExtra(CommonUtil.KEY_VALUE_1, false);
        editCellId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_2, 0);
        return edit ? "编辑小区" : "添加小区";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cell_edit;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initAddressUtil();
        preVRight.setText("选取定位");
        preVRight.setVisibility(View.GONE);
        rvLogo.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.spacing_small), false));
        logoAdapter = new UploadImageAdapter(this, true);
        logoAdapter.setMaxCount(1);
        DealScrollRecyclerView.getInstance().dealAdapter(logoAdapter, rvLogo, new FullyGridLayoutManager(this, 3));
        logoAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<Object>() {
            @Override
            public void onItemClick(View view, int position, Object entity, BaseAdapterWithHF<Object> baseAdaper) {
                if (view.getId() == R.id.img_delete) {
                    logoAdapter.deleteItemData(entity);
                } else {
                    if (position >= logoAdapter.getItemCount() - logoAdapter.getFooterCount()) {
                        PictureSelectorUtil.showPictureSelector(CellEditActivity.this, CommonUtil.REQ_CODE_2, logoAdapter.getMaxCount() - logoAdapter.getItemRealCount());
                    }
                }
            }
        });

        rvImages.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.spacing_small), false));
        imagesAdapter = new UploadImageAdapter(this);
        imagesAdapter.setMaxCount(3);
        DealScrollRecyclerView.getInstance().dealAdapter(imagesAdapter, rvImages, new FullyGridLayoutManager(this, 3));
        imagesAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<Object>() {
            @Override
            public void onItemClick(View view, int position, Object entity, BaseAdapterWithHF<Object> baseAdaper) {
                if (view.getId() == R.id.img_delete) {
                    imagesAdapter.deleteItemData(entity);
                } else {
                    if (position >= imagesAdapter.getItemCount() - imagesAdapter.getFooterCount()) {
                        PictureSelectorUtil.showPictureSelectorNoCrop(CellEditActivity.this, CommonUtil.REQ_CODE_3, imagesAdapter.getMaxCount() - imagesAdapter.getItemRealCount());
                    }
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (edit) {
            NetService.getInstance().cellDetails(editCellId)
                    .compose(this.<CellDetailsEntity>bindLifeCycle())
                    .subscribe(new CustomApiCallback<CellDetailsEntity>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            showToast(ex.getDisplayMessage());
                            loadingComplete(STATE_ERROR);
                        }

                        @Override
                        public void onNext(CellDetailsEntity entity) {
                            if (entity == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            location = String.format("%s,%s", entity.getLongitude(), entity.getLatitude());

                            tvArea.setTag(entity.getRegionCode());
                            tvArea.setText(entity.getRegionName());

                            etAddress.setText(entity.getAddress());
                            etAddress.setSelection(etAddress.getText().length());

                            etName.setText(entity.getCellName());

                            tvType.setTag(entity.getCellType());
                            tvType.setText(FormatUtils.getInstance().getCellType(entity.getCellType()));

//                            etFlow.setText(String.valueOf(entity.getHumanTraffic()));

                            logoAdapter.addData(entity.getCellLogo());
                            imagesAdapter.addDatas(entity.getCellBanner());

                            loadingComplete(STATE_CONTENT);
                            preVRight.setVisibility(View.GONE);
                        }
                    });
        } else {
            loadingComplete(STATE_CONTENT);
            preVRight.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.pre_v_right, R.id.ll_area, R.id.ll_address, R.id.ll_name, R.id.ll_type,  R.id.tv_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_right:
//                startActivityForResult(CellLocationChooseActivity.class, CommonUtil.REQ_CODE_1);
                break;
            case R.id.ll_area:
//                KeyBoardUtils.closeKeybord(this);
//                tvArea.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        addressUtil.showPicker();
//                    }
//                }, 100);
//                if (tvArea.getTag() == null) {
//                    showToast("请选择定位");
//                }
                startActivityForResult(CellLocationChooseActivity.class, CommonUtil.REQ_CODE_1);
                break;
            case R.id.ll_address:
                if (TextUtils.isEmpty(CommonUtil.getEditText(etAddress))) {
                    showToast("请选择定位");
                }
                break;
            case R.id.ll_name:
                break;
            case R.id.ll_type:
                KeyBoardUtils.closeKeybord(this);
                showCellTypePicker();
                break;

            case R.id.tv_create:
                onSaveClick();
                break;
        }
    }

    private String location;
    private Long adCode;
    private String address;
    private String cellName;
    private String cellType;
    private Long flow;
    private String logoResult;
    private String imagesResult;

    private List<File> logoFile;
    private List<String> logoAll;
    private List<File> imageFiles;
    private List<String> imageAll;

    private void onSaveClick() {
        if (TextUtils.isEmpty(location)) {
            showToast("请选择定位");
            return;
        }

        adCode = (Long) tvArea.getTag();
        if (adCode == null) {
            showToast("请选择地区");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etAddress))) {
            showToast("请输入地址");
            return;
        }
        address = etAddress.getText().toString();

        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            showToast("请输入小区名称");
            return;
        }
        cellName = etName.getText().toString();

        cellType = (String) tvType.getTag();
        if (TextUtils.isEmpty(cellType)) {
            showToast("请选择小区类型");
            return;
        }

        flow = null;
//        if (!TextUtils.isEmpty(CommonUtil.getEditText(etFlow))) {
//            try {
//                flow = Long.parseLong(etFlow.getText().toString());
//            } catch (NumberFormatException e) {
//                flow = null;
//            }
//            if (flow == null || flow <= 0) {
//                showToast("输入的人流量信息有误");
//                return;
//            }
//        }
//        if (flow == null) {
//            flow = 0L;
//        }
        if (logoAdapter.getItemRealCount() <= 0) {
            showToast("请选择小区封面");
            return;
        }
        logoFile = logoAdapter.getUploadDatas();
        logoAll = logoAdapter.getAllDatas();

        if (imagesAdapter.getItemRealCount() <= 0) {
            showToast("请选择小区照片");
            return;
        }
        imageFiles = imagesAdapter.getUploadDatas();
        imageAll = imagesAdapter.getAllDatas();

        tvCreate.setEnabled(false);
        showProgressDialog(getString(R.string.wait));

        dealLogo();
    }

    private void dealLogo() {
        if (logoFile != null && logoFile.size() > 0) {
            showProgressDialog("小区封面上传中...");
            NetService.getInstance().uploadFile(this, logoFile.get(0), new NetUpload.OnUploadCompletedListener() {
                @Override
                public void uploadSuccess(List<String> images, List<String> hash,JSONObject response) {
                    LogUtils.e(images,hash,response);
                    if (images != null && images.size() > 0) {
                        logoResult = images.get(0);
                        dealImages();
                    } else {
                        hideProgress();
                        tvCreate.setEnabled(true);
                        showToast("上传图片失败");
                    }
                }

                @Override
                public void uploadFailed(ApiException ex) {
                    hideProgress();
                    tvCreate.setEnabled(true);
                    showToast("上传图片失败");
                }
            });
        } else {
            logoResult = NetService.getInstance().getUploadResultString(logoAll);
            dealImages();
        }
    }

    private void dealImages() {
        if (imageFiles != null && imageFiles.size() > 0) {
            showProgressDialog("小区照片上传中...");
            NetService.getInstance().uploadFiles(this, imageFiles, new NetUpload.OnUploadCompletedListener() {
                @Override
                public void uploadSuccess(List<String> images,List<String> hash,JSONObject response) {
                    LogUtils.e(images,hash,response);
                    if (images != null && images.size() == imageFiles.size()) {
                        try {
                            List<String> results = new ArrayList<>();
                            int index = 0;
                            for (String old : imageAll) {
                                if (!TextUtils.isEmpty(old) && !old.startsWith("http")) {
                                    results.add(images.get(index++));
                                } else {
                                    results.add(old);
                                }
                            }
                            imagesResult = NetService.getInstance().getUploadResultString(results);
                            publishRequest();
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            tvCreate.setEnabled(true);
                            showToast("上传图片失败");
                        }
                    } else {
                        hideProgress();
                        tvCreate.setEnabled(true);
                        showToast("上传图片失败");
                    }
                }

                @Override
                public void uploadFailed(ApiException ex) {
                    hideProgress();
                    tvCreate.setEnabled(true);
                    showToast("上传图片失败");
                }
            });
        } else {
            imagesResult = NetService.getInstance().getUploadResultString(imageAll);
            publishRequest();
        }
    }


    private void publishRequest() {
        showProgressDialog(getString(R.string.wait));
        double[] centerLatLon = FormatUtils.getInstance().getCenterLatLon(location);
        NetService.getInstance().addCell(edit ? editCellId : null, String.valueOf(adCode), address
                , cellName, cellType, flow, logoResult, imagesResult, centerLatLon[0], centerLatLon[1])
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        tvCreate.setEnabled(true);
                        hideProgress();
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        tvCreate.setEnabled(true);
                        showToast("提交成功");
                        hideProgress();
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    private void showCellTypePicker() {
        if (typePicker == null || typeOptions == null) {
            typeOptions = new ArrayList<>();
            typeOptions.add(new NormalParamEntity(Constants.CELL_TYPE.COMMERCIAL, FormatUtils.getInstance().getCellType(Constants.CELL_TYPE.COMMERCIAL)));
            typeOptions.add(new NormalParamEntity(Constants.CELL_TYPE.HOUSE, FormatUtils.getInstance().getCellType(Constants.CELL_TYPE.HOUSE)));
            typeOptions.add(new NormalParamEntity(Constants.CELL_TYPE.OFFICE, FormatUtils.getInstance().getCellType(Constants.CELL_TYPE.OFFICE)));
            typePicker = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    if (typeOptions == null || typeOptions.size() <= options1) {
                        return;
                    }
                    NormalParamEntity entity = typeOptions.get(options1);
                    tvType.setTag(entity.getType());
                    tvType.setText(entity.getName());
                }
            }).setCyclic(false, false, false).build();
            typePicker.setPicker(typeOptions);
        }
        typePicker.show();
    }

    private void initAddressUtil() {
        addressUtil = new AddressPickerUtil(this, true, true, true, "gd_district_py.json");
        addressUtil.setSelectListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_1:
                    PoiItem poi = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    if (poi != null) {
                        location = String.format("%s,%s", poi.getLatLonPoint().getLongitude(), poi.getLatLonPoint().getLatitude());
                        tvArea.setTag(Long.parseLong(poi.getAdCode()));
                        tvArea.setText(String.format("%s%s%s", poi.getProvinceName(), poi.getCityName(), poi.getAdName()));
                        etAddress.setText(poi.getSnippet());
                        etAddress.setSelection(etAddress.getText().length());
                    }
                    break;
                case CommonUtil.REQ_CODE_2:
                    ArrayList<String> mSelectPath1 = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (mSelectPath1 != null && mSelectPath1.size() > 0) {
                        logoAdapter.addDatas(mSelectPath1);
                    }
                    break;
                case CommonUtil.REQ_CODE_3:
                    ArrayList<String> mSelectPath2 = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (mSelectPath2 != null && mSelectPath2.size() > 0) {
                        imagesAdapter.addDatas(mSelectPath2);
                    }
                    break;
            }
        }
    }

    @Override
    public void onAreaSelect(AMapCityEntity first, AMapCityEntity second, AMapCityEntity third, int op1, int op2, int op3, View v) {
        if (third != null) {
            tvArea.setTag(Long.parseLong(third.getAdcode()));
            tvArea.setText(String.format("%s%s%s", first != null ? first.getName() : "", second != null ? second.getName() : "", third.getName()));
        }
    }

    @Override
    protected void onDestroy() {
        if (addressUtil != null) {
            addressUtil.onDestroy();
        }
        super.onDestroy();
    }
}

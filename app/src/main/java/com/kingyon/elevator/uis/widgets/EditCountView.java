package com.kingyon.elevator.uis.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.kingyon.elevator.R;

/**
 * Created by arvin on 2016/6/15 11:08
 */
public class EditCountView extends FrameLayout {

    private View decrease;
    private View increase;
    private EditText edInput;

    private int currentCount = 1;
    private int maxCount = Integer.MAX_VALUE;
    private int minCount = 1;

    public OnNumberChange onNumberChange;

    public void setMaxCount(int maxCount) {
        if (maxCount < 1) {
            maxCount = 1;
        }
        this.maxCount = maxCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
        setCount();
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public EditCountView(Context context) {
        this(context, null, 0);
    }

    public EditCountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initEvent();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.layout_edit_count, null);
        decrease = view.findViewById(R.id.img_decrease);
        increase = view.findViewById(R.id.img_increase);
        edInput = (EditText) view.findViewById(R.id.ed_input);
        edInput.setSelection(edInput.getText().toString().trim().length());
        edInput.setEnabled(false);
        addView(view);
    }

    private void initEvent() {
        decrease.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCount == minCount) {
                    cannotLessTips();
                    return;
                }
                currentCount--;
                setCount();
                if (onNumberChange != null) {
                    onNumberChange.onChange(getCurrentCount());
                }
            }
        });
        increase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCount == maxCount) {
                    noMoreTips();
                    return;
                }
                currentCount++;
                setCount();
                if (onNumberChange != null) {
                    onNumberChange.onChange(getCurrentCount());
                }
            }
        });

        edInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int inputCount = 0;
                if (s.length() > 0) {
                    inputCount = Integer.valueOf(s.toString());
                }
                if (inputCount > maxCount) {
                    noMoreTips();
                    setCount();
                    return;
                }

                if (inputCount < minCount) {
                    cannotLessTips();
                    setCount();
                    return;
                }
                currentCount = inputCount;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onNumberChange != null) {
                    onNumberChange.onChange(getCurrentCount());
                }
            }
        });
    }

    private void setCount() {
        edInput.setText("" + currentCount);
        edInput.setSelection(edInput.getText().length());
    }

    private void cannotLessTips() {
        if (limitToastCallBack == null) {
            Toast.makeText(getContext(), "不能再小了~", Toast.LENGTH_SHORT).show();
        } else {
            limitToastCallBack.cannotLessTips();
        }
    }

    private void noMoreTips() {
        if (limitToastCallBack == null) {
            Toast.makeText(getContext(), "库存不够了~", Toast.LENGTH_SHORT).show();
        } else {
            limitToastCallBack.noMoreTips();
        }
    }

    public void setCanEdit(boolean canEdit) {
        edInput.setEnabled(canEdit);
    }

    public interface OnNumberChange {
        void onChange(int num);
    }

    public void setOnNumberChange(OnNumberChange onNumberChange) {
        this.onNumberChange = onNumberChange;
    }

    public void setEnable(boolean isEnable) {
        if (isEnable) {
            decrease.setEnabled(true);
            increase.setEnabled(true);
            edInput.setEnabled(false);
            edInput.setTextColor(0xff000000);
        } else {
            decrease.setEnabled(false);
            increase.setEnabled(false);
            edInput.setEnabled(false);
            edInput.setTextColor(getContext().getResources().getColor(R.color.black_hint));
        }
    }

    public interface LimitToastCallBack {
        void cannotLessTips();

        void noMoreTips();
    }

    private LimitToastCallBack limitToastCallBack;

    public LimitToastCallBack getLimitToastCallBack() {
        return limitToastCallBack;
    }

    public void setLimitToastCallBack(LimitToastCallBack limitToastCallBack) {
        this.limitToastCallBack = limitToastCallBack;
    }
}

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
public class EditCountViewInList extends FrameLayout {

    private View decrease;
    private View increase;
    private EditText edInput;

    private int currentCount = 1;
    private int maxCount = Integer.MAX_VALUE;
    private int minCount = 1;

    private int position = 0;
    public OnNumberChange onNumberChange;

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public void setCurrentCount(int currentCount) {
        if (currentCount < minCount) {
            currentCount = minCount;
        }
        if (currentCount > maxCount) {
            currentCount = maxCount;
        }
        this.currentCount = currentCount;
        setCount();
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public EditCountViewInList(Context context) {
        this(context, null, 0);
    }

    public EditCountViewInList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditCountViewInList(Context context, AttributeSet attrs, int defStyleAttr) {
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
                    onNumberChange.onChange(getCurrentCount(), position, edInput);
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
                    onNumberChange.onChange(getCurrentCount(), position, edInput);
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
                    onNumberChange.onChange(getCurrentCount(), position, edInput);
                }
            }
        });
    }

    private void setCount() {
        edInput.setText("" + currentCount);
        edInput.setSelection(edInput.getText().length());
    }

    private void cannotLessTips() {
        Toast.makeText(getContext(), "不能再小了~", Toast.LENGTH_SHORT).show();
    }

    private void noMoreTips() {
        Toast.makeText(getContext(), "不能再大了~", Toast.LENGTH_SHORT).show();
    }

    public void setCanEdit(boolean canEdit) {
        edInput.setEnabled(canEdit);
    }

    public interface OnNumberChange {
        void onChange(int num, int position, EditText text);
    }

    public void setOnNumberChange(OnNumberChange onNumberChange, int position) {
        this.onNumberChange = onNumberChange;
        this.position = position;
    }

    public void removeOnNumberChange() {
        this.onNumberChange = null;
        this.position = -1;
    }

}

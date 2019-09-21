package com.example.lendandborrowclient.Validation;

import android.support.design.widget.TextInputLayout;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;

public class TextInputLayoutDataAdapter implements ViewDataAdapter<TextInputLayout, String>
{
    @Override
    public String getData(TextInputLayout view) {
        return view.getEditText().getText().toString();
    }
}

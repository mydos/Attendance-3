package com.sust.attendence.Listener;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.sust.attendence.Others.LogginActivity;
import com.sust.attendence.Others.ToastMessage;
import com.sust.attendence.R;

public class addCustomTextChangedListener implements TextWatcher {

	private EditText mEditText;
	private Context mContext;
	String mEditTextText;

	public addCustomTextChangedListener(Context context, EditText et) {
		this.mEditText = et;
		this.mContext = context;
		mEditText.addTextChangedListener(this);
	}
	public void validate() {
		mEditTextText = mEditText.getText().toString().trim();
		switch (mEditText.getId()) {
		case R.id.register_email_field_et:
			if (mEditTextText.length() == 0) {
				mEditText.setError("Email Required");
			} else {
				if(isValidEmail(mEditTextText)) {
					mEditText.setError(null);
					LogginActivity.validation_map.put("register_email_et", true);
				}
				else{
					mEditText.setError("Provide Valid Email");
				}
			}
			break;
		case R.id.register_password_field_et:
			if (mEditTextText.length() == 0) {
				mEditText.setError("Password Required");
			} else {
				mEditText.setError(null);
				LogginActivity.validation_map.put("register_password_et", true);
			}
			break;
		case R.id.register_name_field_et:
			if (mEditTextText.length()==0) {
				mEditText.setError("Name Required");
			} else {
				mEditText.setError(null);
				LogginActivity.validation_map.put("register_name_et", true);
			}
			break;
		}
	}
	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		validate();
	}
	public void removeCustomTextChangedListener(){
		mEditText.removeTextChangedListener(this);
		mEditText.setError(null);
	}

}

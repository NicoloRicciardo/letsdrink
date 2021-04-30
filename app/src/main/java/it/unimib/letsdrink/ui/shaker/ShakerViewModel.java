package it.unimib.letsdrink.ui.shaker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShakerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShakerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is shaker fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.zii.easy.vm;

import androidx.arch.core.util.Function;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * Created by zii on 2019/7/1.
 */
public class TestViewModule extends ViewModel {

  public TestViewModule() {
    MutableLiveData<String> source = new MutableLiveData<>();
    MutableLiveData<Integer> integer = new MutableLiveData<>();
    String value = Transformations.map(source, input -> input).getValue();
    Transformations.map(integer, new Function<Integer, String>() {
      @Override
      public String apply(Integer input) {
        return null;
      }
    });
  }

}

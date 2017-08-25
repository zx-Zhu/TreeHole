package com.zxzhu.show.view.Inference;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zxzhu on 2017/8/23.
 */

public interface ISquareContentActivity {
    void setBar();
    void setList(List<String> list, HashMap<String, Object> audios);
    void setContent();
    void hideRefresh();

}

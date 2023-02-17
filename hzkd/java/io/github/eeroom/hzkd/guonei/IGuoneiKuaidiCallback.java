package io.github.eeroom.hzkd.guonei;

import io.github.eeroom.hzkd.httpclient.ApiMapping;
import io.github.eeroom.hzkd.viewmodel.ApidataWrapper;

public interface IGuoneiKuaidiCallback {

    @ApiMapping(action = "",wrapperType = ApidataWrapper.class)
    public FeedResponse execute(FeedMessage msg);
}

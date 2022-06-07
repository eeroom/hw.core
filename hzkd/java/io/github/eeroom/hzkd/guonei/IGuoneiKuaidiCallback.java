package io.github.eeroom.hzkd.guonei;

import io.github.eeroom.hzkd.viewmodel.ApidataWrapper;
import io.github.eeroom.remoting.proxy.ApiMapping;

public interface IGuoneiKuaidiCallback {

    @ApiMapping(action = "",wrapperType = ApidataWrapper.class)
    public FeedResponse execute(FeedMessage msg);
}

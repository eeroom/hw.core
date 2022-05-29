package io.github.eeroom.hzcore.hzkd.api;

import io.github.eeroom.proxyclient.ApiMapping;
import io.github.eeroom.hzcore.ApidataWrapper;
import io.github.eeroom.hzcore.hzkd.guonei.FeedMessage;
import io.github.eeroom.hzcore.hzkd.guonei.FeedResponse;

public interface IGuoneiKuaidiCallback {

    @ApiMapping(action = "",wrapperType = ApidataWrapper.class)
    public FeedResponse execute(FeedMessage msg);
}

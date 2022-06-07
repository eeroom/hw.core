package io.github.eeroom.hzoa.hzkd;

import io.github.eeroom.remoting.proxy.ApiMapping;

public interface IGuoneiKuaidiCallback {

    @ApiMapping(action = "",wrapperType = ApidataWrapper.class)
    public FeedResponse execute(FeedMessage msg);
}

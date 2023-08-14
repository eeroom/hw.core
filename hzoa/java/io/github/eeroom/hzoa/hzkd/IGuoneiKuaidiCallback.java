package io.github.eeroom.hzoa.hzkd;


import io.github.eeroom.hzoa.httpclient.ApiMapping;

public interface IGuoneiKuaidiCallback {

    @ApiMapping(action = "",wrapperType = ApidataWrapper.class)
    public FeedResponse execute(FeedMessage msg);
}

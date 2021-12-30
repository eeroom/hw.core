package io.github.eeroom.gtop.api.sf.kuaidi;

import io.github.eeroom.apiclient.ApiMapping;
import io.github.eeroom.gtop.entity.ApidataWrapper;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedResponse;

public interface IGuoneiKuaidiCallback {

    @ApiMapping(action = "",wrapperType = ApidataWrapper.class)
    public FeedResponse execute(FeedMessage msg);
}

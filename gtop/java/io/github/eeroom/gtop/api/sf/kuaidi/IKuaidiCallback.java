package io.github.eeroom.gtop.api.sf.kuaidi;

import io.github.eeroom.gtop.entity.sf.kuaidi.FeedMessage;
import io.github.eeroom.gtop.entity.sf.kuaidi.FeedResponse;

public interface IKuaidiCallback {

    public FeedResponse feed(FeedMessage msg);
}
